package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldSaveCategory() {

        CategoryDTO inputDto = new CategoryDTO(1L, "Books");

        Category categoryEntity = new Category();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setCategoryName("Books");

        CategoryDTO outputDto = new CategoryDTO(1L, "Books");

        when(modelMapper.map(inputDto, Category.class))
                .thenReturn(categoryEntity);

        when(categoryRepository.save(categoryEntity))
                .thenReturn(categoryEntity);

        when(modelMapper.map(categoryEntity, CategoryDTO.class))
                .thenReturn(outputDto);

        CategoryDTO result =
                categoryService.createCategory(inputDto);

        assertEquals("Books", result.getCategoryName());

        verify(categoryRepository).save(categoryEntity);
        verify(categoryRepository).findByCategoryName("Books");
    }
}
