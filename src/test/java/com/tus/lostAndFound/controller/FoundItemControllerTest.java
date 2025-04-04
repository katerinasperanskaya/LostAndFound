
package com.tus.lostAndFound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.lostAndFound.model.FoundItem;
import com.tus.lostAndFound.model.FoundItemStatus;
import com.tus.lostAndFound.service.FoundItemService;
import com.tus.lostAndFound.util.JwtUtil;
import com.tus.lostAndFound.util.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoundItemController.class)
class FoundItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoundItemService foundItemService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private FoundItem sampleItem;

    @BeforeEach
    void setUp() {
        sampleItem = new FoundItem();
        sampleItem.setId(1L);
        sampleItem.setCategory("Electronics");
        sampleItem.setStatus(FoundItemStatus.UNCLAIMED);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testAddFoundItem() throws Exception {
        Mockito.when(foundItemService.addFoundItem(any())).thenReturn(sampleItem);

        mockMvc.perform(post("/api/found-items")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetAllFoundItems() throws Exception {
        Mockito.when(foundItemService.getAllFoundItems()).thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/found-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetFoundItemById_Found() throws Exception {
        Mockito.when(foundItemService.getFoundItemById(1L)).thenReturn(Optional.of(sampleItem));

        mockMvc.perform(get("/api/found-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetFoundItemById_NotFound() throws Exception {
        Mockito.when(foundItemService.getFoundItemById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/found-items/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetFoundItemsByStatus() throws Exception {
        Mockito.when(foundItemService.getFoundItemsByStatus(FoundItemStatus.UNCLAIMED))
                .thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/found-items/status/UNCLAIMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UNCLAIMED"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetFoundItemsByUser() throws Exception {
        Mockito.when(foundItemService.getFoundItemsByUser(1L)).thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/found-items/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetFoundItemsByCategory() throws Exception {
        Mockito.when(foundItemService.getFoundItemsByCategory("Electronics"))
                .thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/found-items/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Electronics"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testUpdateFoundItemStatus_Found() throws Exception {
        sampleItem.setStatus(FoundItemStatus.CLAIMED);
        Mockito.when(foundItemService.updateFoundItemStatus(1L, FoundItemStatus.CLAIMED))
               .thenReturn(Optional.of(sampleItem));

        mockMvc.perform(patch("/api/found-items/1/status/CLAIMED")
                .with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLAIMED"));
    }



    @Test
    @WithMockUser(authorities = {"USER"})
    void testUpdateFoundItemStatus_NotFound() throws Exception {
        Mockito.when(foundItemService.updateFoundItemStatus(999L, FoundItemStatus.CLAIMED))
               .thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/found-items/999/status/CLAIMED")
                .with(csrf())) 
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteFoundItem_Success() throws Exception {
        Mockito.when(foundItemService.deleteFoundItem(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/found-items/1")
                .with(csrf()))
               .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteFoundItem_NotFound() throws Exception {
        Mockito.when(foundItemService.deleteFoundItem(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/found-items/999")
                .with(csrf())) 
               .andExpect(status().isNotFound());
    }

}
