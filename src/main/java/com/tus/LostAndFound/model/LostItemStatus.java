package com.tus.lostAndFound.model;

public enum LostItemStatus {
    OPEN, FOUND, CLAIMED
}

//OPEN: The item is reported as lost but not yet found. It's when a user first reports a lost item.
//FOUND: Someone has found the lost item, but it hasn't been claimed yet. When a user reports they found the item but haven't given it to the owner.
//CLAIMED:  The owner has claimed the lost item and it is no longer available. When the original owner verifies and takes back their lost item.
