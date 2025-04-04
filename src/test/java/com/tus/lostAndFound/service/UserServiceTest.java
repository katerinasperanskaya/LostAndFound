package com.tus.lostAndFound.service;

import com.tus.lostAndFound.dto.UserDTO;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user = new User(1L, "Alice", "alice@email.com", "123", "pass");
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "Bob", "bob@email.com", "456", "pass");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getName());
    }

    @Test
    void testGetUserByEmail() {
        User user = new User(2L, "Charlie", "charlie@email.com", "789", "pass");
        when(userRepo.findByEmail("charlie@email.com")).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUserByEmail("charlie@email.com");

        assertTrue(result.isPresent());
        assertEquals("Charlie", result.get().getName());
    }

    @Test
    void testCreateUser_Success() {
        User user = new User(null, "Dana", "dana@email.com", "000", "plainPass");

        when(userRepo.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plainPass")).thenReturn("hashedPass");
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User created = userService.createUser(user);

        assertEquals("hashedPass", created.getPassword());
        verify(userRepo).save(user);
    }

    @Test
    void testCreateUser_EmailExists() {
        User user = new User(null, "Eve", "eve@email.com", "111", "pass");

        when(userRepo.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        User existing = new User(userId, "Frank", "frank@old.com", "000", "pass");
        UserDTO updated = new UserDTO(userId, "Frank Updated", "frank@new.com", "999");

        when(userRepo.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserDTO result = userService.updateUser(userId, updated);

        assertEquals("Frank Updated", result.getName());
        assertEquals("frank@new.com", result.getEmail());
        assertEquals("999", result.getPhone());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepo.findById(404L)).thenReturn(Optional.empty());

        UserDTO updated = new UserDTO(404L, "Ghost", "ghost@email.com", "000");

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(404L, updated));
    }

    @Test
    void testDeleteUser_Exists() {
        when(userRepo.existsById(1L)).thenReturn(true);
        doNothing().when(userRepo).deleteById(1L);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepo).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotExists() {
        when(userRepo.existsById(2L)).thenReturn(false);

        boolean result = userService.deleteUser(2L);

        assertFalse(result);
        verify(userRepo, never()).deleteById(anyLong());
    }
}
