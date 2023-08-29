package com.cncstock.repository.stockitem;

import com.cncstock.model.entity.stockitem.StockItemCategory;
import com.cncstock.model.entity.stockitem.StockItemSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockItemSubCategoryRepository extends JpaRepository<StockItemSubCategory, Long> {

    boolean existsBySubCategoryName(String subCategoryName);
    List<StockItemSubCategory> findByStockItemCategory_Id(Long categoryId);


    boolean existsBySubCategoryNameAndStockItemCategory(String subCategoryName, StockItemCategory stockItemCategory);
}
