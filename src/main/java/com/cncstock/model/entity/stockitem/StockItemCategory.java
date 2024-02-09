package com.cncstock.model.entity.stockitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
@Getter
@Setter
@Table(name = "STOCK_CATEGORIES")
public class StockItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "stockItemCategory", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<StockItemSubCategory> stockItemSubCategories  = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<StockItem> stockItems = new ArrayList<>();

    public StockItemCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
