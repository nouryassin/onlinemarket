package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.AdminStatsitics;
import com.example.demo.entities.DeletedAndEdited;
import com.example.demo.entities.Offer;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.SoldOutProduct;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.entities.collaborator;
import com.example.demo.entities.sesion;
import com.example.demo.repositories.AdminStatRepository;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.DeletedAndEditedRepository;
import com.example.demo.repositories.OfferRepository;
import com.example.demo.repositories.collaboratorRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.productStoreRepository;
import com.example.demo.repositories.sesionRepository;
import com.example.demo.repositories.soldOutProductsRepository;
import com.example.demo.repositories.userRepository;

@Controller
public class userController {
	
	@Autowired
	private sesionRepository sesionRepo;
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
	@Autowired
	collaboratorRepository collaRepo;
	@Autowired
	OfferRepository offerRepo;
	@Autowired
	DeletedAndEditedRepository DelRepo;
	@Autowired
	AdminStatRepository adminstatRepo;
	
	
	@GetMapping("/addUser") /** shows sign up page */
	public String showForm(Model model) {
		model.addAttribute("user",new User());
		return "addUser";
	}
	
	@PostMapping("/addUser")/** adds a new user to the system */
	public String addUser(Model model,@ModelAttribute User user) {
		
		userRepo.save(user);
		if(user.getType().compareTo("storeowner")==0) {
			collaborator colla=new collaborator();
			colla.setId(user.getId());
			colla.setName(user.getName());
			colla.setPassword(user.getPassword());
			colla.setUsername(user.getUsername());
			colla.setEmail(user.getEmail());
			collaRepo.save(colla);
		}
		model.addAttribute("user",new User());
		return "login";
	}
	
	@GetMapping("/login") /** shows user login page */
	public String showLoginForm(Model model) {
		model.addAttribute("user",new User());
		return "login";
	}

