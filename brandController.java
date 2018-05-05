package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Brand;
import com.example.demo.entities.Product;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.storeRequestRepository;

@Controller
public class brandController {/** controller for brand entity */

	@Autowired
	private BrandRepository brandRepo;

	@Autowired
	storeRequestRepository storeRequestRepo;
	
	@PostMapping("/addBrand")/** saves a new brand to the market */
	public String AddBrand(Model model,@ModelAttribute Brand brand) {
		
		brandRepo.save(brand);
		model.addAttribute("product",new Product());
		model.addAttribute("brand",new Brand());
		model.addAttribute("storerequest",storeRequestRepo.findAll());

		return "adminHome";
	}
	
}
