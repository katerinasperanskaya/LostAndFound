package com.tus.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.lostAndFound.model.ClaimRequest;
import com.tus.lostAndFound.model.ClaimStatus;
import com.tus.lostAndFound.model.ClaimType;
import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.LostItem;
import com.tus.lostAndFound.repo.ClaimRequestRepo;
import com.tus.lostAndFound.repo.FoundItemRepo;
import com.tus.lostAndFound.repo.LostItemRepo;

@Service
public class ClaimRequestService {
    
    @Autowired
    private ClaimRequestRepo claimRequestRepo;

    @Autowired
    private LostItemRepo lostItemRepo;  // Added repository for LostItem

    @Autowired
    private FoundItemRepo foundItemRepo; // Added repository for FoundItem

    public List<ClaimRequest> getAllClaims() {
        return claimRequestRepo.findAll();
    }

    public Optional<ClaimRequest> getClaimById(Long id) {
        return claimRequestRepo.findById(id);
    }

    public List<ClaimRequest> getClaimsByUser(Long userId) {
        return claimRequestRepo.findByUserId(userId);
    }

    public List<ClaimRequest> getClaimsByStatus(ClaimStatus status) {
        return claimRequestRepo.findByStatus(status);
    }
    
    public List<ClaimRequest> getClaimsByType(ClaimType type) {
        return claimRequestRepo.findByRequestType(type);
    }

//    public ClaimRequest createClaim(ClaimRequest claimRequest) {
//        claimRequest.setCreatedAt(LocalDateTime.now());
//        claimRequest.setUpdatedAt(LocalDateTime.now());
//        claimRequest.setStatus(ClaimStatus.PENDING);
//
//        // Fetch LostItem from database if provided
//        if (claimRequest.getLostItem() != null) {
//            Long lostItemId = claimRequest.getLostItem().getId();
//            LostItem lostItem = lostItemRepo.findById(lostItemId)
//                    .orElseThrow(() -> new IllegalArgumentException("Lost item not found with ID: " + lostItemId));
//            claimRequest.setLostItem(lostItem);
//        }
//
//        // Fetch FoundItem from database if provided
//        if (claimRequest.getFoundItem() != null) {
//            Long foundItemId = claimRequest.getFoundItem().getId();
//            FoundItem foundItem = foundItemRepo.findById(foundItemId)
//                    .orElseThrow(() -> new IllegalArgumentException("Found item not found with ID: " + foundItemId));
//            claimRequest.setFoundItem(foundItem);
//        }
//
//        return claimRequestRepo.save(claimRequest);
//    }
    
    public ClaimRequest createClaim(ClaimRequest claimRequest) {
        claimRequest.setCreatedAt(LocalDateTime.now());
        claimRequest.setUpdatedAt(LocalDateTime.now());
        claimRequest.setStatus(ClaimStatus.PENDING);
        return claimRequestRepo.save(claimRequest);
    }
    
    

    public ClaimRequest updateClaimStatus(Long id, ClaimStatus status) {
        Optional<ClaimRequest> optionalClaim = claimRequestRepo.findById(id);
        if (optionalClaim.isPresent()) {
            ClaimRequest claim = optionalClaim.get();
            claim.setStatus(status);
            claim.setUpdatedAt(LocalDateTime.now());
            return claimRequestRepo.save(claim);
        }
        return null;
    }

    public boolean deleteClaim(Long id) {
        if (claimRequestRepo.existsById(id)) {
            claimRequestRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
