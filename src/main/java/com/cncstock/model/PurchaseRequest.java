package com.cncstock.model;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "PURCHASE_REQUESTS")
public class PurchaseRequest {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    private String requestDate;

    @Getter
    @Setter
    private String requestBody;

    @Getter
    @Setter
    private String requester;

    @Getter
    @Setter
    private boolean itemPurchased;



}

