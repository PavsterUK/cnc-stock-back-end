package com.cncstock.model.entity.stockitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "STOCK_SUB_CATEGORIES")
public class StockItemSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String subCategoryName;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id", nullable = false)
    @Getter
    @Setter
    private StockItemCategory stockItemCategory;

    public StockItemSubCategory(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

}
