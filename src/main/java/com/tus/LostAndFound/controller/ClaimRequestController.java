package com.tus.lostAndFound.controller;


import com.tus.lostAndFound.model.ClaimRequest;
import com.tus.lostAndFound.model.ClaimStatus;
import com.tus.lostAndFound.model.ClaimType;
import com.tus.lostAndFound.service.ClaimRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
public class ClaimRequestController {

    @Autowired
    private ClaimRequestService claimRequestService;

    @GetMapping		// + ?
    public List<ClaimRequest> getAllClaims() {
        return claimRequestService.getAllClaims();
    }
    
    @GetMapping("/type/{type}")
    public List<ClaimRequest> getClaimsByType(@PathVariable ClaimType type) {
        return claimRequestService.getClaimsByType(type);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClaimRequest> getClaimById(@PathVariable Long id) {
        Optional<ClaimRequest> claim = claimRequestService.getClaimById(id);
        return claim.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<ClaimRequest> getClaimsByUser(@PathVariable Long userId) {
        return claimRequestService.getClaimsByUser(userId);
    }

    @GetMapping("/status/{status}")
    public List<ClaimRequest> getClaimsByStatus(@PathVariable ClaimStatus status) {
        return claimRequestService.getClaimsByStatus(status);
    }

    @PostMapping		// +
    public ResponseEntity<ClaimRequest> createClaim(@RequestBody ClaimRequest claimRequest) {
        ClaimRequest createdClaim = claimRequestService.createClaim(claimRequest);
        return ResponseEntity.ok(createdClaim);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ClaimRequest> updateClaimStatus(@PathVariable Long id, @RequestParam ClaimStatus status) {
        ClaimRequest updatedClaim = claimRequestService.updateClaimStatus(id, status);
        if (updatedClaim != null) {
            return ResponseEntity.ok(updatedClaim);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        boolean deleted = claimRequestService.deleteClaim(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

