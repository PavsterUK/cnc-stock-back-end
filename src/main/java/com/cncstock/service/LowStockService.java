package com.cncstock.service;

import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.repository.stockitem.LowStockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public List<LowStockItem> getAll() {
       return lowStockItemRepository.findAll();
    }

    public void updateLowStockDatabase() {
        List<StockItem> incomingLowStockItemsList = stockItemService.getLowStockItems();
        List<LowStockItem> existingLowStockItemsList = getAll();

        // Convert to Sets for efficient lookup and removal
        Set<StockItem> incomingLowStockItems = new HashSet<>(incomingLowStockItemsList);
        Set<StockItem> existingStockItems = existingLowStockItemsList.stream()
                .map(LowStockItem::getStockItem)
                .collect(Collectors.toSet());

        // Prepare a set for items to be removed
        Set<LowStockItem> itemsToRemove = existingLowStockItemsList.stream()
                .filter(item -> !incomingLowStockItems.contains(item.getStockItem()))
                .collect(Collectors.toSet());

        // Remove items that are no longer low stock
        existingLowStockItemsList.removeAll(itemsToRemove);
        // Optionally, if you need to update the database here, remove them from the database

        // Find items that are newly low stock
        incomingLowStockItems.removeAll(existingStockItems); // Remaining items in incomingLowStockItems are new low stock items

        // Convert the remaining incomingLowStockItems back to LowStockItem and save/update them in the database
        Date todaysDate = new Date(System.currentTimeMillis());
        List<LowStockItem> newLowStockItems = incomingLowStockItemsList.stream()
                .filter(incomingLowStockItems::contains) // Filter only new low stock items
                .map(stockItem -> new LowStockItem(null, stockItem, todaysDate, false, null)) // Assuming constructor usage
                .toList();

        // Save new low stock items to the database
        lowStockItemRepository.saveAll(newLowStockItems);
    }





}
