package com.cncstock.service;

import com.cncstock.event.StockItemUpdateEvent;
import com.cncstock.model.StockItem;
import com.cncstock.model.VendingTransaction;
import com.cncstock.repository.StockItemRepository;
import com.cncstock.repository.VendingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class StockItemService {

    private final StockItemRepository stockItemRepository;
    private final VendingTransactionRepository vendingTransactionRepository;

    private ApplicationEventPublisher eventPublisher;
    @Autowired
    public StockItemService(StockItemRepository stockItemRepository, VendingTransactionRepository vendingTransactionRepository, ApplicationEventPublisher eventPublisher) {
        this.stockItemRepository = stockItemRepository;
        this.vendingTransactionRepository = vendingTransactionRepository;
        this.eventPublisher = eventPublisher;
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

    public StockItem getStockItemById(Long id) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        return stockItemOptional.orElse(null);
    }

    public StockItem createStockItem(StockItem stockItem) {

        checkRequiredFields(stockItem, "title", "location", "supplier", "minQty","category","stockQty", "restockQty");

        return stockItemRepository.save(stockItem);
    }

    public StockItem updateStockItem(Long id, StockItem updatedStockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        if (stockItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Item with the specified id not found.");
        }

        StockItem existingStockItem = stockItemOptional.get();

        checkRequiredFields(updatedStockItem, "title", "supplier", "minQty", "category", "location","restockQty");

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
                    throw new IllegalArgumentException(fieldName + " is a required field and cannot be empty.");
                }

                // Additional validation for numeric fields (minQty, location, stockQty, restockQty)
                if (field.getType() == int.class) {
                    int numericValue = (int) value;
                    if (numericValue < 0) {
                        throw new IllegalArgumentException(fieldName + " must be a non-negative value.");
                    }
                    if (fieldName.equals("location") && numericValue < 1 ) {
                        throw new IllegalArgumentException(fieldName + " must be assigned to a stock item.");
                    }

                }
                // Additional validation for numeric fields (minQty, location, stockQty, restockQty)

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
            }
        }
    }

    private void updateProperties(StockItem existingStockItem, StockItem updatedStockItem) {
        StockItemUpdateEvent event = new StockItemUpdateEvent(this, updatedStockItem, existingStockItem.getStockQty() );
        eventPublisher.publishEvent(event);
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


    public void deleteStockItem(Long id) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        if (stockItemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with the specified id not found.");
        }
        stockItemRepository.deleteById(id);
    }

}
