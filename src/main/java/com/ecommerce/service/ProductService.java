package com.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepo;
//import com.paytm.miniapp.utils.StringUtil;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;
	


	public Page<Product> getAllProduct(String sortBy, String order, int pageNumber, int pageSize) {
	    // Determine sort direction (ascending or descending)
	    boolean isAscending = StringUtils.hasText(order) && order.equalsIgnoreCase("asc");

	    // Default sorting based on the sortBy parameter
	    Sort sort;
	    if ("sortByName".equalsIgnoreCase(sortBy)) {
	        sort = Sort.by(isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, "name");
	    } else if ("sortByDate".equalsIgnoreCase(sortBy)) {
	        sort = Sort.by(isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, "date");
	    } else {
	        // Default sorting or no sorting (unsorted)
	        sort = Sort.unsorted();
	    }

	    // Create a pageable object that includes both page number, page size, and sorting
	    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);  // pageNumber is 0-based in Spring

	    // Fetch the page of products from the repository
	    return productRepo.findAll(pageable);  // Return the entire page object, not just the content
	}     

    
    
    
	
	public void addProduct(Product product) {
		productRepo.save(product);
	}
	
	public void removeProductById(long id ) {
		productRepo.deleteById(id);
	}
	
	public Optional<Product> getProductById(long id){
		return productRepo.findById(id);
	}
	
	public List<Product> getAllProductByCatogeryId(int id){
		return productRepo.findAllByCategory_id(id);
	}
}
