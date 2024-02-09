package com.cncstock.model.entity.stockitem;

import lombok.*;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOW_STOCK_ITEMS")
public class LowStockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_item_id")
    private StockItem stockItem;

    private Date dateAdded;

    private Date dateArchived;

    private boolean purchased;

    private boolean archived;
}
