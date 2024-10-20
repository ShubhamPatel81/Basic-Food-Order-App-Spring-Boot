package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.global.GlobalData;
import com.ecommerce.model.Product;
import com.ecommerce.service.CatogeryService;
import com.ecommerce.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	CatogeryService catogeryService;

	@Autowired
	ProductService productService;

	@GetMapping({ "/", "home" })
	public String home(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "8") int pageSize,
			@RequestParam(defaultValue = "none") String sortBy, @RequestParam(defaultValue = "asc") String order,
			Model model) {

		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("categories", catogeryService.getAllCatogery());

		// Fetch the page of products from the service
		Page<Product> productPage = productService.getAllProduct(sortBy, order, pageNumber, pageSize);

		// Add products and pagination metadata to the model
		model.addAttribute("products", productPage.getContent()); // Product content
		model.addAttribute("currentPage", pageNumber); // Current page number
		model.addAttribute("totalPages", productPage.getTotalPages()); // Total number of pages
		model.addAttribute("pageSize", pageSize); // Number of items per page
		model.addAttribute("sortBy", sortBy); // Current sorting field
		model.addAttribute("order", order); // Current sorting order (asc/desc)

		return "index"; // Return the template name
	}

	@GetMapping("/shop")
	public String getAllCategoriesAndShop(
	        @RequestParam(defaultValue = "1") int pageNumber,
	        @RequestParam(defaultValue = "6") int pageSize,
	        @RequestParam(defaultValue = "none") String sortBy, 
	        @RequestParam(defaultValue = "asc") String order,
	        Model model) {

	    model.addAttribute("cartCount", GlobalData.cart.size());
	    model.addAttribute("categories", catogeryService.getAllCatogery());
	    
	    // Fetch the page of products
	    Page<Product> productPage = productService.getAllProduct(sortBy, order, pageNumber, pageSize);
	    
	    // Add pagination-related data to the model
	    model.addAttribute("products", productPage.getContent());
	    model.addAttribute("currentPage", pageNumber);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("sortBy", sortBy);
	    model.addAttribute("order", order);
	    
	    return "shop";
	}


	@GetMapping("/shop/category/{id}")
	public String shopByCategory(Model model, @PathVariable int id) {

		model.addAttribute("cartCount", GlobalData.cart.size());

		model.addAttribute("categories", catogeryService.getAllCatogery());
		model.addAttribute("products", productService.getAllProductByCatogeryId(id));
		return "shop";
	}

	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(Model model, @PathVariable int id) {

		model.addAttribute("cartCount", GlobalData.cart.size());

		model.addAttribute("product", productService.getProductById(id).get());
		return "viewProduct";
	}

}
