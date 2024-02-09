package com.cncstock.controller.stockitem;

import com.cncstock.exception.CategoryNotFoundException;
import com.cncstock.exception.SubCategoryAlreadyExistsException;
import com.cncstock.exception.SubCategoryNotFoundException;
import com.cncstock.model.dto.StockItemCategoryDTO;
import com.cncstock.model.dto.StockItemSubCategoryDTO;
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
import java.util.stream.Collectors;

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

    @PostMapping("/subcategory/add/{category_id}")
    public ResponseEntity<?> createSubCategory(@PathVariable Long category_id, @RequestParam String subcategoryName) {
        Optional<StockItemCategory> optionalStockItemCategory = stockItemCategoryRepository.findById(category_id);

        if (optionalStockItemCategory.isEmpty()) {
            throw new CategoryNotFoundException("category not found with id " + category_id);
        }

        boolean subCategoryExistsForGivenCategory = stockItemSubCategoryRepository.existsBySubCategoryNameAndStockItemCategory(
                subcategoryName, optionalStockItemCategory.get());

        if (subCategoryExistsForGivenCategory) {
            throw new SubCategoryAlreadyExistsException(subcategoryName);
        }

        StockItemSubCategory savedSubCategory = stockItemSubCategoryRepository.save(new StockItemSubCategory(optionalStockItemCategory.get(), subcategoryName));
        return ResponseEntity.status(HttpStatus.CREATED).body("Subcategory created with ID: " + savedSubCategory.getId());
    }

    @GetMapping("/subcategories/{category_id}")
    public ResponseEntity<List<StockItemSubCategoryDTO>> getSubCategories(@PathVariable Long category_id) {
        List<StockItemSubCategory> subCategoryList = stockItemSubCategoryRepository.findByStockItemCategory_Id(category_id);

        List<StockItemSubCategoryDTO> dtoList = subCategoryList.stream()
                .map(subCategory -> new StockItemSubCategoryDTO(subCategory.getId(), subCategory.getSubCategoryName(), subCategory.getStockItemCategory().getId()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<StockItemSubCategoryDTO> getSubCategory(@PathVariable Long subCategoryId) {
        Optional<StockItemSubCategory> optionalSubCategory = stockItemSubCategoryRepository.findById(subCategoryId);
        if (optionalSubCategory.isEmpty()) {
            throw new SubCategoryNotFoundException(Long.toString(subCategoryId));
        }
        StockItemSubCategory subCategory = optionalSubCategory.get();
        StockItemSubCategoryDTO dtoSubCat = new StockItemSubCategoryDTO(subCategory.getId(), subCategory.getSubCategoryName(), subCategory.getStockItemCategory().getId());
        return new ResponseEntity<>(dtoSubCat, HttpStatus.OK);
    }

    @PutMapping("/subcategory/{id}")
    public ResponseEntity<String> updateStockItemSubCategory(@PathVariable Long id, @RequestBody StockItemSubCategory updatedSubCategory) {
        StockItemSubCategory existingItem = stockItemSubCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sub category " + updatedSubCategory.getSubCategoryName() + " not found"));

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
