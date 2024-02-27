package com.cncstock.repository.stockitem;
import com.cncstock.model.entity.stockitem.VendingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendingTransactionRepository extends JpaRepository<VendingTransaction, Long> {

    List<VendingTransactionProjection> findByStockItemId(Long stockItemId);

}
