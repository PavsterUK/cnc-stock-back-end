package com.cncstock.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STOCK")
public class StockItem {

    @Id
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
    private int minQty;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private String[] materials;

    public StockItem() {
    }


}
