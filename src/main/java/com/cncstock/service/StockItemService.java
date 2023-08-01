package com.cncstock.service;

import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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

        checkRequiredFields(stockItem, "title", "location", "supplier", "minQty","category");

        // Check if a stock item with the same location already exists
        if (stockItemRepository.existsById(stockItem.getLocation())) {
            throw new IllegalArgumentException("Item with the same location already exists.");
        }
        return stockItemRepository.save(stockItem);
    }

    public StockItem updateStockItem(int location, StockItem updatedStockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Item with the specified location not found.");
        }

        StockItem existingStockItem = stockItemOptional.get();

        checkRequiredFields(updatedStockItem, "title", "supplier", "minQty", "category", "location","stockQty");

        checkLocationConflict(location, updatedStockItem);

        updateProperties(existingStockItem, updatedStockItem);

        return stockItemRepository.save(existingStockItem);
    }

    private void checkRequiredFields(StockItem stockItem, String... requiredFields) {
        List<String> reqItems = List.of(requiredFields);

        for (String fieldName : reqItems) {
            try {
                Field field = StockItem.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(stockItem);
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    throw new IllegalArgumentException(fieldName + " is a required field and cannot be null or empty.");
                }

                // Additional validation for numeric fields (minQty, location, stockQty)
                if (field.getType() == int.class) {
                    int numericValue = (int) value;
                    System.out.println("Num value="+ fieldName +"="+ numericValue);
                    if (numericValue < 0) {
                        throw new IllegalArgumentException(fieldName + " must be a non-negative value.");
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
            }
        }
    }

    private void checkLocationConflict(int location, StockItem updatedStockItem) {
        int updatedLocation = updatedStockItem.getLocation();
        if (location != updatedLocation && stockItemRepository.existsById(updatedLocation)) {
            throw new IllegalArgumentException("Item with the updated location already exists.");
        }
    }

    private void updateProperties(StockItem existingStockItem, StockItem updatedStockItem) {
        Class<? extends StockItem> stockItemClass = existingStockItem.getClass();
        Field[] fields = stockItemClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object newValue = field.get(updatedStockItem);
                if (newValue != null && !Objects.equals(newValue, field.get(existingStockItem))) {
                    field.set(existingStockItem, newValue);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Item not updated.");
            }
        }
    }

    public void deleteStockItem(int location) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(location);
        if (stockItemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with the specified location not found.");
        }
        stockItemRepository.deleteById(location);
    }

}
