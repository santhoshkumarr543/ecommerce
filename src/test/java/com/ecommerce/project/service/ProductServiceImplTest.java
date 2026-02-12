package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CartRepository cartRepository;


    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    FileServiceImpl fileService;

    @InjectMocks
    ProductServiceImpl productService;

    // AddProduct Test
    @Test
    void shouldAddProduct() {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setProducts(new ArrayList<>());

        ProductDTO dto = new ProductDTO(null,"Phone","img","good",10,1000.0,10.0,null);
        Product product = new Product();
        product.setPrice(1000.0);
        product.setDiscount(10.0);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(dto, Product.class)).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(dto);

        ProductDTO result = productService.addProduct(1L, dto);

        assertNotNull(result);
        verify(productRepository).save(any());
    }

    @Test
    void shouldThrowWhenProductAlreadyExists() {
        Product existing = new Product();
        existing.setProductName("Phone");

        Category category = new Category();
        category.setProducts(List.of(existing));

        ProductDTO dto = new ProductDTO();
        dto.setProductName("Phone");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(APIException.class,
                () -> productService.addProduct(1L, dto));
    }

    // getAllProducts Test

    @Test
    void shouldReturnAllProducts() {
        Product product = new Product();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(new ProductDTO());

        ProductResponse response =
                productService.getAllProducts(0,5,"price","asc");

        assertEquals(1, response.getContent().size());
    }

    // updateProduct Test

    @Test
    void shouldUpdateProduct() {

        Product existing = new Product();
        existing.setProductId(1L);

        ProductDTO dto = new ProductDTO(null,"New","i","d",5,200.0,10.0,null);

        Product mapped = new Product();
        mapped.setPrice(200.0);
        mapped.setDiscount(10.0);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(modelMapper.map(dto, Product.class))
                .thenReturn(mapped);

        when(cartRepository.findCartsByProductId(anyLong()))
                .thenReturn(new ArrayList<>());

        when(productRepository.save(existing))
                .thenReturn(existing);

        when(modelMapper.map(existing, ProductDTO.class))
                .thenReturn(dto);

        ProductDTO result = productService.updateProduct(dto,1L);

        assertEquals("New", result.getProductName());
    }


    // deleteProduct Test

    @Test
    void shouldDeleteProduct() {

        Product product = new Product();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(cartRepository.findCartsByProductId(anyLong()))
                .thenReturn(new ArrayList<>());

        when(modelMapper.map(product, ProductDTO.class))
                .thenReturn(new ProductDTO());

        ProductDTO result = productService.deleteProduct(1L);

        verify(productRepository).delete(product);
        assertNotNull(result);
    }

}