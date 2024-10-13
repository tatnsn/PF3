package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.constant.UlrConst;

@Controller
public class MenuController {
	
	@GetMapping(UlrConst.MENU)
	public String view() {
		
		return"menu";
	}
	

}
