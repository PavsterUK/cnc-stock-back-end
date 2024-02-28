package com.cncstock.controller.stockitem;

import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.service.LowStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/lowstockitems")
    public ResponseEntity<List<LowStockItem>> getCategories() {
        List<LowStockItem> lowStockItems = lowStockService.updateWithMostRecentItems();
        return new ResponseEntity<>(lowStockItems, HttpStatus.OK);
    }

}
