package com.cncstock.service;

import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.repository.stockitem.LowStockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class LowStockService {

    private final LowStockItemRepository lowStockItemRepository;

    private final StockItemService stockItemService;

    @Autowired
    public LowStockService(LowStockItemRepository lowStockItemRepository, StockItemService stockItemService) {
        this.lowStockItemRepository = lowStockItemRepository;
        this.stockItemService = stockItemService;
    }

    @Transactional
    public List<LowStockItem> updateWithMostRecentItems() {
        List<StockItem> incomingLowStockItemsList = stockItemService.getLowStockItems();
        List<LowStockItem> existingLowStockItemsList = lowStockItemRepository.findAll();

        // Convert to Sets for efficient operations
        Set<StockItem> incomingLowStockItemsSet = new HashSet<>(incomingLowStockItemsList);
        Set<StockItem> existingStockItemsSet = existingLowStockItemsList.stream()
                .map(LowStockItem::getStockItem)
                .collect(Collectors.toSet());

        // Determine items to remove (existing but not in incoming)
        Set<LowStockItem> itemsToRemove = existingLowStockItemsList.stream()
                .filter(item -> !incomingLowStockItemsSet.contains(item.getStockItem()))
                .collect(Collectors.toSet());

        // Determine incoming items that are already in existing to avoid re-adding them
        incomingLowStockItemsList.removeIf(existingStockItemsSet::contains);

        // Remove items from the existing list/database
        if (!itemsToRemove.isEmpty()) {
            lowStockItemRepository.deleteAllInBatch(itemsToRemove);
        }

        // Prepare new low stock items to add
        Date todaysDate = new Date(System.currentTimeMillis());
        List<LowStockItem> itemsToAdd = incomingLowStockItemsList.stream()
                .map(stockItem -> new LowStockItem(stockItem, todaysDate, false, null))
                .collect(Collectors.toList());

        // Add new items to the database
        if (!itemsToAdd.isEmpty()) {
            lowStockItemRepository.saveAll(itemsToAdd);
        }

        // Return the latest state from the database
        return lowStockItemRepository.findAll();
    }

    public LowStockItem toggleOrderDate(Long id) {
        return lowStockItemRepository.findById(id).map(lowStockItem -> {
            Date prevOrderDate = lowStockItem.getOrderDate();
            if (prevOrderDate == null) {
                Date todayDate = new Date(System.currentTimeMillis());
                lowStockItem.setOrderDate(todayDate);
                lowStockItem.setOrdered(true);

            } else {

                lowStockItem.setOrderDate(null);
                lowStockItem.setOrdered(false);
            }
            return lowStockItemRepository.save(lowStockItem);
        }).orElseThrow(() -> new EntityNotFoundException("LowStockItem not found with id " + id));
    }


}
