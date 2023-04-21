package com.jtspringproject.JtSpringProject.model;

import javax.persistence.*;



@Entity
@Table
public class Category {

    public Category(){}

    public Category(int category_id, String category_name){
        this.category_id = category_id;
        this.category_name = category_name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int category_id;

    @Column(name = "category_name", nullable = false)
    private String category_name;

    // Getters and setters
    public int getId() {
        return category_id;
    }

    public void setId(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return category_name;
    }

    public void setName(String category_name) {
        this.category_name = category_name;
    }
}

