package com.cncstock.controller;

import com.cncstock.model.PurchaseRequest;
import com.cncstock.service.PurchaseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    @Autowired
    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @GetMapping("/purchase-requests")
    public ResponseEntity<List<PurchaseRequest>> getAllPurchaseRequests() {
        List<PurchaseRequest> purchaseRequests = purchaseRequestService.getAllRequests();
        if (purchaseRequests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(purchaseRequests, HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity<PurchaseRequest> createPurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) {
        PurchaseRequest createdRequest = purchaseRequestService.createPurchaseRequest(purchaseRequest);
        return ResponseEntity.ok(createdRequest);
    }

}
