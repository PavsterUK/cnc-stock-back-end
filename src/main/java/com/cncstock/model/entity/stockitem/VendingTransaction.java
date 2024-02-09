package com.cncstock.model.entity.stockitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "VENDING_TRANSACTIONS")
public class VendingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private int vendQty;

    @Getter
    @Setter
    private Date vendDate;

    @ManyToOne()
    @JoinColumn(name = "stock_item_id", nullable = false)
    @Getter
    @Setter
    private StockItem stockItem;
}
