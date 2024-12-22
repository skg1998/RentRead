package com.rentread.api.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rentread.api.dto.LoginDto;
import com.rentread.api.dto.RegisterDto;
import com.rentread.api.dto.UpdateUserDto;
import com.rentread.api.dto.UserResponse;
import com.rentread.api.entity.User;
import com.rentread.api.enumeration.Role;
import com.rentread.api.exceptions.BadRequestException;
import com.rentread.api.exceptions.NotFoundException;
import com.rentread.api.repository.UserRepository;
import com.rentread.api.security.CustomUserDetails;
import com.rentread.api.serviceImpls.UserServiceImpl;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto("test@example.com", "password", "John", "Doe", Role.USER);
        when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        userService.register(registerDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RegisterDto registerDto = new RegisterDto("test@example.com", "password", "John", "Doe", Role.USER);
        when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.register(registerDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        LoginDto loginDto = new LoginDto("test@example.com", "password");
        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

        UserResponse response = userService.login(loginDto);

        assertEquals(loginDto.getEmail(), response.getEmail());
        verify(userRepository, times(1)).findByEmail(loginDto.getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginDto loginDto = new LoginDto("test@example.com", "password");
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.login(loginDto));
    }

    @Test
    void testFindById_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponse> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }

    @Test
    void testUpdateProfile_Success() {
        UpdateUserDto updateUserDto = new UpdateUserDto("NewFirstName", "NewLastName");
        User user = new User();
        user.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(CustomUserDetails.create(user));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateProfile(updateUserDto);

        verify(userRepository, times(1)).save(user);
        assertEquals("NewFirstName", user.getFirstName());
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).delete(any(User.class));
    }
}
