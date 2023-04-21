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

import java.sql.*;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Value("${spring.datasource.username}")
    String db_username;

    @Value("${spring.datasource.password}")
    String db_password;

    @Value("${spring.datasource.url}")
    String db_url;

    private final ProductService productService;
    private final BasketService basketService;

    private final UserService userService;

    public ProductController(ProductService productService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @RequestMapping("/products")
    public String redirectProducts(ModelMap model, HttpSession session) {
        if (userService.isCustomer(session)){
            return "redirect:/user/products";
        } else if (userService.isAdmin(session)) {
            System.out.println("User is an admin.");
            return "redirect:/admin/products";
        } else {
            return "redirect:/";
        }

    }

//    CUSTOMER PRODUCTS
    @RequestMapping("/user/products")
    public String products(ModelMap model, HttpSession session) throws SQLException {

        //We only want users who are logged in to see the product page
        if (session.getAttribute("loggedInUser") != null){
            String userIdStr = (String) session.getAttribute("loggedInUser");
            int userId = Integer.parseInt(userIdStr);


            Connection con = DriverManager.getConnection(db_url,db_username,db_password);
            List<Integer> favorites = new ArrayList<>();
            String query = "SELECT product_id FROM favorites WHERE user_id = ? AND favorited_at IS NOT NULL;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favorites.add(rs.getInt("product_id"));
            }

            System.out.println("Favorites: " + favorites);

            List<Object[]> products = productService.findAllProducts();
            productService.loadProductsIntoRepository(products);

            List<Map.Entry<Integer, Integer>> basketItems = new ArrayList<>();
            Map<Product, Integer> basketObject = basketService.getProductsInBasket();

            for (Map.Entry<Product, Integer> entry : basketObject.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                basketItems.add(Map.entry(product.getId(), quantity));
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

            model.addAttribute("products", products);
            model.addAttribute("favourites", favorites);
            model.addAttribute("discounts", discounts);
            model.addAttribute("discountIds", discountIds);
            model.addAttribute("basketService", basketService);
            if (userService.isCustomer(session)){
                System.out.println("User is a customer.");
                return "customerProducts";
            } else if (userService.isAdmin(session)) {
                System.out.println("User is an admin.");
                return "redirect:/admin/products";
            } else {
                return "redirect:/";
            }

        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/admin/products")
    public String getproduct(Model model, HttpSession session) throws SQLException {
        if (session.getAttribute("loggedInUser") != null){

            List<Object[]> products = productService.findAllProducts();
            productService.loadProductsIntoRepository(products);

            List<Integer> mostSelling = getMostSellingProductIds();
            List<Integer> leastSelling = getLeastSellingProductIds();
            Map<Integer,Integer> sales = getProductSales();

            model.addAttribute("products", products);
            model.addAttribute("mostSelling", mostSelling);
            model.addAttribute("leastSelling", leastSelling);
            model.addAttribute("sales", sales);

            return "adminProducts";

        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/admin/products/add")
    public String addproduct(Model model) {
        //Querying for the categories
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("select category_id, category_name from Cuisine_Categories");
            ResultSet rs = pst.executeQuery();

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setName(rs.getString("category_name"));
                category.setId(rs.getInt("category_id"));
                categories.add(category);
            }
            model.addAttribute("categories", categories);
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }

        //Querying for the restaurants
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("select restaurant_id, restaurant_name from Restaurants");
            ResultSet rs = pst.executeQuery();

            List<Restaurant> restaurants = new ArrayList<>();
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(rs.getString("restaurant_name"));
                restaurant.setId(rs.getInt("restaurant_id"));
                restaurants.add(restaurant);
            }
            model.addAttribute("restaurants", restaurants);
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }
        return "productsAdd";
    }

    @GetMapping("/admin/products/update")
    public String updateproduct(@RequestParam("pid") int id,Model model) {

//        Querying for the desired product
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

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
            model.addAttribute("product", product);

        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }

        //Querying for the categories
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("select category_id, category_name from Cuisine_Categories");
            ResultSet rs = pst.executeQuery();

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setName(rs.getString("category_name"));
                category.setId(rs.getInt("category_id"));
                categories.add(category);
            }
            model.addAttribute("categories", categories);
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }

        //Querying for the restaurants
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("select restaurant_id, restaurant_name from Restaurants");
            ResultSet rs = pst.executeQuery();

            List<Restaurant> restaurants = new ArrayList<>();
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(rs.getString("restaurant_name"));
                restaurant.setId(rs.getInt("restaurant_id"));
                restaurants.add(restaurant);
            }
            model.addAttribute("restaurants", restaurants);
        }
        catch(Exception e)
        {
            System.out.println("!!!!!!!!!!!!!!!!!Exception:"+e);
        }


        return "productsUpdate";
    }
    @RequestMapping(value = "/admin/products/updateData",method=RequestMethod.POST)
    public String updateproducttodb(@RequestParam("id") int id, @RequestParam("name") String name, @RequestParam("restaurantId") int restaurantId, @RequestParam("categoryId") int categoryId, @RequestParam("price") double price, @RequestParam("description") String description, @RequestParam("image") String image)

    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("update Products set product_name= ?, restaurant_ID = ?, category_id = ?, price = ?, product_description = ?, image = ? WHERE product_id = ?;");
            pst.setString(1, name);
            pst.setInt(2, restaurantId);
            pst.setInt(3, categoryId);
            pst.setDouble(4, price);
            pst.setString(5, description);
            pst.setString(6, image);
            pst.setInt(7, id);
            int i = pst.executeUpdate();
            System.out.println("Executed " + i);
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }


        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete")
    public String removeProductDb(@RequestParam("id") int id)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);


            PreparedStatement pst = con.prepareStatement("delete from Products where product_id = ? ;");
            pst.setInt(1, id);
            int i = pst.executeUpdate();

        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products")
    public String postproduct() {
        return "redirect:/admin/categories";
    }
    @RequestMapping(value = "admin/products/sendData", method = RequestMethod.POST)
    public String addproducttodb(@RequestParam("name") String name, @RequestParam("restaurantId") int restaurantId,
                                 @RequestParam("categoryId") int categoryId, @RequestParam("price") double price,
                                 @RequestParam("description") String description, @RequestParam("image") String image) {
        try {
            Connection con = DriverManager.getConnection(db_url,db_username,db_password);

            PreparedStatement pst = con.prepareStatement("INSERT INTO Products (product_name, restaurant_ID, category_ID, price, product_description, image) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setString(1, name);
            pst.setInt(2, restaurantId);
            pst.setInt(3, categoryId);
            pst.setDouble(4, price);
            pst.setString(5, description);
            pst.setString(6, image);
            int i = pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return "redirect:/admin/products";
    }

    public List<Integer> getMostSellingProductIds() throws SQLException {
        Connection conn = DriverManager.getConnection(db_url,db_username,db_password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT product_id, SUM(quantity) as total_quantity from Transactions GROUP BY product_id ORDER BY total_quantity DESC;");

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

    public List<Integer> getLeastSellingProductIds() throws SQLException {
        Connection conn = DriverManager.getConnection(db_url,db_username,db_password);
        List<Integer> leastSellingProducts = new ArrayList<>();

        // Get the list of all product IDs
        Set<Integer> allProductIDs = new HashSet<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT product_id FROM Products;");
            while (rs.next()) {
                allProductIDs.add(rs.getInt("product_id"));
            }
        }

        // Get the list of sold product IDs
        Map<Integer, Integer> soldProductCounts = new HashMap<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT product_id, SUM(quantity) as Sales FROM Transactions GROUP BY product_id;\n");
            while (rs.next()) {
                int productID = rs.getInt("product_id");
                int sales = rs.getInt("Sales");
                soldProductCounts.put(productID, sales);
                allProductIDs.remove(productID);
            }
        }

        // If there are any unsold products, add them to the list
        for (int productID : allProductIDs) {
            if (!soldProductCounts.containsKey(productID)) {
                leastSellingProducts.add(productID);
            }
        }


        // If all products have been sold, find the ones with the least sales
        if (leastSellingProducts.isEmpty()) {
            int minSales;
            if (soldProductCounts.isEmpty()) {
                minSales = 0; // no sales of any product
            } else {
                minSales = Integer.MAX_VALUE; // at least one product has sales
            }
            for (int sales : soldProductCounts.values()) {
                if (sales < minSales) {
                    minSales = sales;
                }
            }
            for (Map.Entry<Integer, Integer> entry : soldProductCounts.entrySet()) {
                if (entry.getValue() == minSales) {
                    leastSellingProducts.add(entry.getKey());
                }
            }
        }
        conn.close();
        System.out.println("Least selling products " + leastSellingProducts);

        return leastSellingProducts;

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

}