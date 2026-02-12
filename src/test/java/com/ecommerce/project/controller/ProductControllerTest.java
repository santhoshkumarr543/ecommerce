package com.ecommerce.project.controller;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com.ecommerce.project.security.*"
        )
)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    // addProduct

    @Test
    void shouldAddProduct() throws Exception {

        ProductDTO dto = new ProductDTO(
                1L,"Phone","img","good",5,100.0,10.0,90.0
        );

        when(productService.addProduct(1L, dto)).thenReturn(dto);

        mockMvc.perform(post("/api/admin/categories/1/product")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    // getAllProducts

    @Test
    void shouldGetAllProducts() throws Exception {

        ProductResponse response = new ProductResponse();
        when(productService.getAllProducts(0,5,"productId","asc"))
                .thenReturn(response);

        mockMvc.perform(get("/api/public/products"))
                .andExpect(status().isOk());
    }

    // deleteProducts
    @Test
    void shouldDeleteProduct() throws Exception {

        when(productService.deleteProduct(1L))
                .thenReturn(new ProductDTO());

        mockMvc.perform(delete("/api/admin/products/1"))
                .andExpect(status().isOk());
    }
}
