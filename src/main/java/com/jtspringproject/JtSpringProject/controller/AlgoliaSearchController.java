package com.jtspringproject.JtSpringProject.controller;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.settings.IndexSettings;
import com.jtspringproject.JtSpringProject.model.Category;
import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.service.BasketService;
import com.jtspringproject.JtSpringProject.service.ProductService;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AlgoliaSearchController {

    private final ProductService productService;
    private final BasketService basketService;

    private final UserService userService;



    @Value("${spring.datasource.username}")
    String db_username;

    @Value("${spring.datasource.password}")
    String db_password;

    @Value("${spring.datasource.url}")
    String db_url;

    @Value("${algolia.api.key}")
    String algolia_api;

    @Value("${algolia.name}")
    String algolia_name;

    public AlgoliaSearchController(ProductService productService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.basketService = basketService;
        this.userService = userService;
    }

    // Create a new Algolia client
    SearchClient client = DefaultSearchClient.create(algolia_name, algolia_api);

    SearchIndex<Product> index = client.initIndex("Products", Product.class);




    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, @RequestParam(name = "category", required = false) String category, Model model, HttpSession session) throws SQLException {
        if (session.getAttribute("loggedInUser") != null) {
            if (userService.isCustomer(session)) {

                IndexSettings settings = new IndexSettings();
                settings.setAttributesForFaceting(Arrays.asList("categoryName"));
                index.setSettings(settings);


                String userIdStr = (String) session.getAttribute("loggedInUser");
                int userId = Integer.parseInt(userIdStr);


                Connection con = DriverManager.getConnection(db_url, db_username, db_password);
                PreparedStatement pst = con.prepareStatement("SELECT * FROM Products");
                ResultSet rs = pst.executeQuery();
                List<Product> products = new ArrayList<>();

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setRestaurant_id(rs.getInt("restaurant_ID"));
                    product.setName(rs.getString("product_name"));
                    product.setImage(rs.getString("image"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setDescription(rs.getString("product_description"));
                    product.setCategoryId(rs.getInt("category_id"));

                    String categoryQuery = "SELECT category_name FROM Cuisine_Categories WHERE category_id = ? ;";
                    PreparedStatement stmt_cat = con.prepareStatement(categoryQuery);
                    stmt_cat.setInt(1, product.getCategoryId());
                    ResultSet rs_cat = stmt_cat.executeQuery();

                    while (rs_cat.next()) {
                        product.setCategoryName(rs_cat.getString("category_name"));
                    }

                    products.add(product);
                }

                index.clearObjects();
                index.saveObjects(products);

                List<Integer> favourites = new ArrayList<>();
                String SQLquery = "SELECT product_id FROM favorites WHERE user_id = ? AND favorited_at IS NOT NULL;";
                PreparedStatement stmt = con.prepareStatement(SQLquery);
                stmt.setInt(1, userId);
                ResultSet rs2 = stmt.executeQuery();

                while (rs2.next()) {
                    favourites.add(rs2.getInt("product_id"));
                }


                List<String> attributesToRetrieve = new ArrayList<>();
                attributesToRetrieve.add("id");
                attributesToRetrieve.add("category_id");
                attributesToRetrieve.add("name");
                attributesToRetrieve.add("image");
                attributesToRetrieve.add("description");
                attributesToRetrieve.add("price");

                Query algoliaQuery = new Query(query)
                        .setAttributesToRetrieve(attributesToRetrieve)
                        .setHitsPerPage(10);

                if (category != null && !category.isEmpty()) {
                    algoliaQuery.setFilters("categoryName:'" + category + "'");
                    List<Product> hits = index.search(algoliaQuery).getHits();
                    model.addAttribute("results", hits);
                } else {
                    List<Product> hits = index.search(algoliaQuery).getHits();
                    model.addAttribute("results", hits);
                }

                PreparedStatement pst3 = con.prepareStatement("SELECT * FROM Cuisine_Categories");
                ResultSet rs3 = pst3.executeQuery();
                List<Category> categories = new ArrayList<>();

                while (rs3.next()) {
                    Category cuisineCategory = new Category();
                    cuisineCategory.setId(rs3.getInt("category_id"));
                    cuisineCategory.setName(rs3.getString("category_name"));
                    categories.add(cuisineCategory);
                }

                //Get category ID from name:
                String findCategoryId = "SELECT category_id FROM Cuisine_Categories WHERE category_name = ?";
                PreparedStatement category_id_stmt = con.prepareStatement(findCategoryId);
                category_id_stmt.setString(1, category);


                ResultSet rs_category_id = category_id_stmt.executeQuery();


                if (category != null && !category.isEmpty()) {
                    int categoryId = 0;
                    while (rs_category_id.next()) {
                        categoryId = rs_category_id.getInt("category_id");
                    }
                    // Storing the search record when category is included:
                    PreparedStatement search_pst = con.prepareStatement("insert into Search_Patterns(user_id, query, category_id) values(?,?,?);");
                    search_pst.setInt(1, userId); //User ID
                    search_pst.setString(2, query); //Product ID
                    search_pst.setInt(3, categoryId); //Quantity
                    int i = search_pst.executeUpdate();
                } else {
                    // Storing the search record when category is null:
                    PreparedStatement search_pst = con.prepareStatement("insert into Search_Patterns(user_id, query, category_id) values(?,?,null);");
                    search_pst.setInt(1, userId); //User ID
                    search_pst.setString(2, query); //Product ID
                    int i = search_pst.executeUpdate();
                }


                model.addAttribute("favourites", favourites);
                model.addAttribute("basketService", basketService);
                model.addAttribute("categories", categories);
                con.close();
                return "search";
            } else {
                return "redirect:/adminhome";
            }
        } else {
            return "redirect:/index";
        }
    }
}
