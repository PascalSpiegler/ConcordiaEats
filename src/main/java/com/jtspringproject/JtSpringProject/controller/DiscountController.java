package com.jtspringproject.JtSpringProject.controller;


import com.jtspringproject.JtSpringProject.model.Category;
import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.model.Restaurant;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.jtspringproject.JtSpringProject.service.ProductService;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DiscountController {
    @Value("${spring.datasource.username}")
    public
    String db_username;

    @Value("${spring.datasource.password}")
    public
    String db_password;

    @Value("${spring.datasource.url}")
    public
    String db_url;

    private final ProductService productService;
    private final BasketService basketService;
    private final UserService userService;



    public DiscountController(ProductService productService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.basketService = basketService;
        this.userService = userService;
    }


    @RequestMapping("/discountproducts")
    public String redirectProducts(ModelMap model, HttpSession session) {
        if (userService.isCustomer(session)){
            return "redirect:/user/discountproducts";
        } else if (userService.isAdmin(session)) {
            System.out.println("User is an admin.");
            return "redirect:/admin/discountproducts";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/admin/discountproducts")
    public String getdiscountproduct(Model model, HttpSession session) {
        if (userService.isAdmin(session)) {
            try {
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);
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

                List<Product> discountProducts = new ArrayList<>();

                for (int discountId : discountIds) {
                    String query2 = "SELECT product_id, restaurant_ID, product_name, image, price, product_description, category_id FROM Products WHERE product_id = ?;";
                    PreparedStatement stmt2 = con.prepareStatement(query2);
                    stmt2.setInt(1, discountId);
                    ResultSet rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        Product product = new Product();
                        product.setId(rs2.getInt("product_id"));
                        product.setRestaurant_id(rs2.getInt("restaurant_ID"));
                        product.setName(rs2.getString("product_name"));
                        product.setImage(rs2.getString("image"));
                        product.setPrice(rs2.getBigDecimal("price"));
                        product.setDescription(rs2.getString("product_description"));
                        product.setCategoryId(rs2.getInt("category_id"));
                        System.out.println(product);
                        discountProducts.add(product);
                    }
                }

                model.addAttribute("discounts", discounts);
                model.addAttribute("discountIds", discountIds);
                model.addAttribute("discountProducts", discountProducts);


                return "adminDiscountProducts";

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/admin/discountproducts/update")
    public String updatediscount(@RequestParam("pid") int id, Model model, HttpSession session) {

        if (userService.isAdmin(session)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);

                PreparedStatement pst = con.prepareStatement("select restaurant_ID, product_name, image, price, product_description, category_id from Products WHERE product_id = " + id + ";");
                ResultSet rs = pst.executeQuery();

                Product product = new Product();
                while (rs.next()) {
                    product.setId(id);
                    product.setRestaurant_id(rs.getInt("restaurant_ID"));
                    product.setName(rs.getString("product_name"));
                    product.setImage(rs.getString("image"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setDescription(rs.getString("product_description"));
                    product.setCategoryId(rs.getInt("category_id"));

                }


                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("select product_id, percent_savings from Discounts");

                Map<Integer, Double> discounts = new HashMap<>();
                List<Integer> discountIds = new ArrayList<>();

                while (rs2.next()) {
                    int productId = rs2.getInt("product_id");
                    double percentSavings = rs2.getDouble("percent_savings");
                    discounts.put(productId, percentSavings);
                    discountIds.add(productId);
                }
                model.addAttribute("discounts", discounts);
                model.addAttribute("product", product);

            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }

            return "discountsUpdate";
        } else {
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/admin/discountproducts/updateData",method= RequestMethod.POST)
    public String updateproducttodb(@RequestParam("id") int id,  @RequestParam("discountPercentage") Double percentSavings, HttpSession session)

    {
        if (userService.isAdmin(session)) {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);

                PreparedStatement pst = con.prepareStatement("update Discounts set percent_savings = ? WHERE product_id = ?;");
                pst.setDouble(1, percentSavings);
                pst.setInt(2, id);
                int i = pst.executeUpdate();
                System.out.println("Executed " + i);
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }


            return "redirect:/admin/discountproducts";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/admin/discountproducts/delete")
    public String removeProductDb(@RequestParam("id") String stringId, HttpSession session)
    {
        if (userService.isAdmin(session)) {

            int id = Integer.parseInt(stringId);

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);


                PreparedStatement pst = con.prepareStatement("delete from Discounts where product_id = ? ;");
                pst.setInt(1, id);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }
            return "redirect:/admin/discountproducts";
        } else {
            return "redirect:index";
        }
    }

    @GetMapping("/admin/discountproducts/add")
    public String addproduct(Model model, HttpSession session) {
        if (userService.isAdmin(session)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);

                PreparedStatement pst = con.prepareStatement("SELECT * FROM Products WHERE product_id NOT IN (SELECT product_id FROM Discounts)");
                ResultSet rs = pst.executeQuery();

                List<Product> availableProducts = new ArrayList<>();
                while (rs.next()) {
                    Product availableProduct = new Product();
                    availableProduct.setId(rs.getInt("product_id"));
                    availableProduct.setRestaurant_id(rs.getInt("restaurant_ID"));
                    availableProduct.setName(rs.getString("product_name"));
                    availableProduct.setImage(rs.getString("image"));
                    availableProduct.setPrice(rs.getBigDecimal("price"));
                    availableProduct.setDescription(rs.getString("product_description"));
                    availableProduct.setCategoryId(rs.getInt("category_id"));
                    availableProducts.add(availableProduct);
                }
                model.addAttribute("availableProducts", availableProducts);
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }


            return "discountsAdd";
        } else {
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "admin/discountproducts/sendData", method = RequestMethod.POST)
    public String addproducttodb(@RequestParam("id") int id, @RequestParam("discountPercentage") double discountPercentage, HttpSession session) {
        if (userService.isAdmin(session)) {
            try {
                Connection con = DriverManager.getConnection(db_url, db_username, db_password);

                PreparedStatement pst = con.prepareStatement("INSERT INTO Discounts (product_id, percent_savings) VALUES (?, ?)");
                pst.setInt(1, id);
                pst.setDouble(2, discountPercentage);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
            return "redirect:/admin/discountproducts";
        } else {
            return "redirect:/index";
        }
    }

}
