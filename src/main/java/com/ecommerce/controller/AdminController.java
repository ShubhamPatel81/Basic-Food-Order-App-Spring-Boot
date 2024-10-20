package com.ecommerce.controller;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CatogeryService;
import com.ecommerce.service.ProductService;
import com.ecommercedto.ProductDTO;

import net.bytebuddy.implementation.bind.annotation.Default;

@Controller
public class AdminController {

	@Autowired
	CatogeryService catogeryService;

	@Autowired
	ProductService productService;

	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}

	@GetMapping("/admin/categories")
	public String getCategories(Model model) {
		model.addAttribute("categories", catogeryService.getAllCatogery());
		return "categories";
	}

	@GetMapping("/admin/categories/add")
	public String getCategoriesAdd(Model module) {
		module.addAttribute("category", new Category());
		return "categoriesAdd"; // Ensure this points to the correct file
	}

	@PostMapping("/admin/categories/add")
	public String postCategoriesAdd(@ModelAttribute Category category) {

		catogeryService.addCatogery(category);

		return "redirect:/admin/categories"; // Ensure this points to the correct file
	}

	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCatogery(@PathVariable int id) {
		catogeryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/update/{id}")
	public String updateCatogery(@PathVariable int id, Model model) {
		Optional<Category> catogery = catogeryService.getCategoryById(id);
		if (catogery.isPresent()) {
			model.addAttribute("category", catogery.get());
			return "categoriesAdd";
		} else {
			return "404";
		}
	}

	///////// ------Product Section ----------/////

	@GetMapping("/admin/products")
	public String getProducts(
	        @RequestParam(defaultValue = "1") int pageNumber,
	        @RequestParam(defaultValue = "8") int pageSize,
	        @RequestParam(defaultValue = "none") String sortBy,
	        @RequestParam(defaultValue = "asc") String order, Model model) {

	    // Fetch the page of products from the service
	    Page<Product> productPage = productService.getAllProduct(sortBy, order, pageNumber, pageSize);

	    // Add products and pagination metadata to the model
	    model.addAttribute("products", productPage.getContent());
	    model.addAttribute("currentPage", pageNumber);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("sortBy", sortBy);
	    model.addAttribute("order", order);

	    return "products";
	}


	@GetMapping("/admin/products/add")
	public String getProductAdd(Model model) {
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", catogeryService.getAllCatogery());

		return "productsAdd";
	}

	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

	@PostMapping("/admin/products/add")
	public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {

		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setCategory(catogeryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setDescription(productDTO.getDescription());

		String imageUUID;
		if (!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path path = Paths.get(uploadDir, imageUUID);
			Files.write(path, file.getBytes());

		} else {
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productService.addProduct(product);

//		System.out.println();
		return "redirect:/admin/products";

	}

	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable long id) {
		productService.removeProductById(id);
		return "redirect:/admin/products";
	}

	@GetMapping("/admin/product/update/{id}")
	public String updateProduct(@PathVariable long id, Model model) {

		Product product = productService.getProductById(id).get();
		ProductDTO dto = new ProductDTO();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setCategoryId(product.getCategory().getId());
		dto.setPrice(product.getPrice());
		dto.setWeight(product.getWeight());
		dto.setDescription(product.getDescription());
		dto.setImageName(product.getImageName());

		model.addAttribute("categories", catogeryService.getAllCatogery());
		model.addAttribute("productDTO", dto);
		return "productsAdd";
	}
}
