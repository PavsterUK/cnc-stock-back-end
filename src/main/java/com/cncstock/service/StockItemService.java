package com.cncstock.service;

import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockItemService {

    private final StockItemRepository stockItemRepository;

    @Autowired
    public StockItemService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public List<StockItem> getAllStockItems() {
        return stockItemRepository.findAll();
    }

    public List<StockItem> getAllStockItemsByTitle(String title) {
        if (title == null) {
            return stockItemRepository.findAll();
        } else {
            return stockItemRepository.findByTitleContaining(title);
        }
    }

    public StockItem getStockItemByLocation(int location) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        return stockItemOptional.orElse(null);
    }

    public StockItem createStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    public StockItem updateStockItem(int location, StockItem updatedStockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isPresent()) {
            StockItem existingStockItem = stockItemOptional.get();
            existingStockItem.setTitle(updatedStockItem.getTitle());
            existingStockItem.setBrand(updatedStockItem.getBrand());
            existingStockItem.setSupplier(updatedStockItem.getSupplier());
            existingStockItem.setMinQty(updatedStockItem.getMinQty());
            existingStockItem.setDescription(updatedStockItem.getDescription());
            existingStockItem.setCategory(updatedStockItem.getCategory());
            existingStockItem.setMaterials(updatedStockItem.getMaterials());

            return stockItemRepository.save(existingStockItem);
        } else {
            return null;
        }
    }

    public void deleteStockItem(int location) {
        stockItemRepository.deleteById(location);
    }
}
