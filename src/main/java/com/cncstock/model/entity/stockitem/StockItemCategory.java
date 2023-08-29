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
@Table(name = "STOCK_CATEGORIES")
public class StockItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String categoryName;

    @OneToMany(mappedBy = "stockItemCategory", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Getter
    @Setter
    private List<StockItemSubCategory> stockItemSubCategories  = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @Getter
    @Setter
    @ToString.Exclude
    private List<StockItem> stockItems = new ArrayList<>();

    public StockItemCategory(String categoryName) {
        this.categoryName = categoryName;
    }

}
