package com.librarymgmt.service;

import com.librarymgmt.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private List<Category> categories = new ArrayList<>();

    public void addCategory(Category category) {
        categories.add(category);
        System.out.println("Category added successfully.");
    }

    public void viewCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }
        for (Category category : categories) {
            System.out.println(category.getName() + " - " + category.getDescription());
        }
    }

    public List<Category> listAllCategories() {
        return List.of();
    }
}

