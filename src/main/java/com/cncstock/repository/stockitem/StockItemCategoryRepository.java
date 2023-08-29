package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.StockItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemCategoryRepository extends JpaRepository<StockItemCategory, Long> {
    StockItemCategory findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);

}
