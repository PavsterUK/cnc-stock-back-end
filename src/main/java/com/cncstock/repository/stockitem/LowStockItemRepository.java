package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.LowStockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LowStockItemRepository extends JpaRepository<LowStockItem, Long> {

    @Query("SELECT DISTINCT l.stockItem.supplier FROM LowStockItem l WHERE DATE(l.dateAdded) = CURRENT_DATE")
    Optional<List<String>> findUniqueSuppliersForItemsAddedToday();

    @Query("SELECT l FROM LowStockItem l WHERE DATE(l.dateAdded) = CURRENT_DATE")
    Optional<List<LowStockItem>> findAllItemsAddedToday();

    // Custom query to find the most recent low stock item by StockItem ID
    @Query("SELECT l FROM LowStockItem l WHERE l.stockItem.id = :stockItemId " + "AND l.dateAdded = (SELECT MAX(ll.dateAdded) FROM LowStockItem ll WHERE ll.stockItem.id = :stockItemId)")
    Optional<LowStockItem> findMostRecentByStockItemId(@Param("stockItemId") Long stockItemId);
    boolean existsByStockItemId(Long stockItemId);
    @Query("SELECT l FROM LowStockItem l WHERE l.stockItem.stockQty >= l.stockItem.minQty")
    List<LowStockItem> findItemsNoLongerLow();

}
