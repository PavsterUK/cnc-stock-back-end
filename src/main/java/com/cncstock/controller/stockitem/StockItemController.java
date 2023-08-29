package com.cncstock.controller.stockitem;

import com.cncstock.model.dto.StockItemDTO;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class StockItemController {

    private final StockItemService stockItemService;

    @Autowired
    public StockItemController(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }

    @GetMapping("/stock-list")
    public ResponseEntity<List<StockItemDTO>> getAllStockItems() {
        List<StockItemDTO> stockItems = stockItemService.getAllStockItems();
        if (stockItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @GetMapping("/stock-item/{id}")
    public ResponseEntity<StockItem> getStockItemById(@PathVariable("id") Long id) {
        StockItem stockItem = stockItemService.getStockItemById(id);
        return new ResponseEntity<>(stockItem, HttpStatus.OK);
    }

    @PostMapping("/stock-item")
    public ResponseEntity<?> createStockItem(@RequestBody StockItem stockItem) {
        StockItem savedStockItem = stockItemService.createStockItem(stockItem);
        return new ResponseEntity<>(savedStockItem, HttpStatus.CREATED);
    }

    @PutMapping("/stock-item/{id}")
    public ResponseEntity<?> updateStockItem(@PathVariable("id") Long id, @RequestBody StockItem stockItem) {
        StockItem updatedStockItem = stockItemService.updateStockItem(id, stockItem);
        return new ResponseEntity<>("Item Updated", HttpStatus.OK);
    }


    @DeleteMapping("/stock-item/{id}")
    public ResponseEntity<String> deleteStockItem(@PathVariable("id") Long id) {
        stockItemService.deleteStockItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
