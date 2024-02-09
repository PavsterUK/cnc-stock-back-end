package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.model.entity.stockitem.VendingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendingTransactionRepository extends JpaRepository<VendingTransaction, Long> {

    @Query("SELECT vt FROM VendingTransaction vt WHERE vt.stockItem.id = :stockItemId")
    List<VendingTransaction> findByStockItemId(@Param("stockItemId") Long stockItemId);

    List<VendingTransaction> findByStockItem(StockItem stockItem);
}
