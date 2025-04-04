package com.tus.lostAndFound.service;

import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.FoundItemStatus;
import com.tus.lostAndFound.repo.FoundItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoundItemServiceTest {

    @Mock
    private FoundItemRepo foundItemRepo;

    @InjectMocks
    private FoundItemService foundItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFoundItem() {
        FoundItem item = new FoundItem();
        when(foundItemRepo.save(any(FoundItem.class))).thenReturn(item);

        FoundItem savedItem = foundItemService.addFoundItem(item);

        assertEquals(FoundItemStatus.UNCLAIMED, savedItem.getStatus());
        verify(foundItemRepo, times(1)).save(item);
    }

    @Test
    void testGetFoundItemById() {
        FoundItem item = new FoundItem();
        item.setId(1L);
        when(foundItemRepo.findById(1L)).thenReturn(Optional.of(item));

        Optional<FoundItem> result = foundItemService.getFoundItemById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetAllFoundItems() {
        FoundItem item1 = new FoundItem();
        FoundItem item2 = new FoundItem();
        List<FoundItem> mockList = Arrays.asList(item1, item2);

        when(foundItemRepo.findAll()).thenReturn(mockList);

        List<FoundItem> result = foundItemService.getAllFoundItems();

        assertEquals(2, result.size());
    }

    @Test
    void testGetFoundItemsByStatus() {
        FoundItem item = new FoundItem();
        item.setStatus(FoundItemStatus.UNCLAIMED);

        when(foundItemRepo.findByStatus(FoundItemStatus.UNCLAIMED))
            .thenReturn(List.of(item));

        List<FoundItem> result = foundItemService.getFoundItemsByStatus(FoundItemStatus.UNCLAIMED);

        assertEquals(1, result.size());
        assertEquals(FoundItemStatus.UNCLAIMED, result.get(0).getStatus());
    }
    
    @Test
    void testGetFoundItemsByUser() {
        FoundItem item = new FoundItem();
        item.setUserId(1L);

        when(foundItemRepo.findByUserId(1L)).thenReturn(List.of(item));

        List<FoundItem> result = foundItemService.getFoundItemsByUser(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }

    @Test
    void testGetFoundItemsByCategory() {
        FoundItem item = new FoundItem();
        item.setCategory("Electronics");

        when(foundItemRepo.findByCategory("Electronics")).thenReturn(List.of(item));

        List<FoundItem> result = foundItemService.getFoundItemsByCategory("Electronics");

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
    }

//    @Test
//    void testGetFoundItemsByLocation() {
//        FoundItem item = new FoundItem();
//        item.setLocation("Library");
//
//        when(foundItemRepo.findByLocation("Library")).thenReturn(List.of(item));
//
//        List<FoundItem> result = foundItemService.getFoundItemsByLocation("Library");
//
//        assertEquals(1, result.size());
//        assertEquals("Library", result.get(0).getLocation());
//    }

    @Test
    void testUpdateFoundItemStatus() {
        FoundItem item = new FoundItem();
        item.setId(1L);
        item.setStatus(FoundItemStatus.UNCLAIMED);

        when(foundItemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(foundItemRepo.save(any(FoundItem.class))).thenReturn(item);

        Optional<FoundItem> updated = foundItemService.updateFoundItemStatus(1L, FoundItemStatus.CLAIMED);

        assertTrue(updated.isPresent());
        assertEquals(FoundItemStatus.CLAIMED, updated.get().getStatus());
    }
    
    @Test
    void testUpdateFoundItemStatus_ItemNotFound() {
        Long id = 99L;

        when(foundItemRepo.findById(id)).thenReturn(Optional.empty());

        Optional<FoundItem> result = foundItemService.updateFoundItemStatus(id, FoundItemStatus.CLAIMED);

        assertTrue(result.isEmpty());
        verify(foundItemRepo, never()).save(any());
    }


    @Test
    void testDeleteFoundItem() {
        Long itemId = 1L;

        when(foundItemRepo.existsById(itemId)).thenReturn(true);
        doNothing().when(foundItemRepo).deleteById(itemId);

        boolean deleted = foundItemService.deleteFoundItem(itemId);

        assertTrue(deleted);
        verify(foundItemRepo, times(1)).deleteById(itemId);
    }
    
    @Test
    void testDeleteFoundItem_NotFound() {
        when(foundItemRepo.existsById(999L)).thenReturn(false);

        boolean deleted = foundItemService.deleteFoundItem(999L);

        assertFalse(deleted);
        verify(foundItemRepo, never()).deleteById(anyLong());
    }



}

