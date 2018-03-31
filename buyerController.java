package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.SoldOutProduct;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productStoreRepository;
import com.example.demo.repositories.soldOutProductsRepository;

@Controller
public class buyerController {
	
	@Autowired
	onlineStoreRepository onlineStoreRepo;
	
	@Autowired
	productStoreRepository productStoreRepo;
	
	@Autowired
	soldOutProductsRepository soldoutProductRepo; 
	
	ProductStore productstore;
	
	@GetMapping("/buyerHome")
	public String showBuyerHome(Model model) {
		List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
		model.addAttribute("onlinestore",stores);
		return "buyerHome";
	}
	
	@PostMapping("/buyerHome")
	public String showBuyerHome(Model model,@RequestParam(name = "productid") int productid ) {
		
		List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
		model.addAttribute("onlinestore",stores);
		productstore=productStoreRepo.findById(productid);
		productstore.setViews(productstore.getViews()+1);
		productStoreRepo.save(productstore);
		model.addAttribute("productstore",productstore);
		return "viewProduct";
	}
	
	@PostMapping("/viewProduct")
	public String buyProduct(Model model,@RequestParam(name = "quant") int quant) {
		
		if(productstore.getQuantity()-quant>1) {
			productstore.setQuantity(productstore.getQuantity()-quant);
			productstore.setBuyers(productstore.getBuyers()+1);
			productStoreRepo.save(productstore);

		}
		else if(productstore.getQuantity()-quant==0){
			productStoreRepo.delete(productstore);
			productstore.setBuyers(productstore.getBuyers()+1);
			SoldOutProduct sold=new SoldOutProduct();
			sold.setProduct_name(productstore.getProduct_name());
			sold.setStore_id(productstore.getStores().getId());
			sold.setStore_name(productstore.getStore_name());
			sold.setViews(productstore.getViews());
			sold.setBuyers(productstore.getBuyers());
			soldoutProductRepo.save(sold);
		}
		
		List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
		model.addAttribute("onlinestore",stores);
		return "buyerHome";
	}
	
	
}
