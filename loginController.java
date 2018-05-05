package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {/** shows main online market page */
	@GetMapping("/onlineMarket")
	public String showlogin() {
		 
		return "/onlineMarket";
	}
}
