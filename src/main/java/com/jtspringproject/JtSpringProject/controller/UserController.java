package com.jtspringproject.JtSpringProject.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController{

	@Value("${spring.datasource.username}")
	String db_username;

	@Value("${spring.datasource.password}")
	String db_password;

	@Value("${spring.datasource.url}")
	String db_url;

	@GetMapping("/register")
	public String registerUser()
	{
		return "register";
	}

	@GetMapping("/contact")
	public String contact()
	{
		return "contact";
	}

	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public String newUseRegister(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email)
	{
		try
		{
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			PreparedStatement pst = con.prepareStatement("insert into Users(username,password,email) values(?,?,?);");
			pst.setString(1,username);
			pst.setString(2, password);
			pst.setString(3, email);
			

			//pst.setString(4, address);
			int i = pst.executeUpdate();
			System.out.println("data base updated"+i);
			
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:/";
	}
}
