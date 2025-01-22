package com.zain.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zain.Request.CreateProductRequest;
import com.zain.exception.ProductException;
import com.zain.model.Category;
import com.zain.model.Product;
import com.zain.model.Size;
import com.zain.repository.CategoryRepository;
import com.zain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private UserService userService;

    @Value("${AZURE.CONTAINER}")
    private String CONTAINER_NAME ;

    @Value("${AZURE.KEY}")
    private String CONNECTION_STRING;

    public ProductServiceImplementation(ProductRepository productRepository, CategoryRepository categoryRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public Product createProduct( @RequestParam("image") MultipartFile image,
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
                                  @RequestParam("description") String description
                            ) throws ProductException, IOException {
        System.out.println(title + "  " + color + "  " + discountedPrice + "  " + price + "  " + discountPersent
                + " " + sizeJson + " " + quantity + " " + topLavelCategory + " " + secondLavelCategory + " " +
                thirdLavelCategory + " " + description
        );

        ObjectMapper objectMapper = new ObjectMapper();
        List<Size> sizesList = null;
        try {
            sizesList = objectMapper.readValue(sizeJson, new TypeReference<List<Size>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Size> sizesSet = new HashSet<>(sizesList);

        String imageUrl = uploadImageToAzure(image);
        Category topLevel = categoryRepository.findByName(topLavelCategory);

        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(topLavelCategory);
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(secondLavelCategory, topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(secondLavelCategory);
            secondLevelCategory.setLevel(2);
            secondLevelCategory.setParentCategory(topLevel);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(thirdLavelCategory, secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(thirdLavelCategory);
            thirdLevelCategory.setLevel(3);
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(title);
        product.setColor(color);
        product.setDescription(description);
        product.setDiscountedPrice(discountedPrice);
        product.setDiscountedPersent(discountPersent);
        product.setImageUrl(imageUrl);
        product.setPrice(price);
        product.setSizes(sizesSet);
        product.setQuantity(quantity);
        product.setCategory(thirdLevel);
        product.setCreationAt(LocalDateTime.now());

        Product saveProduct = productRepository.save(product);
        return saveProduct;
    }

    public String uploadImageToAzure(MultipartFile image) throws IOException {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        String fileName = System.currentTimeMillis() + "-" + image.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        try (InputStream inputStream = image.getInputStream()) {
            blobClient.upload(inputStream, image.getSize(), true);
        }
        System.out.println("blobClient.getBlobUrl() "+ blobClient.getBlobUrl());
        return blobClient.getBlobUrl();
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        if (product == null) {throw  new ProductException("Product not found");}
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted Successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product updateProduct = findProductById(productId);
        if (updateProduct == null) {throw  new ProductException("Product not found");}
        updateProduct.setTitle(req.getTitle());
        updateProduct.setColor(req.getColor());
        updateProduct.setDescription(req.getDescription());
        updateProduct.setDiscountedPrice(req.getDiscountedPrice());
        updateProduct.setDiscountedPersent(req.getDiscountedPersent());
        updateProduct.setImageUrl(req.getImageUrl());
        updateProduct.setPrice(req.getPrice());
        updateProduct.setSizes(req.getSizes());
        if (req.getQuantity() != 0){
            updateProduct.setQuantity(req.getQuantity());
        }
        Product saveProduct = productRepository.save(updateProduct);
        return saveProduct;
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductException("Product not found with id: "+id);
    }

    @Override
    public Product findProductByCategory(String category) throws ProductException {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) throws ProductException {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
        if (!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream()
                            .anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                            .collect(Collectors.toList());
        }
        if (stock != null) {
            if (stock.equals("in_stock")){
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            }
            else if (stock.equals("out_stock")){
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(startIndex+pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

        return filteredProducts;
    }
}