	@PostMapping("/login") /** login user to the system */
	public String LoginUser(Model model,@RequestParam(name = "uname") String uname,@RequestParam(name = "pass") String pass,@ModelAttribute User user,@Valid @ModelAttribute("productstore") ProductStore productstore,@Valid @ModelAttribute("storerequest") StoreRequests storerequest) {
		User user2=userRepo.findByUsername(uname);
		
		if(userRepo.findByUsernameAndPassword(uname, pass)!=null) {/** if exists shows home page */
			sesion sessi=new sesion();
			sessi.setId(1);
			sessi.setUserid(user2.getId());
			sesionRepo.save(sessi);
			int id=sesionRepo.findById(1).getUserid();
			if(user2.getType().compareTo("storeowner")==0) {
				model.addAttribute("storerequest",new StoreRequests());
				model.addAttribute("productstore",new ProductStore());
				model.addAttribute("Product",productRepo.findAll());
				model.addAttribute("onlinestore",onlineStoreRepo.findAllByStoreownerId(id));
				model.addAttribute("Brand",brandRepo.findAll());
				model.addAttribute("stores",onlineStoreRepo.findAllByStoreownerId(id));
				List<OnlineStore> editstores=(List<OnlineStore>) onlineStoreRepo.findAllByStoreownerId(id);
				model.addAttribute("editstores",editstores);
				model.addAttribute("collastores",onlineStoreRepo.findAllByCollaboratorId(id));

				model.addAttribute("collaboratorstore",onlineStoreRepo.findAllByCollaboratorId(id));
				List<collaborator>collaborators=(List<collaborator>) collaRepo.findAll();
				for(collaborator coll : collaborators) {
					if(coll.getId()==id) {
						collaborators.remove(coll);
						break;
					}
				}
				model.addAttribute("collaborators",collaborators);
				model.addAttribute("collaborators",collaborators);
				
				model.addAttribute("offerstores",onlineStoreRepo.findAllByStoreownerId(id));
				model.addAttribute("collaofferstores",onlineStoreRepo.findAllByCollaboratorId(id));
				List<OnlineStore> editofferstores=onlineStoreRepo.findAllByStoreownerId(id);
				List<SoldOutProduct>soldouts=new ArrayList();
				List<ProductStore>productStat=new ArrayList();
				for(OnlineStore on : editofferstores) {
					soldouts.addAll(soldoutRepo.findAllByStoreid(on.getId()));
					productStat.addAll(on.getStoreproducts());
				}
				
				editofferstores.addAll(onlineStoreRepo.findAllByCollaboratorId(id));
				List<Offer>offers=new ArrayList();
				
				for(OnlineStore on : editofferstores) {
					offers.addAll(offerRepo.findAllByStoreid(on.getId()));
				}
				model.addAttribute("offers",offers);
				
				model.addAttribute("soldout",soldouts);
				model.addAttribute("product",productStat);
				
				List<OnlineStore> deleteeditstores=onlineStoreRepo.findAllByStoreownerId(id);
				List<DeletedAndEdited> deleteEditactions=new ArrayList();
				List<DeletedAndEdited> addactions=new ArrayList();
				List<DeletedAndEdited> deleteEditoffers=new ArrayList();
				List<DeletedAndEdited> addoffers=new ArrayList();
				
				for(OnlineStore store: deleteeditstores) {
					deleteEditactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product", "edit",store.getId()));
					deleteEditactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product","delete",store.getId()));
					addactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product", "add",store.getId()));
					deleteEditoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "edit", store.getId()));
					deleteEditoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "delete", store.getId()));
					addoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "add", store.getId()));
					
				}
				model.addAttribute("deleteEditactions",deleteEditactions);

				model.addAttribute("addactions",addactions);
				
				model.addAttribute("deleteEditoffers",deleteEditoffers);

				model.addAttribute("addoffers",addoffers);
				
				List<OnlineStore> buystores=(List<OnlineStore>) onlineStoreRepo.findAll();
				model.addAttribute("buystores",buystores);
				model.addAttribute("user",userRepo.findById(id));
				
				
				List<OnlineStore>statStores=onlineStoreRepo.findAllByStoreownerId(id);
				List<AdminStatsitics>MaxBought=new ArrayList();
				List<AdminStatsitics>MaxViewed=new ArrayList();
				List<AdminStatsitics>MiniBought=new ArrayList();
				List<AdminStatsitics>MiniViewed=new ArrayList();
				List<AdminStatsitics>TotalBuys=new ArrayList();
				List<AdminStatsitics>AverageBuys=new ArrayList();
				for(OnlineStore onstore :statStores)
				{
					MaxBought=adminstatRepo.findByStoreidAndType(onstore.getId(),"MaximumBoughtProduct");
					MaxViewed=adminstatRepo.findByStoreidAndType(onstore.getId(),"MaximumViewedtProduct");
					MiniBought=adminstatRepo.findByStoreidAndType(onstore.getId(),"MinimumBoughtProduct");
					MiniViewed=adminstatRepo.findByStoreidAndType(onstore.getId(),"MinimumViewedtProduct");
					TotalBuys=adminstatRepo.findByStoreidAndType(onstore.getId(),"TotalBuyingTimes");
					AverageBuys=adminstatRepo.findByStoreidAndType(onstore.getId(),"AverageBuyingTimes");
				}

				model.addAttribute("MaxBought",MaxBought);
				model.addAttribute("MaxViewed",MaxViewed);
				model.addAttribute("MiniBought",MiniBought);
				model.addAttribute("MiniViewed",MiniViewed);
				model.addAttribute("TotalBuys",TotalBuys);
				model.addAttribute("AverageBuys",AverageBuys);
				
				
				return "storeownerHome";
			}
			else if(user2.getType().compareTo("buyer")==0)  { /** shows buyer login page */
				List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
				model.addAttribute("onlinestore",stores);
				model.addAttribute("user",userRepo.findById(id));
				return "buyerHome";
			}
			else {
				model.addAttribute("user",new User());
				return "login";
			}
		}	
		else {
			model.addAttribute("user",new User()); /** shows login page again as wrong username or password */
			return"login";
		}
		
	}
	
}
