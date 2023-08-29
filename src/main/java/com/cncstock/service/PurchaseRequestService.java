package com.cncstock.service;

import com.cncstock.model.entity.PurchaseRequest;
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
        if(purchaseRequest.getRequester().isEmpty()) {
            throw new IllegalArgumentException("Requester cannot be anonymos");
        }
       return purchaseRequestRepository.save(purchaseRequest);
    }

    public PurchaseRequest updatePurchaseRequest(Long id, PurchaseRequest updatedRequest) {
        PurchaseRequest existingRequest = purchaseRequestRepository.findById(id).orElse(null);
        if (existingRequest == null) {
            throw new IllegalArgumentException("Item with the provided id doesn't exists.");
        }

        existingRequest.setRequestDate(updatedRequest.getRequestDate());
        existingRequest.setRequestBody(updatedRequest.getRequestBody());
        existingRequest.setRequester(updatedRequest.getRequester());
        existingRequest.setItemPurchased(updatedRequest.isItemPurchased());

        return purchaseRequestRepository.save(existingRequest);
    }

    public boolean deletePurchaseRequest(Long id) {
        PurchaseRequest existingRequest = purchaseRequestRepository.findById(id).orElse(null);
        if (existingRequest == null) {
            throw new IllegalArgumentException("Item with the provided id doesn't exists.");
        }

        purchaseRequestRepository.delete(existingRequest);
        return true;
    }


}
