package com.jtspringproject.JtSpringProject.model;

import javax.persistence.*;

@Entity
@Table
public class User {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Integer user_id;
    private String username;
    private String password;
    @Column(name = "customer_name")
    private String customer_name;
    private String email;
    private String phone;
    private String address;
    private String user_role;
    private Boolean enabled;

    public User(Integer user_id, String username, String password, String customer_name, String email, String phone, String address, String user_role, Boolean enabled) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.customer_name = customer_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.user_role = user_role;
        this.enabled = enabled;
    }

    public User() {
    }

    public Integer getId() {
        return user_id;
    }

    public void setId(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomerName() {
        return customer_name;
    }

    public void setCustomerName(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return user_role;
    }

    public void setRole(String user_role) {
        this.user_role = user_role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", user_role=" + user_role +
                ", enabled=" + enabled +
                '}';
    }
}


