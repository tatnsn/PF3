package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Category; // カテゴリーエンティティをインポート
import com.example.demo.repository.jpa.CategoryRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/list")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findByIsDeletedFalse(); // 論理削除されていないカテゴリを取得
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setDeletedDate(LocalDateTime.now()); // 削除日を設定
        category.setIsDeleted(true); // 論理削除フラグを設定
        categoryRepository.save(category); // 更新して保存
        return "redirect:/categories/list";
    }
    
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/add";
    }

    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "categories/add";
        }
        category.setCreatedDate(LocalDateTime.now()); // 作成日を設定
        categoryRepository.save(category);
        return "redirect:/categories/list";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, @Valid @ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "categories/edit";
        }
        category.setId(id); // IDを設定して更新
        category.setUpdatedDate(LocalDateTime.now()); // 更新日を設定
        categoryRepository.save(category);
        return "redirect:/categories/list";
    }
}
