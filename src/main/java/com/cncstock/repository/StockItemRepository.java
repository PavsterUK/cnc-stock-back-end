package com.cncstock.repository;

import com.cncstock.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    List<StockItem> findByTitleContaining(String title);
}
