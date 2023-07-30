package com.cncstock.service;

import com.cncstock.ExceptionGenerator;
import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        //required fields
        String[] reqItemsArray = {"title", "location", "supplier", "minQty","category"};
        List<String> reqItems = new ArrayList<>(Arrays.asList(reqItemsArray));

        //check if any required fields are empty
        try {
            ExceptionGenerator.checkForEmptyFields(stockItem, reqItems);
        } catch (ExceptionGenerator.EmptyFieldException e) {
            String errorMessage = e.getMessage();
            int semicolonIndex = errorMessage.indexOf(":");
            if (semicolonIndex != -1) {
                errorMessage = errorMessage.substring(semicolonIndex + 1).trim();
            }
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if a stock item with the same location already exists
        if (stockItemRepository.existsById(stockItem.getLocation())) {
            throw new IllegalArgumentException("Item with the same location already exists.");
        }

        return stockItemRepository.save(stockItem);
    }

    public StockItem updateStockItem(int location, StockItem updatedStockItem) {
        // Check if a stock item with the given location exists
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with the specified location not found.");
        }

        StockItem existingStockItem = stockItemOptional.get();

        // Check if the updated location conflicts with an existing item
        if (location != updatedStockItem.getLocation() && stockItemRepository.existsById(updatedStockItem.getLocation())) {
            throw new IllegalArgumentException("Item with the updated location already exists.");
        }

        // Update the existing stock item
        existingStockItem.setTitle(updatedStockItem.getTitle());
        existingStockItem.setBrand(updatedStockItem.getBrand());
        existingStockItem.setSupplier(updatedStockItem.getSupplier());
        existingStockItem.setMinQty(updatedStockItem.getMinQty());
        existingStockItem.setDescription(updatedStockItem.getDescription());
        existingStockItem.setCategory(updatedStockItem.getCategory());
        existingStockItem.setMaterials(updatedStockItem.getMaterials());

        return stockItemRepository.save(existingStockItem);
    }

    public void deleteStockItem(int location) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with the specified location not found.");
        }
        stockItemRepository.deleteById(location);
    }
}
