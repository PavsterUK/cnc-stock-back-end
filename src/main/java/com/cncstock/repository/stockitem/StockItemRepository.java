package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    List<StockItem> findByTitleContaining(String title);

    @Query("SELECT si FROM StockItem si JOIN FETCH si.category c JOIN FETCH si.subCategory sc")
    List<StockItem> findAllWithCategoryAndSubCategory();
}
