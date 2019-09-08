package com.gjq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ToController {

	@RequestMapping("/regist")
	public String toRegist(){
		return "regist";
		
	}
	@RequestMapping("/login")
	public String toLogin(){
		return "login";
		
	}
}
