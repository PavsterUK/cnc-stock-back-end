package com.cncstock.controller;

import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import com.cncstock.service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class StockItemController {

    private final StockItemService stockItemService;

    @Autowired
    public StockItemController(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }

    @GetMapping("/stock-items")
    public ResponseEntity<List<StockItem>> getAllStockItems() {
        List<StockItem> stockItems = stockItemService.getAllStockItems();
        if (stockItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @GetMapping("/stock-list")
    public ResponseEntity<List<StockItem>> getAllStockItemsByTitle(@RequestParam(required = false) String title) {
        List<StockItem> stockItems = stockItemService.getAllStockItemsByTitle(title);
        if (stockItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @GetMapping("/stock-item/{location}")
    public ResponseEntity<StockItem> getStockItemByLocation(@PathVariable("location") int location) {
        StockItem stockItem = stockItemService.getStockItemByLocation(location);
        if (stockItem != null) {
            return new ResponseEntity<>(stockItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/stock-item")
    public ResponseEntity<?> createStockItem(@RequestBody StockItem stockItem) {
        System.out.println(stockItem.toString());
        try {
            StockItem savedStockItem = stockItemService.createStockItem(stockItem);
            return new ResponseEntity<>(savedStockItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stock-item/{location}")
    public ResponseEntity<?> updateStockItem(@PathVariable("location") int location, @RequestBody StockItem stockItem) {
        try {
            StockItem updatedStockItem = stockItemService.updateStockItem(location, stockItem);
            if (updatedStockItem != null) {
                return new ResponseEntity<>(updatedStockItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error: Item with the specified location not found.", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/stock-item/{location}")
    public ResponseEntity<HttpStatus> deleteStockItem(@PathVariable("location") int location) {
        stockItemService.deleteStockItem(location);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
