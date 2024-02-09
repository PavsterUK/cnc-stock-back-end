package com.cncstock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StockItemSubCategoryDTO {

    private Long id;

    private String subCategoryName;

    private Long categoryId;
}
