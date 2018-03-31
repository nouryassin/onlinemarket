package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
	@GetMapping("/onlineMarket")
	public String showlogin() {
		return "/onlineMarket";
	}
}
