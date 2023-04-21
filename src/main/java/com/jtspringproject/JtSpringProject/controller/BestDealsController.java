package com.jtspringproject.JtSpringProject.controller;
import com.jtspringproject.JtSpringProject.model.Category;
import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.jtspringproject.JtSpringProject.service.ProductService;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
public class BestDealsController {
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

    private RestTemplate restTemplate = new RestTemplate();


    Map<String, Double> score = new HashMap<>();

    public BestDealsController(ProductService productService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.basketService = basketService;
        this.userService = userService;

        score.put("discountScore", 0.25);
        score.put("favouriteScore", 0.5);
        score.put("personalSalesScore", 1.0);
        score.put("globalSalesScore", 50.0);
    }


    @RequestMapping("/bestdeals")
    public String redirectBestDeals(ModelMap model, HttpSession session) {
        if (userService.isCustomer(session)){
            return "redirect:/user/bestdeals";
        } else if (userService.isAdmin(session)) {
            System.out.println("User is an admin.");
            return "redirect:/adminhome";
        } else {
            return "redirect:/";
        }
    }

    public Map<Integer, Integer> getProductSales() throws SQLException {

        Connection conn = DriverManager.getConnection(db_url,db_username,db_password);
        Map<Integer, Integer> productSales = new HashMap<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT product_id, SUM(quantity) as Sales from Transactions GROUP BY product_id;");
            while (rs.next()) {
                int productID = rs.getInt("product_id");
                int sales = rs.getInt("Sales");
                productSales.put(productID, sales);
            }
        }

        conn.close();

