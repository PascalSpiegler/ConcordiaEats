package com.jtspringproject.JtSpringProject.service;

import com.jtspringproject.JtSpringProject.model.User;
import com.jtspringproject.JtSpringProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(int id){
        return userRepository.findById(id);
    }


    public boolean isCustomer(HttpSession session){
        String userIdStr = (String) session.getAttribute("loggedInUser");
        int userId = Integer.parseInt(userIdStr);
        User user = findById(userId);
        if ((user.getRole()).equals("customer")){
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdmin(HttpSession session){
        String userIdStr = (String) session.getAttribute("loggedInUser");
        int userId = Integer.parseInt(userIdStr);
        User user = findById(userId);
        if (user.getRole().equals("admin")){
            return true;
        } else {
            return false;
        }
    }

}
