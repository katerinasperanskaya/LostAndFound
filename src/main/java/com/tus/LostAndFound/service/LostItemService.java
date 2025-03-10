package com.tus.lostAndFound.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.lostAndFound.model.LostItem;
import com.tus.lostAndFound.model.LostItemStatus;
import com.tus.lostAndFound.repo.LostItemRepo;

@Service
public class LostItemService {
	   @Autowired
	    private LostItemRepo lostItemRepo;

	    //  Add a new lost item
	    public LostItem addLostItem(LostItem lostItem) {
	        lostItem.setStatus(LostItemStatus.OPEN); // Default status when reported
	        return lostItemRepo.save(lostItem);
	    }

	    //  Get a lost item by ID
	    public Optional<LostItem> getLostItemById(Long id) {
	        return lostItemRepo.findById(id);
	    }

	    //  Get all lost items
	    public List<LostItem> getAllLostItems() {
	        return lostItemRepo.findAll();
	    }

	    //  Get lost items by status (OPEN, FOUND, CLAIMED)
	    public List<LostItem> getLostItemsByStatus(LostItemStatus status) {
	        return lostItemRepo.findByStatus(status);
	    }

	    //  Get lost items by user ID
	    public List<LostItem> getLostItemsByUser(Long userId) {
	        return lostItemRepo.findByUserId(userId);
	    }

	    //  Get lost items by category
	    public List<LostItem> getLostItemsByCategory(String category) {
	        return lostItemRepo.findByCategory(category);
	    }

	    //  Get lost items by location
	    public List<LostItem> getLostItemsByLocation(String location) {
	        return lostItemRepo.findByLocationLostIgnoreCase(location);
	    }

	    //  Update lost item status (e.g., from OPEN → FOUND → CLAIMED)
	    public Optional<LostItem> updateLostItemStatus(Long id, LostItemStatus newStatus) {
	        Optional<LostItem> lostItemOpt = lostItemRepo.findById(id);
	        if (lostItemOpt.isPresent()) {
	            LostItem lostItem = lostItemOpt.get();
	            lostItem.setStatus(newStatus);
	            return Optional.of(lostItemRepo.save(lostItem));
	        }
	        return Optional.empty();
	    }

	    //  Delete a lost item by ID
	    public boolean deleteLostItem(Long id) {
	        if (lostItemRepo.existsById(id)) {
	            lostItemRepo.deleteById(id);
	            return true;
	        }
	        return false;
	    }

}
