package com.cncstock.controller.stockitem;

import com.cncstock.model.entity.stockitem.VendingTransaction;
import com.cncstock.repository.stockitem.VendingTransactionProjection;
import com.cncstock.repository.stockitem.VendingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/transactions")
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

    @GetMapping("/{stockItemId}")
    public List<VendingTransactionProjection> getVendingTransactionsByStockItemId(@PathVariable Long stockItemId) {
        return vendingTransactionRepository.findByStockItemId(stockItemId);
    }

}
