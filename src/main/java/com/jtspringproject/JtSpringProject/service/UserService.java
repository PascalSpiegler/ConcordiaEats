package com.jtspringproject.JtSpringProject.service;

import com.jtspringproject.JtSpringProject.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface UserService {
    User findById(int id);


    boolean isCustomer(HttpSession session);

    boolean isAdmin(HttpSession session);
}
