package com.cncstock.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "STOCK")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private int location;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String brand;

    @Getter
    @Setter
    private String supplier;

    @Getter
    @Setter
    private int minQty ;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private String[] materials;

    @Getter
    @Setter
    private boolean isConstantStock;

    @Getter
    @Setter
    private int stockQty;

    @Getter
    @Setter
    private int restockQty;

    @OneToMany(mappedBy = "stockItem")
    private List<VendingTransaction> vendingTransactions;


}
