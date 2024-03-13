package com.cncstock.controller.stockitem;

import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.service.LowStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class LowStockItemController {

    private final LowStockService lowStockService;

    @Autowired
    public LowStockItemController(LowStockService lowStockService) {
        this.lowStockService = lowStockService;
    }

    @GetMapping("/low-stock-items")
    public ResponseEntity<List<LowStockItem>> getLowStockItems() {
        List<LowStockItem> lowStockItems = lowStockService.updateWithMostRecentItems();
        return new ResponseEntity<>(lowStockItems, HttpStatus.OK);
    }

    @PutMapping ("/low-stock-item/{id}")
    public ResponseEntity<LowStockItem> updateLowStockItem(@PathVariable("id") Long id) {
        LowStockItem updatedItem = lowStockService.toggleOrderDate(id);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

}
