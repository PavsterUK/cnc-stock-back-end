package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.LowStockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface LowStockItemRepository extends JpaRepository<LowStockItem, Long> {

    @Query("SELECT DISTINCT l.stockItem.supplier FROM LowStockItem l WHERE DATE(l.dateAdded) = CURRENT_DATE")
    Optional<List<String>> findUniqueSuppliersForItemsAddedToday();

    @Query("SELECT l FROM LowStockItem l WHERE DATE(l.dateAdded) = CURRENT_DATE")
    Optional<List<LowStockItem>> findAllItemsAddedToday();

    @Modifying
    @Transactional
    @Query("update LowStockItem l set l.orderDate = :orderDate where l.id = :id")
    int updateOrderDateById(@Param("orderDate") Date orderDate, @Param("id") Long id);

}
