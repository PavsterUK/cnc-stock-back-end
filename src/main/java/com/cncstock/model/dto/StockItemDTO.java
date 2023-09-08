package com.cncstock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockItemDTO {

    private Long id;

    private int location;

    private String title;

    private String brand;

    private String supplier;

    private int minQty ;

    private String description;

    private Long categoryId;

    private String categoryName;

    private Long subCategoryId;

    private String subCategoryName;

    private String[] materials;

    private boolean isConstantStock;

    private int stockQty;

    private int restockQty;
}
