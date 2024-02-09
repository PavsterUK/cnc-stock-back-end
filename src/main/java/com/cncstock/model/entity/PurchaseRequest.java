package com.cncstock.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "PURCHASE_REQUESTS")
public class PurchaseRequest {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Date requestDate;

    @Getter
    @Setter
    @Column(length = 1000)
    private String requestBody;

    @Getter
    @Setter
    @NonNull
    private String requester;

    @Getter
    @Setter
    private boolean itemPurchased;

    @Getter
    @Setter
    private Long stockItemId;

}

