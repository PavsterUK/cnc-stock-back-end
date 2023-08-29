package com.cncstock.repository;

import com.cncstock.model.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
}