        return productSales;
    }

    public List<Integer> getMostSellingProductIds() throws SQLException {
        Connection conn = DriverManager.getConnection(db_url,db_username,db_password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT t.product_id, SUM(t.quantity) as total_quantity from Transactions t JOIN Discounts d ON t.product_id = d.product_id GROUP BY t.product_id ORDER BY total_quantity DESC;");

        List<Integer> result = new ArrayList<>();
        int maxQuantity = 0;

        while (rs.next()) {
            int quantity = rs.getInt("total_quantity");
            int productId = rs.getInt("product_id");

            if (quantity > maxQuantity) {
                maxQuantity = quantity;
                result.clear();
                result.add(productId);
            } else if (quantity == maxQuantity) {
                result.add(productId);
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        System.out.println("Most selling products: " + result);

        return result;
    }


    /*New Trial for Recommendation*/
    @GetMapping("/user/bestdeals")
    public String getBestDeals(Model model, HttpSession session) {

        if (session.getAttribute("loggedInUser") != null) {
            if (userService.isCustomer(session)) {
                String userIdStr = (String) session.getAttribute("loggedInUser");
                int userId = Integer.parseInt(userIdStr);

                Map<Integer, Double> bestDealsScore = new HashMap<>();

                try {
                    //Getting products with discounts
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

                        //Calculated the score of product based on the discount offered
                        bestDealsScore.put(productId, percentSavings * score.get("discountScore"));
                    }

                    List<Product> discountProducts = new ArrayList<>();

                    //Getting favorited items
                    List<Integer> favourites = new ArrayList<>();
                    String SQLquery = "SELECT product_id FROM favorites WHERE user_id = ? AND favorited_at IS NOT NULL;";
                    PreparedStatement stmt2 = con.prepareStatement(SQLquery);
                    stmt2.setInt(1, userId);
                    ResultSet rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        Integer product_id = rs2.getInt("product_id");
                        favourites.add(product_id);

                        //Updating Score Based on Favourite items
                        bestDealsScore.computeIfPresent(product_id, (key, val) -> val + score.get("favouriteScore"));
                    }

                    //Getting personal purchase score
                    SQLquery = "SELECT product_id, COUNT(product_id)/(SELECT COUNT(product_id) FROM Transactions WHERE user_id = 1?) percentage FROM Transactions WHERE user_id = 2? GROUP BY product_id;";
                    stmt2 = con.prepareStatement(SQLquery);
                    stmt2.setInt(1, userId);
                    stmt2.setInt(2, userId);
                    rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        Integer product_id = rs2.getInt("product_id");
                        Double percentage = rs2.getDouble("percentage");

                        bestDealsScore.computeIfPresent(product_id, (key, val) -> val + score.get("personalSalesScore") * percentage);
                    }

                    //Getting Global purchase score
                    SQLquery = "SELECT product_id, COUNT(product_id)/(SELECT COUNT(product_id) FROM Transactions) percentage FROM Transactions GROUP BY product_id;";
                    stmt2 = con.prepareStatement(SQLquery);
                    rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        Integer product_id = rs2.getInt("product_id");
                        Double percentage = rs2.getDouble("percentage");
                        bestDealsScore.computeIfPresent(product_id, (key, val) -> val + score.get("globalSalesScore") * percentage);
                    }

                    /*
                    If succeeded; then search information could be included
                    */

                    //Getting most searched categories from searchPattern
                    String most_searched_category = "SELECT category_id FROM Search_Patterns WHERE user_id = ? AND category_id IS NOT NULL GROUP BY category_id ORDER BY COUNT(*) DESC LIMIT 1;\n";
                    PreparedStatement most_searched_category_stmt = con.prepareStatement(most_searched_category);
                    most_searched_category_stmt.setInt(1, userId);
                    ResultSet most_searched_category_rs = most_searched_category_stmt.executeQuery();
                    Category mostSearchedCategory = new Category();
                    while (most_searched_category_rs.next()) {
                        System.out.println("Searched id: " + most_searched_category_rs.getInt("category_id"));
                        mostSearchedCategory.setId(most_searched_category_rs.getInt("category_id"));
                        String getCategoryName = "SELECT category_name FROM Cuisine_Categories WHERE category_id = ?";
                        PreparedStatement getCategoryName_stmt = con.prepareStatement(getCategoryName);
                        getCategoryName_stmt.setInt(1, mostSearchedCategory.getId());
                        ResultSet mostSearchedCategory_rs = getCategoryName_stmt.executeQuery();
                        while (mostSearchedCategory_rs.next()) {
                            mostSearchedCategory.setName(mostSearchedCategory_rs.getString("category_name"));
                        }
                    }

                    System.out.println("Top category: " + mostSearchedCategory.getName());
                    Map<Integer, Integer> sales = getProductSales();

                    List<Integer> bestSellers = getMostSellingProductIds();

                    //Constructing products in Order
                    //Retrieving information of discounted products
                    for (int discountId : discountIds) {
                        String query2 = "SELECT product_id, restaurant_ID, product_name, image, price, product_description, category_id FROM Products WHERE product_id = ?;";
                        stmt2 = con.prepareStatement(query2);
                        stmt2.setInt(1, discountId);
                        rs2 = stmt2.executeQuery();
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

                    for (Product item : discountProducts) {
                        System.out.println(item.toString());
                    }

                    for (int i = 1; i < discountProducts.size(); i++) {
                        Double value = bestDealsScore.get(discountProducts.get(i).getId());
                        Product item = discountProducts.get(i);
                        int j = i - 1;
                        while (j >= 0 && bestDealsScore.get(discountProducts.get(j).getId()) < value) {
                            discountProducts.set(j + 1, discountProducts.get(j));
                            j = j - 1;
                        }
                        discountProducts.set(j + 1, item);
                    }

                    for (Product item : discountProducts) {
                        System.out.println(item.toString() + "score = " + bestDealsScore.get(item.getId()));
                    }

                    model.addAttribute("discounts", discounts);
                    model.addAttribute("sales", sales);
                    model.addAttribute("topCategory", mostSearchedCategory);
                    model.addAttribute("favourites", favourites);
                    model.addAttribute("discountIds", discountIds);
                    model.addAttribute("deals", discountProducts);
                    model.addAttribute("basketService", basketService);
                    model.addAttribute("bestSellers", bestSellers);
                    return "bestDeals";

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return "redirect:/index";
            }
        } else {
            return "redirect:/index";
        }

    }

}
