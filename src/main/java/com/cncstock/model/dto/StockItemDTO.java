package com.cncstock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class StockItemDTO {

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
    private String categoryName;

    @Getter
    @Setter
    private String subCategoryName;

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
}
