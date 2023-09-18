package com.cncstock.controller.stockitem;

import com.cncstock.exception.CategoryAlreadyExistsException;
import com.cncstock.model.dto.StockItemCategoryDTO;
import com.cncstock.model.entity.stockitem.StockItemCategory;
import com.cncstock.repository.stockitem.StockItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class StockItemCategoryController {

    private final StockItemCategoryRepository stockItemCategoryRepository;
    @Autowired
    public StockItemCategoryController(StockItemCategoryRepository stockItemCategoryRepository) {
        this.stockItemCategoryRepository = stockItemCategoryRepository;
    }

    @PostMapping("/category/add")
    public ResponseEntity<?> createCategory(@RequestParam String categoryName) {
        if(stockItemCategoryRepository.existsByCategoryName(categoryName)) {
            throw new CategoryAlreadyExistsException(categoryName);
        }
        StockItemCategory stockItemCategory = new StockItemCategory(categoryName);
        StockItemCategory savedCategory = stockItemCategoryRepository.save(stockItemCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created with ID: " + savedCategory.getId());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<StockItemCategoryDTO>> getCategories() {
        List<StockItemCategory> categoryList = stockItemCategoryRepository.findAll();

        List<StockItemCategoryDTO> dtoList = categoryList.stream()
                .map(category -> new StockItemCategoryDTO(category.getId(), category.getCategoryName()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateStockItemCategory(@PathVariable Long id, @RequestBody StockItemCategory updatedCategory) {
        StockItemCategory existingItem = stockItemCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        existingItem.setCategoryName(updatedCategory.getCategoryName());

        stockItemCategoryRepository.save(existingItem);

        return ResponseEntity.ok("Item updated");
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteStockItemCategory(@PathVariable("id") Long id) {
        if (stockItemCategoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        stockItemCategoryRepository.deleteById(id);
        return ResponseEntity.ok("Item deleted");
    }

}
