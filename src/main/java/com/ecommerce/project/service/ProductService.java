package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts();

    ProductResponse searchProductsByCategory(Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);
}
