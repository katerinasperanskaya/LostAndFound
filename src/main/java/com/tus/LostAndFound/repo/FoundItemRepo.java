package com.tus.lostAndFound.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.FoundItemStatus;

@Repository
public interface FoundItemRepo extends JpaRepository<FoundItem, Long>{
	//  Find all found items by status (UNCLAIMED, CLAIMED)
    List<FoundItem> findByStatus(FoundItemStatus status);

    //  Find all found items by user ID
    List<FoundItem> findByUserId(Long userId);

    //  Find all found items in a specific category
    List<FoundItem> findByCategory(String category);

    //  Find found items by location (case-insensitive)
    List<FoundItem> findByLocationFoundIgnoreCase(String location);

}
