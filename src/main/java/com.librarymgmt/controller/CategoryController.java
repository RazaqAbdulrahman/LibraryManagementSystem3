package com.librarymgmt.controller;

import com.librarymgmt.model.Category;
import com.librarymgmt.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.listAllCategories());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        categoryService.addCategory(category);
        return ResponseEntity.status(201).body(category);
    }
}
