package com.cncstock.controller.stockitem;

import com.cncstock.exception.CategoryNotFoundException;
import com.cncstock.exception.SubCategoryAlreadyExistsException;
import com.cncstock.model.entity.stockitem.StockItemCategory;
import com.cncstock.model.entity.stockitem.StockItemSubCategory;
import com.cncstock.repository.stockitem.StockItemCategoryRepository;
import com.cncstock.repository.stockitem.StockItemSubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class StockItemSubCategoryController {

    private final StockItemSubCategoryRepository stockItemSubCategoryRepository;

    private final StockItemCategoryRepository stockItemCategoryRepository;

    @Autowired
    public StockItemSubCategoryController(StockItemSubCategoryRepository stockItemSubCategoryRepository, StockItemCategoryRepository stockItemCategoryRepository) {
        this.stockItemSubCategoryRepository = stockItemSubCategoryRepository;
        this.stockItemCategoryRepository = stockItemCategoryRepository;
    }

    @PostMapping("/subcategory")
    public ResponseEntity<?> createSubCategory(@RequestBody StockItemSubCategory stockItemSubCategory) {
        StockItemCategory stockItemCategory = stockItemSubCategory.getStockItemCategory();
        Optional<StockItemCategory> optionalStockItemCategory = stockItemCategoryRepository.findById(stockItemCategory.getId());

        if (optionalStockItemCategory.isEmpty()) {
            throw new CategoryNotFoundException(stockItemCategory.getCategoryName());
        }

        boolean subCategoryExistsForGivenCategory = stockItemSubCategoryRepository.existsBySubCategoryNameAndStockItemCategory(
                stockItemSubCategory.getSubCategoryName(), stockItemCategory);

        if (subCategoryExistsForGivenCategory) {
            throw new SubCategoryAlreadyExistsException(stockItemSubCategory.getSubCategoryName());
        }

        StockItemSubCategory savedSubCategory = stockItemSubCategoryRepository.save(stockItemSubCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subcategory created with ID: " + savedSubCategory.getId());
    }

    @GetMapping("/subcategories/{category_id}")
    public ResponseEntity<List<StockItemSubCategory>> getSubCategories(@PathVariable Long category_id) {
        List<StockItemSubCategory> subCategoryList = stockItemSubCategoryRepository.findByStockItemCategory_Id(category_id);
        return new ResponseEntity<>(subCategoryList, HttpStatus.OK);
    }

    @PutMapping("/subcategory/{id}")
    public ResponseEntity<String> updateStockItemSubCategory(@PathVariable Long id, @RequestBody StockItemSubCategory updatedSubCategory) {
        StockItemSubCategory existingItem = stockItemSubCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sub category " + updatedSubCategory.getSubCategoryName() +  " not found"));

        existingItem.setSubCategoryName(updatedSubCategory.getSubCategoryName());
        existingItem.setStockItemCategory(updatedSubCategory.getStockItemCategory());

        stockItemSubCategoryRepository.save(existingItem);

        return ResponseEntity.ok("Item updated");
    }

    @DeleteMapping("/subcategory/{id}")
    public ResponseEntity<String> deleteStockItemSubCategory(@PathVariable("id") Long id) {
        if (stockItemSubCategoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub-Category not found");
        }
        stockItemSubCategoryRepository.deleteById(id);
        return ResponseEntity.ok("Sub-Category deleted");
    }
}
