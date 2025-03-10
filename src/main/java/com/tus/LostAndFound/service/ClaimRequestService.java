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

    
    public ClaimRequest createClaim(ClaimRequest claimRequest) {
        claimRequest.setCreatedAt(LocalDateTime.now());
        claimRequest.setUpdatedAt(LocalDateTime.now());
        claimRequest.setStatus(ClaimStatus.PENDING);
        if (claimRequest.getRequestType() == ClaimType.CLAIM_FOUND) {
            // Check if the user has already claimed this found item
            if (claimRequestRepo.existsByUserIdAndFoundItemId(claimRequest.getUserId(), claimRequest.getFoundItemId())) {
                throw new IllegalArgumentException("You have already made a claim for this found item");
            }
        }

        if (claimRequest.getRequestType() == ClaimType.CLAIM_LOST) {
            // Check if the user has already claimed this lost item
            if (claimRequestRepo.existsByUserIdAndLostItemId(claimRequest.getUserId(), claimRequest.getLostItemId())) {
                throw new IllegalArgumentException("You have already made a claim for this lost item");
            }
        }

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
