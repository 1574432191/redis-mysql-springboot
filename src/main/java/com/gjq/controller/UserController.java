package com.gjq.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gjq.pojo.User;
import com.gjq.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	@Qualifier("uService")
	private UserService userService;
	
	@RequestMapping("/regist")
	public String addUser(HttpServletRequest request){
		try {
			User user = new User();
			user.setName(request.getParameter("name"));
			user.setAge(Integer.parseInt(request.getParameter("age")));
			user.setUsername(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			user.setEmail(request.getParameter("email"));
			user.setPhoneNumber(request.getParameter("phoneNumber"));
			System.out.println("dddd"+user);
			userService.addUsers(user);
			return "添加个人信息成功";
			
		} catch (Exception e) {
			return "添加个人信息错误";
		}

	}
	
	@RequestMapping("/findByUsername")
	public String findByUsername(String username,String password){
		try {
			Map map = userService.findByUsername(username);
			System.out.println(map+"  +  " +map.get("password"));
			String password1 = (String) map.get(password);
		//	System.out.println(password1+"    "+password);
			if (!password.equals(map.get("password"))) {
				return "密码错误";
			}else {
				return "登录成功"; 
			}
		} catch (Exception e) {			
			return "登录异常";
		}
		
		
		
	}

}
