package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.testng.annotations.Test;

import com.example.demo.entities.Brand;
import com.example.demo.entities.DeletedAndEdited;
import com.example.demo.entities.Offer;
import com.example.demo.entities.OnlineStore;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductStore;
import com.example.demo.entities.SoldOutProduct;
import com.example.demo.entities.StoreRequests;
import com.example.demo.entities.User;
import com.example.demo.entities.collaborator;
import com.example.demo.repositories.ActionRepository;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.DeletedAndEditedRepository;
import com.example.demo.repositories.OfferRepository;
import com.example.demo.repositories.collaboratorRepository;
import com.example.demo.repositories.onlineStoreRepository;
import com.example.demo.repositories.productRepository;
import com.example.demo.repositories.productStoreRepository;
import com.example.demo.repositories.sesionRepository;
import com.example.demo.repositories.soldOutProductsRepository;
import com.example.demo.repositories.storeRequestRepository;
import com.example.demo.repositories.userRepository;

import junit.framework.Assert;;

@Controller
public class onlineStoreController { /** controls everything related to the online stores */
	
	
	@Autowired
	sesionRepository sesionRepo;
	@Autowired
	onlineStoreRepository onlineStoreRepo;
	@Autowired
	productStoreRepository productStoreRepo;
	@Autowired
	BrandRepository brandRepo;
    @Autowired
    storeRequestRepository storeRequestRepo;
    @Autowired
    soldOutProductsRepository soldoutRepo;
    @Autowired
    productRepository productRepo;
    @Autowired
    userRepository userRepo;
    @Autowired
    collaboratorRepository collaRepo;
    @Autowired
    ActionRepository actionRepo;
    @Autowired
    OfferRepository offerRepo;
    @Autowired
    DeletedAndEditedRepository DelRepo;
    
    
	@PostMapping("/storeownerHome") /** store owner add a new request to add a new store */
	public String addStore(Model model,@ModelAttribute("storerequest") StoreRequests storerequest,@ModelAttribute("productstore") ProductStore productstore,@ModelAttribute("OnlineStore") OnlineStore onlinestore) {
		int id=sesionRepo.findById(1).getUserid();
		storerequest.setStoreowner_id(id);
		storeRequestRepo.save(storerequest);
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

	
	
	@PostMapping("/saveProduct") /** saves new product to one of his stores */
	public String saveProduct(@Valid @ModelAttribute("productstore") ProductStore productstore, Model model,@RequestParam(name = "storeid") int storeid,@RequestParam(name = "brandid") int brandid,@RequestParam(name = "productid") int productid,@RequestParam(name = "quan") double quan,@RequestParam(name = "pri") double pri) {
		OnlineStore store=onlineStoreRepo.findById(storeid);
		Product product=productRepo.findById(productid);
		productstore.setStores(store);
		productstore.setStore_name(store.getStore_name());
		Brand brand=brandRepo.findById(brandid);
		productstore.setBrands(brand);
		productstore.setBrand_name(brand.getBrand_name());
		productstore.setProduct_name(product.getProduct_name());
		productstore.setPrice((float) pri);
		productstore.setQuantity((int) quan);
		if(pri<0||quan<0) {
			return "error";
		}
		productStoreRepo.save(productstore);
		DeletedAndEdited del=DelRepo.findByProductId(productid);
		if(del==null) {
			del=new DeletedAndEdited();
		}
		del.setActiontype("add");
		del.setType("product");
		del.setProduct_name(productstore.getProduct_name());
		del.setBrand_name(productstore.getBrand_name());
		del.setBuyers(productstore.getBuyers());;
		del.setPrice(productstore.getPrice());
		del.setQuantity(productstore.getQuantity());
		del.setStore_name(productstore.getStore_name());
		del.setProductId(productstore.getId());
		del.setViews(productstore.getViews());
		del.setStoreid(productstore.getStores().getId());
		del.setBrandid(productstore.getBrands().getId());

		DelRepo.save(del);


		int id=sesionRepo.findById(1).getUserid();

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
		
		for(OnlineStore store2: deleteeditstores) {
			deleteEditactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product", "edit",store2.getId()));
			deleteEditactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product","delete",store2.getId()));
			addactions.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("product", "add",store2.getId()));
			deleteEditoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "edit", store2.getId()));
			deleteEditoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "delete", store2.getId()));
			addoffers.addAll(DelRepo.findAllByTypeAndActiontypeAndStoreid("offer", "add", store2.getId()));
			
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
	
	@PostMapping("/addCollaborator") /** store owner adds a collaborator to stores */
	public String addCollaborator(@Valid @ModelAttribute("productstore") ProductStore productstore, Model model,@RequestParam(name = "storeid") int storeid,@RequestParam(name = "collaboratorid") int collaboratorid) {
		
		OnlineStore onstore=onlineStoreRepo.findById(storeid);
		collaborator colla=collaRepo.findById(collaboratorid);
		colla.getStore().add(onstore);
		onstore.setCollaborator(colla);
		onlineStoreRepo.save(onstore);
		collaRepo.save(colla);
		int id=sesionRepo.findById(1).getUserid();// hnkteb 7aga badalha

		model.addAttribute("storerequest",new StoreRequests());
		model.addAttribute("productstore",new ProductStore());
		model.addAttribute("Product",productRepo.findAll());
		model.addAttribute("onlinestore",onlineStoreRepo.findAllByStoreownerId(id));
		model.addAttribute("Brand",brandRepo.findAll());
		model.addAttribute("stores",onlineStoreRepo.findAllByStoreownerId(id));
		List<OnlineStore> editstores=(List<OnlineStore>) onlineStoreRepo.findAllByStoreownerId(id);
		model.addAttribute("editstores",editstores);
		model.addAttribute("collaboratorstore",onlineStoreRepo.findAllByCollaboratorId(id));
		List<collaborator>collaborators=(List<collaborator>) collaRepo.findAll();
		for(collaborator coll : collaborators) {
			if(coll.getId()==id) {
				collaborators.remove(coll);
				break;
			}
		}
		model.addAttribute("collaborators",collaborators);
		model.addAttribute("offerstores",onlineStoreRepo.findAllByStoreownerId(id));
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
	
	@PostMapping("/addOffer") /** store owner adds a new offer to product */
	public String addOffer(@Valid @ModelAttribute("productstore") ProductStore productstore, Model model,@RequestParam(name = "productid") int productid,@RequestParam(name = "discount") int discount) {
		ProductStore productStore=productStoreRepo.findById(productid);
		if(discount<0) {
			return "error";
		}
		Offer offer=new Offer();
		offer.setDiscount(discount);
		offer.setProductId(productid);
		offer.setProductName(productStore.getProduct_name());
		offer.setStoreid(productStore.getStores().getId());
		offerRepo.save(offer);
		
		DeletedAndEdited del=DelRepo.findByOfferid(offer.getId());
		if(del==null) {
			del=new DeletedAndEdited();
		}
		del.setActiontype("add");
		del.setType("offer");
		del.setOfferid(offer.getId());
		del.setDiscount(offer.getDiscount());
		del.setProductId(productStore.getId());
		del.setStoreid(productStore.getStores().getId());
		del.setProduct_name(offer.getProductName());
		del.setPrice(productStore.getPrice());
		del.setOriginalprice(productStore.getPrice());
		DelRepo.save(del);
		
		productStore.setPrice(productStore.getPrice()-(productStore.getPrice()*discount/100));
		productStoreRepo.save(productStore);
		
		int id=sesionRepo.findById(1).getUserid();

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
		editofferstores.addAll(onlineStoreRepo.findAllByCollaboratorId(id));
		List<Offer>offers=new ArrayList();
		for(OnlineStore on : editofferstores) {
			offers.addAll(offerRepo.findAllByStoreid(on.getId()));
		}
		model.addAttribute("offers",offers);
		
		model.addAttribute("soldout",soldoutRepo.findAll());
		model.addAttribute("product",productStoreRepo.findAll());
		
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
	float discountvalue=0;
	Offer editoffer=new Offer();
	@PostMapping("/editOffer") /** store owner edit existing offer */
	public String editOffer(Model model,@RequestParam(name = "offerid")  int offerid) {
		discountvalue=offerRepo.findById(offerid).getDiscount();
		editoffer=offerRepo.findById(offerid);
		model.addAttribute("editoffer",editoffer);
		return "editOffer";
	}
	
	@PostMapping("Edited") /** store owner edited an offer */
	public String edited(@RequestParam(name = "discount") int discount, Model model) {
		DeletedAndEdited del=DelRepo.findByOfferid(editoffer.getId());
		if(del==null) {
			del=new DeletedAndEdited();
		}
		ProductStore pro=productStoreRepo.findById(editoffer.getProductId());
		float price=pro.getPrice();
		pro.setPrice(del.getOriginalprice()-(del.getOriginalprice()*discount/100));
		productStoreRepo.save(pro);
		
		del.setActiontype("edit");
		del.setType("offer");
		del.setOfferid(editoffer.getId());
		del.setDiscount(editoffer.getDiscount());
		del.setProductId(editoffer.getProductId());
		del.setStoreid(editoffer.getStoreid());
		del.setProduct_name(editoffer.getProductName());
		del.setPrice(price);
		
		DelRepo.save(del);
		editoffer.setDiscount(discount);
		offerRepo.save(editoffer);
		
		
		int id=sesionRepo.findById(1).getUserid();

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
		editofferstores.addAll(onlineStoreRepo.findAllByCollaboratorId(id));
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
	@PostMapping("deleteOffer")	 /** store onwer can delete an offer */
	public String deleteoffer( Model model,@RequestParam(name = "offerid") int offerid) {
		Offer offer=offerRepo.findById(offerid);
		
		DeletedAndEdited del=DelRepo.findByOfferid(offer.getId());
		if(del==null) {
			del=new DeletedAndEdited();
		}
		ProductStore productStore;
		productStore=productStoreRepo.findById(del.getProductId());
		float price=productStore.getPrice();
		productStore.setPrice(del.getOriginalprice());
		productStoreRepo.save(productStore);
		
		del.setActiontype("delete");
		del.setType("offer");
		del.setOfferid(offer.getId());
		del.setDiscount(offer.getDiscount());
		del.setProductId(offer.getProductId());
		del.setStoreid(offer.getStoreid());
		del.setProduct_name(offer.getProductName());
		del.setPrice(price);
		DelRepo.save(del);
		
		
		offerRepo.delete(offer);

		int id=sesionRepo.findById(1).getUserid();

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

	
	
	@PostMapping("/delete")/** storeowner deleted an offer  */
	
	public String delete(@Valid @ModelAttribute("productstore") ProductStore productstore, Model model,@RequestParam(name = "productid") int productid) {
		ProductStore pro=productStoreRepo.findById(productid);
		DeletedAndEdited del=DelRepo.findByProductId(productid);
		if(del==null) {
			del=new DeletedAndEdited();
		}
		del.setActiontype("delete");
		del.setType("product");
		del.setProduct_name(pro.getProduct_name());
		del.setBrand_name(pro.getBrand_name());
		del.setBuyers(pro.getBuyers());;
		del.setPrice(pro.getPrice());
		del.setQuantity(pro.getQuantity());
		del.setStore_name(pro.getStore_name());
		del.setProductId(pro.getId());
		del.setViews(pro.getViews());
		del.setStoreid(pro.getStores().getId());
		del.setBrandid(pro.getBrands().getId());
		
		DelRepo.save(del);
		
		
		productStoreRepo.delete(pro);
		
		int id=sesionRepo.findById(1).getUserid();

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
	ProductStore pro=new ProductStore();
	@PostMapping("/showToEdit")/** store owner can edit his products */
	public String show(Model model,@RequestParam(name = "productid") int productid ) {
		System.out.println(productid);
		pro=productStoreRepo.findById(42);
		System.out.println(pro.getId());
		model.addAttribute("productstore",pro);
		return "showToEdit";
	}

	@PostMapping("/Edit")/** store owner edited a product */
	public String showToedit( @Valid @ModelAttribute("productstore") ProductStore productstore, Model model) {
		
		
		DeletedAndEdited del=DelRepo.findByProductId(productstore.getId());
		if(del==null) {
			del=new DeletedAndEdited();
		}
		del.setActiontype("edit");
		del.setType("product");
		del.setProduct_name(pro.getProduct_name());
		del.setBrand_name(pro.getBrand_name());
		del.setBuyers(pro.getBuyers());
		del.setPrice(pro.getPrice());
		del.setQuantity(pro.getQuantity());
		del.setStore_name(pro.getStore_name());
		del.setProductId(pro.getId());
		del.setViews(pro.getViews());
		del.setStoreid(pro.getStores().getId());
		del.setBrandid(pro.getBrands().getId());
		
		DelRepo.save(del);
		productstore.setStores(pro.getStores());
		productstore.setBrands(pro.getBrands());
		productStoreRepo.save(productstore);
		
		
		int id=sesionRepo.findById(1).getUserid();

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

	
	@PostMapping("/undoEditProduct")/** undo editing an exisiting product */
	public String undoEditProduct(Model model,@RequestParam(name = "actionid") int actionid ) {
		DeletedAndEdited action=DelRepo.findById(actionid);
		ProductStore product;
		product=productStoreRepo.findById(action.getProductId());
		if(product==null) {
			product=new ProductStore();
		}
		product.setBrand_name(action.getBrand_name());
		product.setBuyers(action.getBuyers());
		product.setPrice(action.getPrice());
		product.setQuantity(action.getQuantity());
		product.setId(action.getProductId());
		product.setViews(action.getViews());
		product.setProduct_name(action.getProduct_name());
		product.setStores(onlineStoreRepo.findById(action.getStoreid()));
		product.setBrands(brandRepo.findById(action.getBrandid()));
		productStoreRepo.save(product);
		
		DelRepo.delete(action);
		
		int id=sesionRepo.findById(1).getUserid();

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
	
	@PostMapping("/undoAddProduct")/** store owner can undo adding product */
	public String undoAddProduct(Model model,@RequestParam(name = "actionid") int actionid ) {
	
		DeletedAndEdited action=DelRepo.findById(actionid);
		productStoreRepo.delete(productStoreRepo.findById(action.getProductId()));
		DelRepo.delete(action);
		
		
		int id=sesionRepo.findById(1).getUserid();

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
	
	
	
	@PostMapping("/undoEditOffer")/** store owner can undo editing an offer */
	public String undoEditOffer(Model model,@RequestParam(name = "actionid") int actionid ) {
		DeletedAndEdited action=DelRepo.findById(actionid);
		Offer offer;
		ProductStore productStore =productStoreRepo.findById(action.getProductId());
		offer=offerRepo.findById(action.getOfferid());
		if(offer==null) {
			offer=new Offer();
			productStore.setPrice(action.getPrice());
		}
		else {
			productStore.setPrice(action.getPrice());
		}
		offer.setId(action.getOfferid());
		offer.setProductId(action.getProductId());
		offer.setDiscount(action.getDiscount());
		offer.setProductName(action.getProduct_name());
		offer.setStoreid(action.getStoreid());
		
		offerRepo.save(offer);
		
		action.setActiontype("add");
		action.setType("offer");
		action.setOfferid(offer.getId());
		action.setDiscount(offer.getDiscount());
		action.setProductId(productStore.getId());
		action.setStoreid(productStore.getStores().getId());
		action.setProduct_name(offer.getProductName());
		action.setPrice(action.getPrice());
		DelRepo.save(action);
		
		int id=sesionRepo.findById(1).getUserid();

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
	
	@PostMapping("/undoAddOffer")/** store owner undo adding offer */
	public String undoAddOffer(Model model,@RequestParam(name = "actionid") int actionid ) {
	
		DeletedAndEdited action=DelRepo.findById(actionid);
		ProductStore pro=productStoreRepo.findById(action.getProductId());
		pro.setPrice(action.getOriginalprice());
		productStoreRepo.save(pro);
		offerRepo.delete(offerRepo.findById(action.getOfferid()));
		DelRepo.delete(action);
		
		
		int id=sesionRepo.findById(1).getUserid();

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
