package org.springframework.security.data.acl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.data.acl.entity.Product;
import org.springframework.security.data.acl.repository.ProductRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@RequestMapping(value = { "/products" })
	public List<Product> findAll() {
		List<Product> products = productRepository.findAll();
		return products;
	}
}
