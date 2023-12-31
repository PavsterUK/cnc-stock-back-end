package com.cncstock.model.entity.stockitem;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
@Getter
@Setter
@Table(name = "STOCK")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int location;

    private String title;

    private String brand;

    private String supplier;

    private int minQty ;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private StockItemCategory category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private StockItemSubCategory subCategory;

    private String[] materials;

    private boolean isConstantStock;

    private int stockQty;

    private int restockQty;

    @OneToMany(mappedBy = "stockItem", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<VendingTransaction> vendingTransactions;

}
