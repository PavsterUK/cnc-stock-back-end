package com.cncstock.model.entity.stockitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@Table(name = "STOCK_SUB_CATEGORIES")
public class StockItemSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String subCategoryName;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id", nullable = false)
    private StockItemCategory stockItemCategory;

    public StockItemSubCategory(StockItemCategory stockItemCategory, String subCategoryName) {
        this.stockItemCategory = stockItemCategory;
        this.subCategoryName = subCategoryName;
    }
}
