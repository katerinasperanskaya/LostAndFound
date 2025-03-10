package com.tus.lostAndFound.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.lostAndFound.model.LostItem;
import com.tus.lostAndFound.model.LostItemStatus;

@Repository
public interface LostItemRepo extends JpaRepository<LostItem, Long>{
	
	//  Find all lost items by status (OPEN, FOUND, CLAIMED)
    List<LostItem> findByStatus(LostItemStatus status);

    //  Find all lost items by user ID
    List<LostItem> findByUserId(Long userId);

    //  Find all lost items in a specific category
    List<LostItem> findByCategory(String category);

    //  Find lost items by location (case-insensitive)
    List<LostItem> findByLocationLostIgnoreCase(String location);
}
