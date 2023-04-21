package com.jtspringproject.JtSpringProject.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jtspringproject.JtSpringProject.model.Category;
import com.jtspringproject.JtSpringProject.model.User;
import com.jtspringproject.JtSpringProject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;




@Controller
public class AdminController {
	@Value("${spring.datasource.username}")
	String db_username;

	@Value("${spring.datasource.password}")
	String db_password;

	@Value("${spring.datasource.url}")
	String db_url;

	private final UserService userService;

	public int adminlogcheck = 0;
	String usernameforclass = "";

	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = {"/","/logout"})
	public String returnIndex(HttpSession session) {
		adminlogcheck =0;
		usernameforclass = "";
		session.invalidate();
		return "userLogin";
	}


	@GetMapping("/index")
	public String index(Model model, HttpSession session) {
		if(usernameforclass.equalsIgnoreCase(""))
			return "userLogin";
		else {
			model.addAttribute("username", usernameforclass);
			if (userService.isAdmin(session)){
				return "redirect:/adminhome";
			}
			return "index";
		}

	}
	@GetMapping("/userloginvalidate")
	public String userlog(Model model, HttpSession session) {
		session.setAttribute("loggedInUser", null);
		return "userLogin";
	}
	@RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
	public String userlogin( @RequestParam("username") String username, @RequestParam("password") String pass,Model model, HttpSession session) {

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("select * from Users where username = '"+username+"' and password = '"+ pass+"' ;");
			if(rst.next()) {
				usernameforclass = rst.getString("username");
				session.setAttribute("loggedInUser", rst.getString("user_id"));
				System.out.println("The user currently logged in has ID = " + session.getAttribute("loggedInUser"));
				if (userService.isCustomer(session)) {
					return "redirect:/index";
				} else if (userService.isAdmin(session)) {
					return "redirect:/adminhome";
				} else {
					return "userLogin";
				}
			} else {
				model.addAttribute("message", "Invalid Username or Password");
				return "userLogin";
			}

		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "userLogin";



	}

	
	@GetMapping("/admin")
	public String adminlogin(Model model) {
		
		return "adminlogin";
	}
	@GetMapping("/adminhome")
	public String adminHome(Model model) {
		if(adminlogcheck!=0)
			return "adminHome";
		else
			return "redirect:/admin";
	}
	@GetMapping("/loginvalidate")
	public String adminlog(Model model) {
		
		return "adminlogin";
	}
	@RequestMapping(value = "loginvalidate", method = RequestMethod.POST)
	public String adminlogin( @RequestParam("username") String username, @RequestParam("password") String pass,Model model) {
		
		if(username.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("123")) {
			adminlogcheck=1;
			return "redirect:/adminhome";
			}
		else {
			model.addAttribute("message", "Invalid Username or Password");
			return "adminlogin";
		}
	}
	@GetMapping("/admin/categories")
	public String getcategory(Model model) {
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

		return "categories";
	}
	@RequestMapping(value = "admin/sendcategory",method = RequestMethod.GET)
	public String addcategorytodb(@RequestParam("categoryname") String catname)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			
			PreparedStatement pst = con.prepareStatement("insert into Cuisine_Categories(category_name) values(?);");
			pst.setString(1,catname);
			int i = pst.executeUpdate();
			
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/delete")
	public String removeCategoryDb(@RequestParam("id") int id)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			
			PreparedStatement pst = con.prepareStatement("delete from Cuisine_Categories where category_id = ? ;");
			pst.setInt(1, id);
			int i = pst.executeUpdate();
			
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/update")
	public String updateCategoryDb(@RequestParam("categoryid") int id, @RequestParam("categoryname") String categoryname)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			
			PreparedStatement pst = con.prepareStatement("update Cuisine_Categories set category_name = ? where category_id = ?");
			pst.setString(1, categoryname);
			pst.setInt(2, id);
			int i = pst.executeUpdate();
			
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:/admin/categories";
	}


	@GetMapping("/admin/customers")
	public String getCustomerDetail(Model model) {
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("select user_id, username, email, address from Users WHERE user_role = 'customer';");

			List<User> users = new ArrayList<>();

			while (rst.next()) {
				User user = new User();
				user.setId(rst.getInt("user_id"));
				user.setUsername(rst.getString("username"));
				user.setEmail(rst.getString("email"));
				user.setAddress(rst.getString("address"));
				users.add(user);
			}
			model.addAttribute("users", users);
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "displayCustomers";
	}
	
	
	@GetMapping("profileDisplay")
	public String profileDisplay(Model model) {
		String displayusername,displaypassword,displayemail,displayaddress;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("select * from Users where username = '"+usernameforclass+"';");
			System.out.println(rst.toString());
			
			if(rst.next())
			{
			int userid = rst.getInt("user_id");
			displayusername = rst.getString("username");
			displayemail = rst.getString("email");
			displaypassword = rst.getString("password");
			displayaddress = rst.getString("address");

			model.addAttribute("userid",userid);
			model.addAttribute("username",displayusername);
			model.addAttribute("email",displayemail);
			model.addAttribute("password",displaypassword);
			model.addAttribute("address",displayaddress);
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "updateProfile";
	}
	
	@RequestMapping(value = "updateuser",method=RequestMethod.POST)
	public String updateUserProfile(@RequestParam("userid") int userid,@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("address") String address)
	
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(db_url,db_username,db_password);
			
			PreparedStatement pst = con.prepareStatement("update Users set username= ?,email = ?,password= ?, address= ? where user_id = ?;");
			System.out.println(pst);
			pst.setString(1, username);
			pst.setString(2, email);
			pst.setString(3, password);
			pst.setString(4, address);
			pst.setInt(5, userid);
			int i = pst.executeUpdate();	
			usernameforclass = username;
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:/index";
	}

}
