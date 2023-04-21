package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TransactionController {
    private final BasketService basketService;

    @Value("${stripe.api.key}")
    String stripeKey;

    @Value("${spring.datasource.username}")
    String db_username;

    @Value("${spring.datasource.password}")
    String db_password;

    @Value("${spring.datasource.url}")
    String db_url;

    public TransactionController(BasketService basketService) {
        this.basketService = basketService;
    }

    @RequestMapping("/checkout")
    public void checkout(HttpServletResponse response) throws StripeException, IOException {
        Stripe.apiKey = stripeKey;
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", Arrays.asList("card"));
        List<Object> lineItems = new ArrayList<>();
        Map<Product, Integer> basket = basketService.getProductsInBasket();

        if (!basket.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : basket.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                Map<String, Object> item = new HashMap<>();
                item.put("name", product.getName());
//              Amount in cents
                item.put("amount", (int) (product.getPrice().doubleValue() * 100));
                item.put("quantity", quantity);
                item.put("currency", "cad");

                ArrayList<String> taxRates = new ArrayList<>();
                taxRates.add("txr_1MtdVbLBUeoQx8lkj8UztWHS");

                item.put("tax_rates", taxRates);
                lineItems.add(item);
            }
            params.put("line_items", lineItems);
            params.put("success_url", "http://localhost:8080/paymentSuccess?session_id={CHECKOUT_SESSION_ID}");
            params.put("cancel_url", "http://localhost:8080/paymentFailed");


            Session session = Session.create(params);
            response.setStatus(303);
            response.setHeader("Location", session.getUrl());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Basket is empty.");
        }
    }

    @RequestMapping("/paymentSuccess")
    public ModelAndView paymentSucess(@RequestParam("session_id") String sessionId, ModelMap model, HttpSession userSession) throws StripeException {

        Stripe.apiKey = stripeKey;

        //We use a try catch block to make sure the user has a valid session id corresponding to their order (so they can't refresh the page and place orders multiple times)
        try {
            Session stripeSession = Session.retrieve(sessionId);
            Map<Product, Integer> basket = basketService.getProductsInBasket();
            generateTransactionRecord(basket, userSession); //Generate a transaction record for future analytics
            basketService.clearBasket(); //Clear basket
            ModelAndView modelAndView = new ModelAndView("/paymentSuccess");
            return modelAndView;
            // perform actions with the session and customer objects
        } catch (StripeException e) {
            // handle the case where the session ID is invalid
            ModelAndView modelAndView = new ModelAndView("/invalidStripeSession");
            model.addAttribute("sessionId", sessionId);
            return modelAndView;
        }

    }


    @GetMapping("/paymentFailed")
    public ModelAndView paymentFailed() {
        ModelAndView modelAndView = new ModelAndView("/paymentFailed");
        return modelAndView;
    }

    public void generateTransactionRecord(Map<Product, Integer> basket, HttpSession session) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_password);
            if (!basket.isEmpty()) {
                for (Map.Entry<Product, Integer> entry : basket.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    PreparedStatement pst = con.prepareStatement("insert into Transactions(user_id,product_id, quantity, vendor_id, price) values(?,?,?,?,?);");
                    String userIdStr = (String) session.getAttribute("loggedInUser");
                    int userId = Integer.parseInt(userIdStr);
                    System.out.println("The user id is " + userId);
                    pst.setInt(1, userId); //User ID
                    pst.setInt(2, product.getId()); //Product ID
                    pst.setInt(3, quantity); //Quantity
                    pst.setInt(4, product.getRestaurantId()); //Vendor ID
                    pst.setInt(5, (int) (product.getPrice().doubleValue() * 100)); //Price in cents
                    int i = pst.executeUpdate();
                }
                System.out.println("Transaction successful, records added to database");
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }

    }

    @GetMapping("/orderHistory")
    public ModelAndView orderHistory(HttpSession userSession) {
        ModelAndView modelAndView = new ModelAndView("orderHistory");
        String userIdStr = (String) userSession.getAttribute("loggedInUser");
        int userId = Integer.parseInt(userIdStr);
        System.out.println("The user id is " + userId);

        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_password);
            String query = "SELECT t.quantity, t.price, t.timestamp, p.product_name FROM Transactions t JOIN Products p ON t.product_id = p.product_id WHERE t.user_id = ? ORDER BY t.timestamp DESC;";
            List<Map<String, String>> transactionList = new ArrayList<>();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> transactionInfo = new HashMap<>();
                transactionInfo.put("product_name", rs.getString("product_name"));
                transactionInfo.put("quantity", Integer.toString(rs.getInt("quantity")));
                int priceInCents = rs.getInt("price");
                String priceInDollars = String.format("$%.2f", priceInCents / 100.0);
                transactionInfo.put("price", priceInDollars);
                String totalPrice = String.format("$%.2f", (rs.getInt("price") / 100.0) * rs.getInt("quantity") * 1.14975);
                transactionInfo.put("totalPrice", totalPrice);
                Timestamp timestamp = rs.getTimestamp("timestamp");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestampString = dateFormat.format(timestamp);
                transactionInfo.put("timestamp", timestampString);
                transactionList.add(transactionInfo);
            }

            modelAndView.addObject("transactionList", transactionList);
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return modelAndView;
    }
}



