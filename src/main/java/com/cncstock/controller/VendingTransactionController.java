package com.cncstock.controller;

import com.cncstock.model.VendingTransaction;
import com.cncstock.repository.VendingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class VendingTransactionController {


    private final VendingTransactionRepository vendingTransactionRepository;
    @Autowired
    public VendingTransactionController(VendingTransactionRepository vendingTransactionRepository) {
        this.vendingTransactionRepository = vendingTransactionRepository;
    }

    @PostMapping("/vending-transaction")
    public VendingTransaction createVendingTransaction(@RequestBody VendingTransaction vendingTransaction) {
        return vendingTransactionRepository.save(vendingTransaction);
    }

    @GetMapping("/by-stock-item/{stockItemId}")
    public List<VendingTransaction> getVendingTransactionsByStockItemId(@PathVariable Long stockItemId) {
        return vendingTransactionRepository.findByStockItemId(stockItemId);
    }


}
