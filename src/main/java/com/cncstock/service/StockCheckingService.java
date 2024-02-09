package com.cncstock.service;

import com.cncstock.model.entity.PurchaseRequest;
import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.repository.PurchaseRequestRepository;
import com.cncstock.repository.stockitem.LowStockItemRepository;
import com.cncstock.repository.stockitem.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockCheckingService {

    private final StockItemRepository stockItemRepository;
    private final LowStockItemRepository lowStockItemRepository;



    @Autowired
    public StockCheckingService(StockItemRepository stockItemRepository, LowStockItemRepository lowStockItemRepository) {
        this.stockItemRepository = stockItemRepository;
        this.lowStockItemRepository = lowStockItemRepository;

    }

    public void checkItemStock() {
        Optional<List<StockItem>> optionalLowStockItemList = stockItemRepository.findItemsWithLowStock();

        if(optionalLowStockItemList.isPresent()) {
            for(StockItem latestLowStockItem : optionalLowStockItemList.get()) {
                if ( !lowStockItemRepository.existsByStockItemId(latestLowStockItem.getId())  ) {
                    saveLowStockItem(latestLowStockItem);
                }
            }
        }
    }

    
    @Transactional
    private void saveLowStockItem(StockItem item) {
        LowStockItem newLowStockItem = new LowStockItem();
        newLowStockItem.setStockItem(item);
        newLowStockItem.setDateAdded(new Date(System.currentTimeMillis()));
        lowStockItemRepository.save(newLowStockItem);
    }

}




