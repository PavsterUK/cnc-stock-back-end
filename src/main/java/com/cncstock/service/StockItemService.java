package com.cncstock.service;

import com.cncstock.event.StockItemUpdateEvent;
import com.cncstock.model.dto.StockItemDTO;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.model.entity.stockitem.StockItemCategory;
import com.cncstock.model.entity.stockitem.StockItemSubCategory;
import com.cncstock.repository.stockitem.StockItemCategoryRepository;
import com.cncstock.repository.stockitem.StockItemRepository;
import com.cncstock.repository.stockitem.StockItemSubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockItemService {

    private final StockItemRepository stockItemRepository;
    private final StockItemCategoryRepository stockItemCategoryRepository;

    private final StockItemSubCategoryRepository stockItemSubCategoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StockItemService(StockItemRepository stockItemRepository, StockItemCategoryRepository stockItemCategoryRepository, StockItemSubCategoryRepository stockItemSubCategoryRepository, ApplicationEventPublisher eventPublisher) {
        this.stockItemRepository = stockItemRepository;
        this.stockItemCategoryRepository = stockItemCategoryRepository;
        this.stockItemSubCategoryRepository = stockItemSubCategoryRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<StockItemDTO> getAllStockItems() {
        return toDTOList();
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

//    public StockItem createStockItem(StockItemDTO stockItemDTO) {
//
//        checkRequiredFields(StockItem, "title", "location", "supplier", "minQty", "category", "stockQty", "restockQty");
//
//        return stockItemRepository.save(stockItem);
//    }

    public StockItem updateStockItem(Long id, StockItem updatedStockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        if (stockItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Item with the specified id not found.");
        }

        StockItem existingStockItem = stockItemOptional.get();

        checkRequiredFields(updatedStockItem, "title", "supplier", "minQty", "category", "location", "restockQty");

        updateProperties(existingStockItem, updatedStockItem);

        return stockItemRepository.save(existingStockItem);
    }

    public void deleteStockItem(Long id) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        if (stockItemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with the specified id not found.");
        }
        stockItemRepository.deleteById(id);
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
                    if (fieldName.equals("location") && numericValue < 1) {
                        throw new IllegalArgumentException(fieldName + " must be assigned to a stock item.");
                    }

                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
            }
        }
    }

    private void updateProperties(StockItem existingStockItem, StockItem updatedStockItem) {
        int prevQty = existingStockItem.getStockQty();
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
        StockItemUpdateEvent event = new StockItemUpdateEvent(this, updatedStockItem, prevQty);
        eventPublisher.publishEvent(event);
    }

    private List<StockItemDTO> toDTOList() {
        List<StockItem> stockItemList = stockItemRepository.findAllWithCategoryAndSubCategory();

        return stockItemList.stream()
                .map(item -> new StockItemDTO(
                        item.getId(),
                        item.getLocation(),
                        item.getTitle(),
                        item.getBrand(),
                        item.getSupplier(),
                        item.getMinQty(),
                        item.getDescription(),
                        item.getCategory().getId(),
                        item.getCategory().getCategoryName(),
                        item.getSubCategory().getId(),
                        item.getSubCategory().getSubCategoryName(), // This should be the sub-category name
                        item.getMaterials(),
                        item.isConstantStock(),
                        item.getStockQty(),
                        item.getRestockQty()))
                .collect(Collectors.toList());
    }

    public StockItem toStockItem(StockItemDTO stockItemDTO) {
        Optional<StockItemCategory> stockItemCategory = stockItemCategoryRepository.findById(stockItemDTO.getId());
        Optional<StockItemSubCategory> stockItemSubCategory = stockItemSubCategoryRepository.findById(stockItemDTO.getSubCategoryId());

        StockItem stockItem = new StockItem();
        stockItem.setId(stockItemDTO.getId());
        stockItem.setLocation(stockItemDTO.getLocation());
        stockItem.setTitle(stockItemDTO.getTitle());
        stockItem.setBrand(stockItemDTO.getBrand());
        stockItem.setSupplier(stockItemDTO.getSupplier());
        stockItem.setMinQty(stockItemDTO.getMinQty());
        stockItem.setDescription(stockItemDTO.getDescription());
        stockItem.setCategory(stockItemCategory.get());
        stockItem.setSubCategory(stockItemSubCategory.get());
        stockItem.setMaterials(stockItemDTO.getMaterials());
        stockItem.setConstantStock(stockItemDTO.isConstantStock());
        stockItem.setStockQty(stockItemDTO.getStockQty());
        stockItem.setRestockQty(stockItemDTO.getRestockQty());

        return stockItem;
    }

}
