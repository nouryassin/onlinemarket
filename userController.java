package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.productStoreRepository;
import com.example.demo.repositories.soldOutProductsRepository;
import com.example.demo.repositories.userRepository;

@Controller
public class userController {
	
	@Autowired
	private userRepository userRepo;
	@Autowired
	onlineStoreRepository onlineStoreRepo;
	@Autowired
	BrandRepository brandRepo;
	@Autowired
	soldOutProductsRepository soldoutRepo;
	@Autowired
	productStoreRepository productStoreRepo;
	@Autowired
	productRepository productRepo;
	
	
	@GetMapping("/addUser")
	public String showForm(Model model) {
		model.addAttribute("user",new User());
		return "addUser";
	}
	
	@PostMapping("/addUser")
	public String addUser(Model model,@ModelAttribute User user) {
		
		userRepo.save(user);
		model.addAttribute("user",new User());
		return "login";
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user",new User());
		return "login";
	}
	
	@PostMapping("/login")
	public String LoginUser(Model model,@ModelAttribute User user,HttpServletRequest request,@Valid @ModelAttribute("productstore") ProductStore productstore,@Valid @ModelAttribute("storerequest") StoreRequests storerequest) {
		User user2=userRepo.findByUsername(user.getUsername());
		if(userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword())!=null) {
			
			if(user2.getType().compareTo("storeowner")==0) {
				model.addAttribute("storerequest",new StoreRequests());
				model.addAttribute("productstore",new ProductStore());
				model.addAttribute("Product",productRepo.findAll());
				model.addAttribute("onlinestore",onlineStoreRepo.findAll());
				model.addAttribute("Brand",brandRepo.findAll());
				model.addAttribute("soldout",soldoutRepo.findAll());
				model.addAttribute("product",productStoreRepo.findAll());
				
				return "storeownerHome";
				}
			else if(user2.getType().compareTo("buyer")==0)  {
				List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
				model.addAttribute("onlinestore",stores);
				return "buyerHome";
			}
			else {
				model.addAttribute("user",new User());
				return "login";
			}
		}

		
		
		else {
			model.addAttribute("user",new User());
			return"login";
		}
		
	}
	
}
