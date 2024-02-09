package com.cncstock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierStockItemsDto {

    private String supplier;
    private List<StockItemDTO> stockItemDTO;

}
