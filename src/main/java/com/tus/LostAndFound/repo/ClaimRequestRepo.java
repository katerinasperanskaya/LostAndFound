package com.tus.lostAndFound.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.lostAndFound.model.ClaimRequest;
import com.tus.lostAndFound.model.ClaimStatus;
import com.tus.lostAndFound.model.ClaimType;

@Repository
public interface ClaimRequestRepo extends JpaRepository<ClaimRequest, Long>{
	
    // Find all claim requests by user ID
    List<ClaimRequest> findByUserId(Long userId);
    
    // Find all claim requests by status
    List<ClaimRequest> findByStatus(ClaimStatus status);
    
    // Find all claim requests by item ID
    List<ClaimRequest> findByLostItemId(Long itemId);
    
    // Find all claim requests by found item ID
    List<ClaimRequest> findByFoundItemId(Long foundItemId);
    
    // retrieve all lost or found claims
    List<ClaimRequest> findByRequestType(ClaimType requestType);


}

