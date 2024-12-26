package com.zain.service;

import com.zain.Request.CreateProductRequest;
import com.zain.exception.ProductException;
import com.zain.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest req);
    String deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product req ) throws ProductException;
    Product findProductById(Long id) throws ProductException;
    Product findProductByCategory(String category) throws ProductException;
    Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) throws ProductException;

}
