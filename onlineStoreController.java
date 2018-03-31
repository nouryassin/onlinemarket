package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Brand;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.soldOutProductsRepository;
import com.example.demo.repositories.storeRequestRepository;;

@Controller
public class onlineStoreController {
	
	@Autowired
	onlineStoreRepository onlineStoreRepo;
	@Autowired
	CrudRepository<ProductStore, Integer> productStoreRepo;
	@Autowired
	BrandRepository brandRepo;
    @Autowired
    storeRequestRepository storeRequestRepo;
    @Autowired
    soldOutProductsRepository soldoutRepo;
    @Autowired
    productRepository productRepo;
    
    
	@PostMapping("/storeownerHome")
	public String addStore(Model model,@ModelAttribute("storerequest") StoreRequests storerequest,@ModelAttribute("productstore") ProductStore productstore,@ModelAttribute("OnlineStore") OnlineStore onlinestore) {
		storeRequestRepo.save(storerequest);
		model.addAttribute("storerequest",new StoreRequests());
		model.addAttribute("productstore",new ProductStore());
		model.addAttribute("Product",productRepo.findAll());
		model.addAttribute("onlinestore",onlineStoreRepo.findAll());
		model.addAttribute("Brand",brandRepo.findAll());
		model.addAttribute("soldout",soldoutRepo.findAll());
		model.addAttribute("product",productStoreRepo.findAll());

		return "storeownerHome";
	}
	
	
	@PostMapping("/saveProduct")
	public String saveProduct(@Valid @ModelAttribute("productstore") ProductStore productstore, Model model,@RequestParam(name = "storeid") int storeid,@RequestParam(name = "brandid") int brandid,@RequestParam(name = "productid") int productid,@RequestParam(name = "quan") int quan,@RequestParam(name = "pri") int pri) {
		OnlineStore store=onlineStoreRepo.findById(storeid);
		Product product=productRepo.findById(productid);
		productstore.setStores(store);
		productstore.setStore_name(store.getStore_name());
		Brand brand=brandRepo.findById(brandid);
		productstore.setBrands(brand);
		productstore.setBrand_name(brand.getBrand_name());
		productstore.setProduct_name(product.getProduct_name());
		productstore.setPrice(pri);
		productstore.setQuantity(quan);
		productStoreRepo.save(productstore);
		model.addAttribute("storerequest",new StoreRequests());
		model.addAttribute("productstore",new ProductStore());
		model.addAttribute("Product",productRepo.findAll());
		model.addAttribute("onlinestore",onlineStoreRepo.findAll());
		model.addAttribute("Brand",brandRepo.findAll());
		model.addAttribute("soldout",soldoutRepo.findAll());
		model.addAttribute("product",productStoreRepo.findAll());

		return "storeownerHome";
	}

	


	
}
