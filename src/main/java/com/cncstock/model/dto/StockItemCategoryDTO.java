package com.cncstock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class StockItemCategoryDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String categoryName;
}
