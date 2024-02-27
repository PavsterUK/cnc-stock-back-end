package com.cncstock.event;

import com.cncstock.model.entity.stockitem.StockItem;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class StockItemUpdateEvent extends ApplicationEvent {
    private final StockItem updatedStockItem;
    @Getter
    private final int prevStockQty;

    public StockItemUpdateEvent(Object source, StockItem updatedStockItem, int prevStockQty) {
        super(source);
        this.updatedStockItem = updatedStockItem;
        this.prevStockQty = prevStockQty;
    }

    public StockItem getUpdatedItem() {
        return updatedStockItem;
    }
}
