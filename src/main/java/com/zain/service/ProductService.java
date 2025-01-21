package com.zain.service;

import com.zain.Request.CreateProductRequest;
import com.zain.exception.ProductException;
import com.zain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product createProduct(@RequestParam("image") MultipartFile image,
                          @RequestParam("title") String title,
                          @RequestParam("brand") String brand,
                          @RequestParam("color") String color,
                          @RequestParam("discountedPrice") int discountedPrice,
                          @RequestParam("price") int price,
                          @RequestParam("discountPersent") int discountPersent,
                          @RequestParam("size") String sizeJson,  // size will be a JSON string
                          @RequestParam("quantity") Integer quantity,
                          @RequestParam("topLavelCategory") String topLavelCategory,
                          @RequestParam("secondLavelCategory") String secondLavelCategory,
                          @RequestParam("thirdLavelCategory") String thirdLavelCategory,
                          @RequestParam("description") String description) throws IOException;

    String deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product req ) throws ProductException;
    Product findProductById(Long id) throws ProductException;
    Product findProductByCategory(String category) throws ProductException;
    Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) throws ProductException;

}
