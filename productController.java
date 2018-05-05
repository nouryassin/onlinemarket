package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entities.Admin;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Product;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.adminRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.storeRequestRepository;
import com.example.demo.repositories.userRepository;

@Controller
public class productController {/** for saving product */
	@Autowired
	private productRepository productRepo;
	
	@Autowired 
	private storeRequestRepository storeRequestRepo;

	@PostMapping("/adminHome") /** saves product to specific store */
	public String AddProduct(Model model,@ModelAttribute Product product) {
		System.out.println(product.getHigh_price());
		if(product.getHigh_price()<0||product.getLow_price()<0) {
			return "error";
		}
		else{
			productRepo.save(product);
			model.addAttribute("product",new Product());
			model.addAttribute("brand",new Brand());
			model.addAttribute("storerequest",storeRequestRepo.findAll());
			return "adminHome";
		}
	}
	

}
