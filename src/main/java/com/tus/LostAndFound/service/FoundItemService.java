package com.tus.lostAndFound.service;


import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.FoundItemStatus;
import com.tus.lostAndFound.repo.FoundItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoundItemService {

    @Autowired
    private FoundItemRepo foundItemRepo;

    // Add a new found item
    public FoundItem addFoundItem(FoundItem foundItem) {
        foundItem.setStatus(FoundItemStatus.UNCLAIMED); // Default status when reported
        return foundItemRepo.save(foundItem);
    }

    // Get a found item by ID
    public Optional<FoundItem> getFoundItemById(Long id) {
        return foundItemRepo.findById(id);
    }

    // Get all found items
    public List<FoundItem> getAllFoundItems() {
        return foundItemRepo.findAll();
    }

    // Get found items by status (UNCLAIMED, CLAIMED)
    public List<FoundItem> getFoundItemsByStatus(FoundItemStatus status) {
        return foundItemRepo.findByStatus(status);
    }

    // Get found items by user ID
    public List<FoundItem> getFoundItemsByUser(Long userId) {
        return foundItemRepo.findByUserId(userId);
    }

    // Get found items by category
    public List<FoundItem> getFoundItemsByCategory(String category) {
        return foundItemRepo.findByCategory(category);
    }

    // Get found items by location
    public List<FoundItem> getFoundItemsByLocation(String location) {
        return foundItemRepo.findByLocationFoundIgnoreCase(location);
    }

    // Update found item status (UNCLAIMED → CLAIMED)
    public Optional<FoundItem> updateFoundItemStatus(Long id, FoundItemStatus newStatus) {
        Optional<FoundItem> foundItemOpt = foundItemRepo.findById(id);
        if (foundItemOpt.isPresent()) {
            FoundItem foundItem = foundItemOpt.get();
            foundItem.setStatus(newStatus);
            return Optional.of(foundItemRepo.save(foundItem));
        }
        return Optional.empty();
    }

    // Delete a found item by ID
    public boolean deleteFoundItem(Long id) {
        if (foundItemRepo.existsById(id)) {
            foundItemRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

