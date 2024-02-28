package com.cncstock.model.entity.stockitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "VENDING_TRANSACTIONS")
public class VendingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int vendQty;

    private Date vendDate;

    @ManyToOne()
    @JoinColumn(name = "stock_item_id", nullable = false)
    @JsonIgnore
    private StockItem stockItem;
}
