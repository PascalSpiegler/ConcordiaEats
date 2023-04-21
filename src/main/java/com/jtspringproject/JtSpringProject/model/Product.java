package com.jtspringproject.JtSpringProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table
public class Product {
    public Product(){}

    public Product(String name, int restaurant_ID){
        this.name = name;
        this.restaurant_ID = restaurant_ID;
    }

    @Id
    @JsonProperty("objectID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int product_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "restaurant_ID", nullable = false)
    private int restaurant_ID;

    @Column(name = "image", nullable = false)
    private String image;


    @Column(name = "description")
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "category_id", nullable = false)
    private int category_id;

    public String categoryName;




    public int getId(){
        return product_id;
    }

    public void setId(int product_id) {
        this.product_id = product_id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getRestaurantId() {return restaurant_ID;}

    public void setRestaurant_id(int restaurant_ID) {this.restaurant_ID = restaurant_ID;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    @Column(name = "category_id")
    public int getCategoryId(){
        return category_id;
    }

    public void setCategoryId(int category_id){
        this.category_id = category_id;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return product_id == product.product_id;
    }

    //  Overriding the equals and hashCode methods of the Product class so that two products with the same id are considered equal, regardless of their memory location.
    @Override
    public int hashCode() {
        return Objects.hash(product_id);
    }
    @Override
    public String toString(){
        return "Product{" + "id: " + product_id + ", name: " + name + ", price: " + price + "}";
    }
}
