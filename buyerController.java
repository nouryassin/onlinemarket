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

import com.example.demo.entities.DeletedAndEdited;
import com.example.demo.entities.Offer;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.SoldOutProduct;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.entities.collaborator;
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
public class buyerController {/** controls buyer entity transactions */
	
	@Autowired
	userRepository userRepo;
	@Autowired
	onlineStoreRepository onlineStoreRepo;
	
	@Autowired
	productStoreRepository productStoreRepo;
	
	@Autowired
	soldOutProductsRepository soldoutProductRepo; 
	
	@Autowired
	BrandRepository brandRepo;
	@Autowired
	soldOutProductsRepository soldoutRepo;
	@Autowired
	productRepository productRepo;
	@Autowired
	collaboratorRepository collaRepo;
	@Autowired
	OfferRepository offerRepo;
	@Autowired
	DeletedAndEditedRepository DelRepo;
	@Autowired
	sesionRepository sesionRepo;
	
	
	ProductStore productstore;
	
	@GetMapping("/buyerHome") /** shows buyer home to buy any products */
	public String showBuyerHome(Model model) {
		List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
		model.addAttribute("onlinestore",stores);
		int id=sesionRepo.findById(1).getUserid();

		model.addAttribute("user",userRepo.findById(id));
		return "buyerHome";
	}
	String type;
	boolean first;
	@PostMapping("/buyerHome")/** buyer makes one view to buy the product or not */
	public String showBuyerHome(Model model,@Valid @ModelAttribute("user") User user,@RequestParam(name = "productid") int productid ) {
		
		List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
		model.addAttribute("onlinestore",stores);
		productstore=productStoreRepo.findById(productid);
		productstore.setViews(productstore.getViews()+1);
		productStoreRepo.save(productstore);
		model.addAttribute("productstore",productstore);
		type=user.getType();
		first=user.isFirst();
		return "viewProduct";
	}
	
	@PostMapping("/viewProduct") /** buyer buy the product ,deduct it from the store */
	public String buyProduct(Model model,@RequestParam(name = "quant") int quant) {
		float discount=0;
		if(type.compareTo("storeowner")==0) {
			discount+=15;
		}
		if(first) {
			discount+=5;
			first=false;
		}
		if(quant>1) {
			discount+=10;
		}
		
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
			sold.setStoreid(productstore.getStores().getId());
			sold.setStore_name(productstore.getStore_name());
			sold.setViews(productstore.getViews());
			sold.setBuyers(productstore.getBuyers());
			soldoutProductRepo.save(sold);
		}
		ProductStore pro=new ProductStore();
		pro.setQuantity(quant);
		pro.setProduct_name(productstore.getProduct_name());
		pro.setBuyers((int)discount);
		pro.setPrice(productstore.getPrice()*quant);
		pro.setPrice(pro.getPrice()-(pro.getPrice()*discount/100));
		model.addAttribute("pro",pro);
		return"checkout";
	}
	@PostMapping("checkout") /** product bought by the buyer is shown to him to check out  and confirm it */
	public String checkout(Model model) {
		int id=sesionRepo.findById(1).getUserid();

		if(type.compareTo("buyer")==0) {
			List<OnlineStore> stores=(List<OnlineStore>) onlineStoreRepo.findAll();
			model.addAttribute("onlinestore",stores);
			model.addAttribute("user",userRepo.findById(id));
	
			return "buyerHome";
		}
		else  { /** return storeowner home  */
			
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
			
			return "storeownerHome";

			
		}
		
	}
	
	
}
