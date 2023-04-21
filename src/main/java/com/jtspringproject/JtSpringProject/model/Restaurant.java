package com.jtspringproject.JtSpringProject.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Restaurant {
    public Restaurant(){}

    public Restaurant(int restaurant_id, String restaurant_name, String email, String address, String phone, Date openingTime, Date closingTime){
        this.restaurant_id = restaurant_id;
        this.restaurant_name = restaurant_name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private int restaurant_id;

    @Column(name = "restaurant_name", nullable = false)
    private String restaurant_name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "openingTime", nullable = false)
    private Date openingTime;

    @Column(name = "closingTime", nullable = false)
    private Date closingTime;




    // Getters and setters
    public int getId() {
        return restaurant_id;
    }

    public void setId(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return restaurant_name;
    }

    public void setName(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getPhone(){
       return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public Date getOpeningTime(){
        return openingTime;
    }

    public void setOpeningTime(Date openingTime){
        this.openingTime = openingTime;
    }

    public Date getClosingTime(){
        return closingTime;
    }

    public void setClosingTime(Date closingTime){
        this.closingTime = closingTime;
    }

    public String toString() {
        return "Restaurant{" +
                "restaurant_id=" + restaurant_id +
                ", restaurant_name='" + restaurant_name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
