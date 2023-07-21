package com.cncstock.controller;

import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class StockItemController {

    @Autowired
    StockItemRepository stockItemRepository;

    @GetMapping("/stock-items")
    public ResponseEntity<List<StockItem>> getAllStockItems() {
        try {
            List<StockItem> stockItems = stockItemRepository.findAll();

            if (stockItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(stockItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stock-list")
    public ResponseEntity<List<StockItem>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<StockItem> stockItems = new ArrayList<StockItem>();

            if (title == null)
                stockItemRepository.findAll().forEach(stockItems::add);
            else
                stockItemRepository.findByTitleContaining(title).forEach(stockItems::add);

            if (stockItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(stockItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stock-item/{location}")
    public ResponseEntity<StockItem> getStockItemByLocation(@PathVariable("location") int location) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        return stockItemOptional.map(stockItem -> new ResponseEntity<>(stockItem, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/stock-item")
    public ResponseEntity<StockItem> createStockItem(@RequestBody StockItem stockItem) {
        try {
            StockItem savedStockItem = stockItemRepository.save(stockItem);
            return new ResponseEntity<>(savedStockItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/stock-item/{location}")
    public ResponseEntity<StockItem> updateStockItem(@PathVariable("location") int location, @RequestBody StockItem stockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isPresent()) {
            StockItem existingStockItem = stockItemOptional.get();
            existingStockItem.setTitle(stockItem.getTitle());
            existingStockItem.setBrand(stockItem.getBrand());
            existingStockItem.setSupplier(stockItem.getSupplier());
            existingStockItem.setMinQty(stockItem.getMinQty());
            existingStockItem.setDescription(stockItem.getDescription());
            existingStockItem.setCategory(stockItem.getCategory());
            existingStockItem.setMaterials(stockItem.getMaterials());

            StockItem updatedStockItem = stockItemRepository.save(existingStockItem);
            return new ResponseEntity<>(updatedStockItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/stock-item/{location}")
    public ResponseEntity<HttpStatus> deleteStockItem(@PathVariable("location") int location) {
        try {
            stockItemRepository.deleteById(location);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
