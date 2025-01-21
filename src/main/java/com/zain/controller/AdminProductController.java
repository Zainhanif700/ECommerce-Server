package com.zain.controller;

import com.zain.Request.CreateProductRequest;
import com.zain.exception.ProductException;
import com.zain.model.Order;
import com.zain.model.Product;
import com.zain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("brand") String brand,
            @RequestParam("color") String color,
            @RequestParam("discountedPrice") int discountedPrice,
            @RequestParam("price") int price,
            @RequestParam("discountPersent") int discountPersent,
            @RequestParam("size") String sizeJson,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("topLavelCategory") String topLavelCategory,
            @RequestParam("secondLavelCategory") String secondLavelCategory,
            @RequestParam("thirdLavelCategory") String thirdLavelCategory,
            @RequestParam("description") String description
    ) throws IOException {
        Product newProduct = productService.createProduct(
                image, title, brand, color, discountedPrice, price, discountPersent,
                sizeJson, quantity, topLavelCategory, secondLavelCategory,
                thirdLavelCategory, description
        );
        return ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Prodct Deleted Successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(required = false, defaultValue = "Integer.MAX_VALUE") Integer maxPrice,
            @RequestParam(required = false, defaultValue = "0") Integer minDiscount,
            @RequestParam(required = false, defaultValue = "name") String sort,
            @RequestParam(required = false, defaultValue = "inStock") String stock,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) throws ProductException {

        Page<Product> products = productService.getAllProduct(
                category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> getAllProducts(
            @PathVariable Long productId, @RequestBody Product req
    ) throws ProductException {
        Product product = productService.updateProduct(productId, req);
        return ResponseEntity.ok(product);
    }
    
}

