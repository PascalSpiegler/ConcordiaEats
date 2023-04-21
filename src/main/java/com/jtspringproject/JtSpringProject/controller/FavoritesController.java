package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.jtspringproject.JtSpringProject.service.ProductService;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FavoritesController {

    @Value("${spring.datasource.username}")
    public
    String db_username;

    @Value("${spring.datasource.password}")
    public
    String db_password;

    @Value("${spring.datasource.url}")
    public
    String db_url;

    @Autowired
    private ProductService productService; // assuming you have a ProductService that can retrieve a Product by its ID

    @Autowired
    private UserService userService; // assuming you have a UserService that can retrieve the logged-in user

    private final BasketService basketService;

    public FavoritesController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/favourites")
    public String showFavorites(HttpSession session, Model model) throws SQLException {
        if (session.getAttribute("loggedInUser") != null){
            String userIdStr = (String) session.getAttribute("loggedInUser");
            int userId = Integer.parseInt(userIdStr);
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);
            List<Integer> favourites = new ArrayList<>();
            String query = "SELECT product_id FROM favorites WHERE user_id = ? AND favorited_at IS NOT NULL;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favourites.add(rs.getInt("product_id"));
            }



            List<Product> products = new ArrayList<>();

            for (int favourite : favourites) {
                String query2 = "SELECT product_id, restaurant_ID, product_name, image, price, product_description, category_id FROM Products WHERE product_id = ?;";
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt2.setInt(1, favourite);
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
                    products.add(product);
                }
            }

            Statement stmt3 = con.createStatement();
            ResultSet rs3 = stmt3.executeQuery("select product_id, percent_savings from Discounts");

            Map<Integer, Double> discounts = new HashMap<>();
            List<Integer> discountIds = new ArrayList<>();

            while (rs3.next()) {
                int productId = rs3.getInt("product_id");
                double percentSavings = rs3.getDouble("percent_savings");
                discounts.put(productId, percentSavings);
                discountIds.add(productId);
            }

            model.addAttribute("discounts", discounts);
            model.addAttribute("discountIds", discountIds);
            model.addAttribute("products", products);
            model.addAttribute("favourites", favourites);
            model.addAttribute("basketService", basketService);

            System.out.println(products);
        }



        return "favourites";
    }

    @PostMapping("/favourites/add/{productId}")
    @ResponseBody
    public String addToFavorites(@PathVariable("productId") int productId, HttpSession session) {
        String userIdStr = (String) session.getAttribute("loggedInUser");
        int userId = Integer.parseInt(userIdStr);
        String query = "INSERT INTO favorites (user_id, product_id, favorited_at) " +
                "VALUES (?, ?, NOW()) " +
                "ON DUPLICATE KEY UPDATE favorited_at = IF(favorited_at IS NULL, NOW(), NULL)";
        try (Connection conn = DriverManager.getConnection(db_url, db_username, db_password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

}