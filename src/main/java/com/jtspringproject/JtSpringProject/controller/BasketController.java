package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.jtspringproject.JtSpringProject.service.ProductService;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

@Controller
public class BasketController {
    private final BasketService basketService;
    private final ProductService productService;
    private final UserService userService;

    @Value("${spring.datasource.username}")
    String db_username;

    @Value("${spring.datasource.password}")
    String db_password;

    @Value("${spring.datasource.url}")
    String db_url;

    @Autowired
    public BasketController(BasketService basketService, ProductService productService, UserService userService) {
        this.basketService = basketService;
        this.productService = productService;
        this.userService = userService;
    }


    @GetMapping("/basket")
    public ModelAndView basket(HttpSession session) throws SQLException {
        if (userService.isAdmin(session)){
            ModelAndView modelAndView = new ModelAndView("/adminBasket");
            return modelAndView;
        } else {

            ModelAndView modelAndView = new ModelAndView("/basket");
            Map<Product, Integer> basketObject = basketService.getProductsInBasket();
            double subtotal = 0.0;
            for (Map.Entry<Product, Integer> entry : basketObject.entrySet()) {
                int quantity = entry.getValue();
                double price = entry.getKey().getPrice().doubleValue();
                double itemSubtotal = price * quantity;
                subtotal += itemSubtotal;
            }

            Connection con = DriverManager.getConnection(db_url,db_username,db_password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select product_id, percent_savings from Discounts");

            Map<Integer, Double> discounts = new HashMap<>();
            List<Integer> discountIds = new ArrayList<>();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                double percentSavings = rs.getDouble("percent_savings");
                discounts.put(productId, percentSavings);
                discountIds.add(productId);
            }



            double qst = subtotal * 0.09975;
            double gst = subtotal * 0.05;
            double total = subtotal + qst + gst; //This double must have 2 decimal points

            DecimalFormat df = new DecimalFormat("#.##"); //We want our prices to be rounded to 2 decimals
            df.setMinimumFractionDigits(2);

            modelAndView.addObject("basket", basketObject);
            modelAndView.addObject("subtotal", df.format(subtotal));
            modelAndView.addObject("qst", df.format(qst));
            modelAndView.addObject("gst", df.format(gst));
            modelAndView.addObject("total", df.format(total));
            modelAndView.addObject("discounts", discounts);
            modelAndView.addObject("discountIds", discountIds);

            return modelAndView;
        }
    }


    @GetMapping("/basket/addProduct/{productId}")
    public void addProduct(@PathVariable("productId") int productId) throws SQLException {
        Connection con = DriverManager.getConnection(db_url, db_username, db_password);
        String query = "SELECT * FROM Products WHERE product_id = ?;";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("product_id"));
            product.setRestaurant_id(rs.getInt("restaurant_ID"));
            product.setName(rs.getString("product_name"));
            product.setImage(rs.getString("image"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setDescription(rs.getString("product_description"));
            product.setCategoryId(rs.getInt("category_id"));
            System.out.println(product);
            basketService.addProduct(product);
        }
        con.close();
    }

    @GetMapping("/basket/removeProduct/{productId}")
    public void removeProduct(@PathVariable("productId") int productId) throws SQLException {
        Connection con = DriverManager.getConnection(db_url, db_username, db_password);
        String query = "SELECT * FROM Products WHERE product_id = ?;";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("product_id"));
            product.setRestaurant_id(rs.getInt("restaurant_ID"));
            product.setName(rs.getString("product_name"));
            product.setImage(rs.getString("image"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setDescription(rs.getString("product_description"));
            product.setCategoryId(rs.getInt("category_id"));
            System.out.println(product);
            basketService.removeProduct(product);
        }
        con.close();
    }

}
