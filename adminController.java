
package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Admin;
import com.example.demo.entities.AdminStatsitics;
import com.example.demo.entities.Brand;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminStatRepository;
import com.example.demo.repositories.adminRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.productStoreRepository;
import com.example.demo.repositories.sesionRepository;
import com.example.demo.repositories.storeRequestRepository;
import com.example.demo.repositories.userRepository;

@Controller
public class adminController { /** controller of admin entity */

		@Autowired
		private sesionRepository sesionRepo;
		@Autowired
		private adminRepository adminRepo;
		
		@Autowired
		private userRepository userRepo;
	
		@Autowired
		private productRepository productRepo;
		
		@Autowired
		private storeRequestRepository storeRequestRepo;
		
		@Autowired
		private onlineStoreRepository onlineStoreRepo;
		
		@Autowired
		private productStoreRepository productStoreRepo;
		
		@Autowired
		private AdminStatRepository adminstatRepo;
		
		@GetMapping("/loginAdmin")            /** showing login admin page */
		public String showForm(Model model) {
			model.addAttribute("admin",new Admin());
			return "loginAdmin";
		}
		
		@PostMapping("/loginAdmin") /** login as admin method */
		public String LoginUser(Model model ,@ModelAttribute Admin admin,HttpServletRequest request) {
			
			if(adminRepo.findByUsernameAndPassword(admin.getUsername(), admin.getPassword())!=null) {
				model.addAttribute("product",new Product());
				model.addAttribute("brand",new Brand());
				model.addAttribute("storerequest",storeRequestRepo.findAll());
				model.addAttribute("AdminStatsitics",new AdminStatsitics());
				return "adminHome";
			}
			else {
				model.addAttribute("admin",new Admin());
				return"loginAdmin";
			}
			
		}
		
		
		@PostMapping("/acceptStore")/** admin accept store requests made by storeowner */
		public String acceptStore( Model model,@RequestParam(name = "requestid") int requestid,HttpServletRequest request) {
			int id=sesionRepo.findById(1).getId();
			StoreRequests store=storeRequestRepo.findById(requestid);
			OnlineStore onstore=new OnlineStore();
			onstore.setStore_name(store.getStore_name());
			onstore.setStore_location(store.getStore_location());
			onstore.setStore_type(store.getStore_type());
			onstore.setStoreowner(userRepo.findById(id));
			onlineStoreRepo.save(onstore);
			storeRequestRepo.delete(store);
			userRepo.findById(id).getStores().add(onstore);
			model.addAttribute("brand",new Brand());
			model.addAttribute("product",new Product());
			model.addAttribute("storerequest",storeRequestRepo.findAll());
			model.addAttribute("AdminStatsitics",new AdminStatsitics());

			return "adminHome";
		}
		
		@PostMapping("/addStat")/** admin add statistics to store owner */
		public String addStatToStoreOwner(Model model,@RequestParam(name = "type") String type) {
			List<OnlineStore>onlineStore=(List<OnlineStore>) onlineStoreRepo.findAll();;
			AdminStatsitics adminstat=new AdminStatsitics();
			if(type.compareTo("MaximumBoughtProduct")==0) {
				for(OnlineStore on:onlineStore) {
					if(on.getStoreproducts().size()>0) {
						ProductStore maxProduct=on.getStoreproducts().get(0);
						for(ProductStore products : on.getStoreproducts())
						{
							if(maxProduct.getBuyers()<products.getBuyers()) {
									maxProduct=products;
								}
							adminstat.setProductid(maxProduct.getId());
							adminstat.setStoreid(maxProduct.getStores().getId());
							adminstat.setProductName(maxProduct.getProduct_name());
							adminstat.setType(type);
							adminstatRepo.save(adminstat);
						}
					}
				}
			}
			if(type.compareTo("MaximumViewedProduct")==0) {
				for(OnlineStore on:onlineStore) {
					if(on.getStoreproducts().size()>0) {
						ProductStore maxProduct=on.getStoreproducts().get(0);
						for(ProductStore products : on.getStoreproducts())
						{
							if(maxProduct.getViews()<products.getViews()) {
									maxProduct=products;
								}
							adminstat.setProductid(maxProduct.getId());
							adminstat.setStoreid(maxProduct.getStores().getId());
							adminstat.setProductName(maxProduct.getProduct_name());
							adminstat.setType(type);
							adminstatRepo.save(adminstat);
						}
					}
				}
			}
			if(type.compareTo("MinimumViewedProduct")==0) {
				for(OnlineStore on:onlineStore) {
					if(on.getStoreproducts().size()>0) {
						ProductStore miniProduct=on.getStoreproducts().get(0);
						for(ProductStore products : on.getStoreproducts())
						{
							if(miniProduct.getViews()>products.getViews()) {
									miniProduct=products;
								}
							adminstat.setProductid(miniProduct.getId());
							adminstat.setStoreid(miniProduct.getStores().getId());
							adminstat.setProductName(miniProduct.getProduct_name());
							adminstat.setType(type);
							adminstatRepo.save(adminstat);
						}
					}
				}
			}
			if(type.compareTo("MinimumBoughtProduct")==0) {
				for(OnlineStore on:onlineStore) {
					if(on.getStoreproducts().size()>0) {
						ProductStore miniProduct=on.getStoreproducts().get(0);
						for(ProductStore products : on.getStoreproducts())
						{
							if(miniProduct.getBuyers()>products.getBuyers()) {
									miniProduct=products;
								}
							adminstat.setProductid(miniProduct.getId());
							adminstat.setStoreid(miniProduct.getStores().getId());
							adminstat.setType(type);
							adminstat.setProductName(miniProduct.getProduct_name());
							adminstatRepo.save(adminstat);
						}
					}
				}
			}
			
			if(type.compareTo("TotalBuyingTimes")==0) {
				int count=0;
				for(OnlineStore on:onlineStore) {
					for(ProductStore products : on.getStoreproducts())
					{
						count+=products.getBuyers();
					}
					adminstat.setProductid(count);
					adminstat.setStoreid(on.getId());
					adminstat.setType(type);
					adminstatRepo.save(adminstat);
				}
				
			}
			if(type.compareTo("AverageBuyingTimes")==0) {
				int count=0;
				for(OnlineStore on:onlineStore) {
					for(ProductStore products : on.getStoreproducts())
					{
						count+=products.getBuyers();
					}
					count/=on.getStoreproducts().size();
					adminstat.setProductid(count);
					adminstat.setStoreid(on.getId());
					adminstat.setType(type);
					adminstatRepo.save(adminstat);
				}
				
			}
			
			model.addAttribute("brand",new Brand());
			model.addAttribute("product",new Product());
			model.addAttribute("storerequest",storeRequestRepo.findAll());
			model.addAttribute("AdminStatsitics",new AdminStatsitics());
			return "adminHome";
			
		}

}
