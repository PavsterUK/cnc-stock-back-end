package com.cncstock.service;

import com.cncstock.model.PurchaseRequest;
import com.cncstock.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    public List<PurchaseRequest> getAllRequests(){
        return purchaseRequestRepository.findAll();
    }

    public PurchaseRequest createPurchaseRequest(PurchaseRequest purchaseRequest) {
       return purchaseRequestRepository.save(purchaseRequest);
    }


}
