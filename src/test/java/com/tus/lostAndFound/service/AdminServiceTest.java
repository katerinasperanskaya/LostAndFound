package com.tus.lostAndFound.service;


import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.repo.AdminRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepo adminRepo;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAdmins() {
        Admin admin = new Admin(1L, "Admin1", "admin1@email.com", "123456","pass");
        when(adminRepo.findAll()).thenReturn(List.of(admin));

        List<Admin> result = adminService.getAllAdmins();

        assertEquals(1, result.size());
        assertEquals("Admin1", result.get(0).getName());
    }

    @Test
    void testGetAdminById() {
        Admin admin = new Admin(1L, "Admin2", "admin2@email.com", "123456", "pass");
        when(adminRepo.findById(1L)).thenReturn(Optional.of(admin));

        Optional<Admin> result = adminService.getAdminById(1L);

        assertTrue(result.isPresent());
        assertEquals("Admin2", result.get().getName());
    }

    @Test
    void testGetAdminByEmail() {
        Admin admin = new Admin(2L, "Admin3", "admin3@email.com", "123456","pass");
        when(adminRepo.findByEmail("admin3@email.com")).thenReturn(Optional.of(admin));

        Optional<Admin> result = adminService.getAdminByEmail("admin3@email.com");

        assertTrue(result.isPresent());
        assertEquals("Admin3", result.get().getName());
    }

    @Test
    void testCreateAdmin_Success() {
        Admin admin = new Admin(null, "Admin4", "admin4@email.com","123456", "pass");

        when(adminRepo.existsByEmail(admin.getEmail())).thenReturn(false);
        when(adminRepo.save(any(Admin.class))).thenAnswer(i -> i.getArgument(0));

        Admin result = adminService.createAdmin(admin);

        assertEquals("Admin4", result.getName());
        verify(adminRepo).save(admin);
    }

    @Test
    void testCreateAdmin_EmailExists() {
        Admin admin = new Admin(null, "Admin5", "admin5@email.com","123456", "pass");

        when(adminRepo.existsByEmail(admin.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> adminService.createAdmin(admin));
    }

    @Test
    void testUpdateAdmin_Success() {
        Long adminId = 1L;
        Admin existing = new Admin(adminId, "Admin6", "old@email.com","123456", "oldpass");
        Admin updated = new Admin(adminId, "Admin Updated", "new@email.com","123456", "newpass");

        when(adminRepo.findById(adminId)).thenReturn(Optional.of(existing));
        when(adminRepo.save(any(Admin.class))).thenAnswer(i -> i.getArgument(0));

        Admin result = adminService.updateAdmin(adminId, updated);

        assertEquals("Admin Updated", result.getName());
        assertEquals("new@email.com", result.getEmail());
        assertEquals("newpass", result.getPassword());
    }

    @Test
    void testUpdateAdmin_NotFound() {
        when(adminRepo.findById(999L)).thenReturn(Optional.empty());
        Admin updated = new Admin(999L, "Ghost", "ghost@email.com","123456", "pass");

        assertThrows(IllegalArgumentException.class, () -> adminService.updateAdmin(999L, updated));
    }

    @Test
    void testDeleteAdmin_Exists() {
        when(adminRepo.existsById(1L)).thenReturn(true);
        doNothing().when(adminRepo).deleteById(1L);

        boolean result = adminService.deleteAdmin(1L);

        assertTrue(result);
        verify(adminRepo).deleteById(1L);
    }

    @Test
    void testDeleteAdmin_NotExists() {
        when(adminRepo.existsById(2L)).thenReturn(false);

        boolean result = adminService.deleteAdmin(2L);

        assertFalse(result);
        verify(adminRepo, never()).deleteById(anyLong());
    }
}

