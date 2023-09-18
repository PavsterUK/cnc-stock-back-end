package com.cncstock.listener;

import com.cncstock.event.StockItemUpdateEvent;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.model.entity.stockitem.VendingTransaction;
import com.cncstock.repository.stockitem.StockItemRepository;
import com.cncstock.repository.stockitem.VendingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StockItemUpdateListener implements ApplicationListener<StockItemUpdateEvent> {

    private final VendingTransactionRepository vendingTransactionRepository;
    private final StockItemRepository stockItemRepository;
    @Autowired
    public StockItemUpdateListener(VendingTransactionRepository vendingTransactionRepository, StockItemRepository stockItemRepository) {
        this.vendingTransactionRepository = vendingTransactionRepository;
        this.stockItemRepository = stockItemRepository;
    }

    @Override
    public void onApplicationEvent(StockItemUpdateEvent event) {
        StockItem stockItem = event.getUpdatedItem();
        int prevStockQty = event.getPrevStockQty();
        StockItem originalStockItem = stockItemRepository.findById(stockItem.getId()).orElse(null);
        System.out.println(prevStockQty);
        if (originalStockItem != null && stockItem.getStockQty() < prevStockQty) {
            int vendQty = prevStockQty - stockItem.getStockQty(); // Quantity taken
            addVendingTransaction(vendQty, stockItem);
        }
    }

    private void addVendingTransaction(int vendQty, StockItem stockItem) {
        VendingTransaction vendingTransaction = new VendingTransaction();
        vendingTransaction.setStockItem(stockItem);
        vendingTransaction.setVendDate(new Date());
        vendingTransaction.setVendQty(vendQty);
        vendingTransactionRepository.save(vendingTransaction);
    }
}
