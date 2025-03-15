package com.tus.lostAndFound.controller;


import com.tus.lostAndFound.model.LostItem;
import com.tus.lostAndFound.model.LostItemStatus;
import com.tus.lostAndFound.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    //  Create a new lost item +
    @PostMapping
    public ResponseEntity<LostItem> addLostItem(@RequestBody LostItem lostItem) {
        LostItem savedItem = lostItemService.addLostItem(lostItem);
        return ResponseEntity.ok(savedItem);
    }

    //  Get a lost item by ID +
    @GetMapping("/{id}")
    public ResponseEntity<LostItem> getLostItemById(@PathVariable Long id) {
        Optional<LostItem> lostItem = lostItemService.getLostItemById(id);
        return lostItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Get all lost items +
    @GetMapping
    public ResponseEntity<List<LostItem>> getAllLostItems() {
        return ResponseEntity.ok(lostItemService.getAllLostItems());
    }

    //  Get lost items by status (OPEN, FOUND, CLAIMED) +
    @GetMapping("/status/{status}") 
    public ResponseEntity<List<LostItem>> getLostItemsByStatus(@PathVariable LostItemStatus status) {
        return ResponseEntity.ok(lostItemService.getLostItemsByStatus(status));
    }

    //  Get lost items by user ID +
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LostItem>> getLostItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(lostItemService.getLostItemsByUser(userId));
    }

    //  Get lost items by category +
    @GetMapping("/category/{category}") 
    public ResponseEntity<List<LostItem>> getLostItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(lostItemService.getLostItemsByCategory(category));
    }

    //  Get lost items by location +
    @GetMapping("/location/{location}")
    public ResponseEntity<List<LostItem>> getLostItemsByLocation(@PathVariable String location) {
        return ResponseEntity.ok(lostItemService.getLostItemsByLocation(location));
    }

    //  Update lost item status (OPEN → FOUND → CLAIMED) + (pass update into url)
    @PatchMapping("/{id}/status/{newStatus}")
    public ResponseEntity<LostItem> updateLostItemStatus(@PathVariable Long id, @PathVariable LostItemStatus newStatus) {
        Optional<LostItem> updatedItem = lostItemService.updateLostItemStatus(id, newStatus);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Delete a lost item by ID +
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        boolean deleted = lostItemService.deleteLostItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

