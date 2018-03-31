package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Brand;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.Product;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.adminRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.storeRequestRepository;
import com.example.demo.repositories.userRepository;

@Controller
public class adminController {

		@Autowired
		private adminRepository adminRepo;
		
		@Autowired
		private productRepository productRepo;
		
		@Autowired
		private storeRequestRepository storeRequestRepo;
		
		@Autowired
		private onlineStoreRepository onlineStoreRepo;
		
		@GetMapping("/loginAdmin")
		public String showForm(Model model) {
			model.addAttribute("admin",new Admin());
			return "loginAdmin";
		}
		
		
		@PostMapping("/loginAdmin")
		public String LoginUser(Model model ,@ModelAttribute Admin admin,HttpServletRequest request) {
			
			if(adminRepo.findByUsernameAndPassword(admin.getUsername(), admin.getPassword())!=null) {
				model.addAttribute("product",new Product());
				model.addAttribute("brand",new Brand());
				model.addAttribute("storerequest",storeRequestRepo.findAll());

				return "adminHome";
			}
			else {
				model.addAttribute("admin",new Admin());
				return"loginAdmin";
			}
			
		}
		
		
		@PostMapping("/acceptStore")
		public String acceptStore( Model model,@RequestParam(name = "requestid") int requestid) {
			
			StoreRequests store=storeRequestRepo.findById(requestid);
			OnlineStore onstore=new OnlineStore();
			onstore.setStore_name(store.getStore_name());
			onstore.setStore_location(store.getStore_location());
			onstore.setStore_type(store.getStore_type());
			onlineStoreRepo.save(onstore);
			storeRequestRepo.delete(store);
			model.addAttribute("brand",new Brand());
			model.addAttribute("product",new Product());
			model.addAttribute("storerequest",storeRequestRepo.findAll());
			return "adminHome";
		}

}
