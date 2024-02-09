package com.cncstock.service;

import com.cncstock.event.StockItemUpdateEvent;
import com.cncstock.exception.CategoryNotFoundException;
import com.cncstock.exception.SubCategoryNotFoundException;
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

    public StockItem save(StockItem stockItem) {
        checkRequiredFields(stockItem);
        return stockItemRepository.save(stockItem);
    }

    public StockItem updateStockItem(Long id, StockItem updatedStockItem) {
        Optional<StockItem> stockItemOptional = stockItemRepository.findById(id);
        if (stockItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Item with the specified id not found.");
        }

        StockItem existingStockItem = stockItemOptional.get();

        checkRequiredFields(updatedStockItem);

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

    private void checkRequiredFields(StockItem stockItem) {
        if (stockItem.getTitle() == null || stockItem.getTitle().isEmpty()) {
            throw new IllegalArgumentException("title is a required field and cannot be empty.");
        }
        if (stockItem.getSupplier() == null || stockItem.getSupplier().isEmpty()) {
            throw new IllegalArgumentException("supplier is a required field and cannot be empty.");
        }
        if (stockItem.getCategory() == null) {
            throw new IllegalArgumentException("category is a required field and cannot be null.");
        }
        if (stockItem.getMinQty() < 0) {
            throw new IllegalArgumentException("minQty must be a non-negative value.");
        }
        if (stockItem.getLocation() < 1) {
            throw new IllegalArgumentException("location must be assigned to a stock item.");
        }
    }


    private void updateProperties(StockItem existingStockItem, StockItem updatedStockItem) {
        int prevQty = existingStockItem.getStockQty();

        if (updatedStockItem.getTitle() != null && !updatedStockItem.getTitle().equals(existingStockItem.getTitle())) {
            existingStockItem.setTitle(updatedStockItem.getTitle());
        }

        if (updatedStockItem.getSupplier() != null && !updatedStockItem.getSupplier().equals(existingStockItem.getSupplier())) {
            existingStockItem.setSupplier(updatedStockItem.getSupplier());
        }

        if (updatedStockItem.getSupplierItemCode() != null && !updatedStockItem.getSupplierItemCode().equals(existingStockItem.getSupplierItemCode())) {
            existingStockItem.setSupplierItemCode(updatedStockItem.getSupplierItemCode());
        }

        if (updatedStockItem.getCategory() != null && !updatedStockItem.getCategory().equals(existingStockItem.getCategory())) {
            existingStockItem.setCategory(updatedStockItem.getCategory());
        }

        if (updatedStockItem.getSubCategory() != null && !updatedStockItem.getSubCategory().equals(existingStockItem.getSubCategory())) {
            existingStockItem.setSubCategory(updatedStockItem.getSubCategory());
        }

        if (updatedStockItem.getMinQty() != existingStockItem.getMinQty()) {
            existingStockItem.setMinQty(updatedStockItem.getMinQty());
        }

        if (updatedStockItem.getBrand() != null && !updatedStockItem.getBrand().equals(existingStockItem.getBrand())) {
            existingStockItem.setBrand(updatedStockItem.getBrand());
        }

        if (updatedStockItem.getDescription() != null && !updatedStockItem.getDescription().equals(existingStockItem.getDescription())) {
            existingStockItem.setDescription(updatedStockItem.getDescription());
        }

        if (updatedStockItem.getLocation() != existingStockItem.getLocation()) {
            existingStockItem.setLocation(updatedStockItem.getLocation());
        }

        if (updatedStockItem.isConstantStock() != existingStockItem.isConstantStock()) {
            existingStockItem.setConstantStock(updatedStockItem.isConstantStock());
        }

        if (updatedStockItem.getStockQty() != existingStockItem.getStockQty()) {
            existingStockItem.setStockQty(updatedStockItem.getStockQty());
        }

        if (updatedStockItem.getRestockQty() != existingStockItem.getRestockQty()) {
            existingStockItem.setRestockQty(updatedStockItem.getRestockQty());
        }

        if (updatedStockItem.getMaterials() != null && !Arrays.equals(updatedStockItem.getMaterials(), existingStockItem.getMaterials())) {
            existingStockItem.setMaterials(updatedStockItem.getMaterials());
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
                        item.getSupplierItemCode(),
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

    public StockItemDTO toDTO(StockItem item) {
        return new StockItemDTO(
                item.getId(),
                item.getLocation(),
                item.getTitle(),
                item.getBrand(),
                item.getSupplier(),
                item.getSupplierItemCode(),
                item.getMinQty(),
                item.getDescription(),
                item.getCategory().getId(),
                item.getCategory().getCategoryName(),
                item.getSubCategory().getId(),
                item.getSubCategory().getSubCategoryName(),
                item.getMaterials(),
                item.isConstantStock(),
                item.getStockQty(),
                item.getRestockQty());
    }


    public StockItem toStockItem(StockItemDTO stockItemDTO) {
        Optional<StockItemCategory> stockItemCategory = stockItemCategoryRepository.findById(stockItemDTO.getCategoryId());
        Optional<StockItemSubCategory> stockItemSubCategory = stockItemSubCategoryRepository.findById(stockItemDTO.getSubCategoryId());

        if (stockItemCategory.isEmpty()) {
            throw new CategoryNotFoundException(stockItemDTO.getCategoryName());
        }

        if (stockItemSubCategory.isEmpty()) {
            throw new SubCategoryNotFoundException(stockItemDTO.getSubCategoryName());
        }

        StockItem stockItem = new StockItem();
        stockItem.setId(stockItemDTO.getId());
        stockItem.setLocation(stockItemDTO.getLocation());
        stockItem.setTitle(stockItemDTO.getTitle());
        stockItem.setBrand(stockItemDTO.getBrand());
        stockItem.setSupplier(stockItemDTO.getSupplier());
        stockItem.setSupplierItemCode(stockItemDTO.getSupplierItemCode());
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
