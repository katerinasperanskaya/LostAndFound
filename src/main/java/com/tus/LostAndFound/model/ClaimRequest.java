package com.tus.lostAndFound.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_requests")
public class ClaimRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "lost_item_id") // Store only the lost item's ID
    private Long lostItemId;

    @Column(name = "found_item_id") // Store only the found item's ID
    private Long foundItemId;

    @Enumerated(EnumType.STRING)
    private ClaimType requestType;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ClaimRequest() {}

    public ClaimRequest(Long userId, Long lostItemId, Long foundItemId, ClaimType requestType) {
        this.userId = userId;
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.requestType = requestType;
        this.status = ClaimStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLostItemId() { return lostItemId; }
    public void setLostItemId(Long lostItemId) { this.lostItemId = lostItemId; }

    public Long getFoundItemId() { return foundItemId; }
    public void setFoundItemId(Long foundItemId) { this.foundItemId = foundItemId; }

    public ClaimType getRequestType() { return requestType; }
    public void setRequestType(ClaimType requestType) { this.requestType = requestType; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
