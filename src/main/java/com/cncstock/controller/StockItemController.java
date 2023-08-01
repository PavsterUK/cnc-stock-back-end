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
import java.util.NoSuchElementException;
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
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @GetMapping("/stock-item/{location}")
    public ResponseEntity<StockItem> getStockItemByLocation(@PathVariable("location") int location) {
        StockItem stockItem = stockItemService.getStockItemByLocation(location);
        return new ResponseEntity<>(stockItem, HttpStatus.OK);
    }

    @PostMapping("/stock-item")
    public ResponseEntity<?> createStockItem(@RequestBody StockItem stockItem) {
        StockItem savedStockItem = stockItemService.createStockItem(stockItem);
        return new ResponseEntity<>(savedStockItem, HttpStatus.CREATED);

    }

    @PutMapping("/stock-item/{location}")
    public ResponseEntity<?> updateStockItem(@PathVariable("location") int location, @RequestBody StockItem stockItem) {
        StockItem updatedStockItem = stockItemService.updateStockItem(location, stockItem);
        return new ResponseEntity<>("Item Updated", HttpStatus.OK);
    }


    @DeleteMapping("/stock-item/{location}")
    public ResponseEntity<String> deleteStockItem(@PathVariable("location") int location) {
        stockItemService.deleteStockItem(location);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
