package com.cncstock.model;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "STOCK")
public class StockItem {

    @Id
    @Getter
    @Setter
    @NonNull
    private int location;

    @Getter
    @Setter
    @NonNull
    private String title;

    @Getter
    @Setter
    private String brand;

    @Getter
    @Setter
    @NonNull
    private String supplier;

    @Getter
    @Setter
    @NonNull
    private int minQty ;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @NonNull
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


}
