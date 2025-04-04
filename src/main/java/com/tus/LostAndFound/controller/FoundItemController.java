package com.tus.lostAndFound.controller;


import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.FoundItemStatus;
import com.tus.lostAndFound.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    // Add a new found item +
    @PostMapping
    public ResponseEntity<FoundItem> addFoundItem(@RequestBody FoundItem foundItem) {
        FoundItem savedItem = foundItemService.addFoundItem(foundItem);
        return ResponseEntity.ok(savedItem);
    }

    // Get all found items +
    @GetMapping
    public ResponseEntity<List<FoundItem>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemService.getAllFoundItems());
    }

    // Get a found item by ID +
    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable Long id) {
        Optional<FoundItem> foundItem = foundItemService.getFoundItemById(id);
        return foundItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get found items by status +
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByStatus(@PathVariable FoundItemStatus status) {
        return ResponseEntity.ok(foundItemService.getFoundItemsByStatus(status));
    }

    // Get found items by user ID +
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(foundItemService.getFoundItemsByUser(userId));
    }

    // Get found items by category +
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(foundItemService.getFoundItemsByCategory(category));
    }

    // Get found items by location  
//    @GetMapping("/location/{location}")
//    public ResponseEntity<List<FoundItem>> getFoundItemsByLocation(@PathVariable String location) {
//        return ResponseEntity.ok(foundItemService.getFoundItemsByLocation(location));
//    }

    // Update found item status (UNCLAIMED → CLAIMED)
    @PatchMapping("/{id}/status/{newStatus}")
    public ResponseEntity<FoundItem> updateFoundItemStatus(
            @PathVariable Long id, @PathVariable FoundItemStatus newStatus) {
        Optional<FoundItem> updatedItem = foundItemService.updateFoundItemStatus(id, newStatus);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a found item +
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteFoundItem(@PathVariable Long id) {
        boolean deleted = foundItemService.deleteFoundItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

