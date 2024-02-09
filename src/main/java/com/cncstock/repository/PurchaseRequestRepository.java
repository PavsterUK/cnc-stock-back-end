package com.cncstock.repository;

import com.cncstock.model.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

    Optional<List<PurchaseRequest>> findByStockItemIdAndItemPurchasedFalse(Long stockItemId);
}
