package com.rentread.api.serviceImpls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentread.api.dto.LoginDto;
import com.rentread.api.dto.RegisterDto;
import com.rentread.api.dto.UpdateUserDto;
import com.rentread.api.dto.UserResponse;
import com.rentread.api.entity.User;
import com.rentread.api.exceptions.BadRequestException;
import com.rentread.api.exceptions.NotFoundException;
import com.rentread.api.repository.UserRepository;
import com.rentread.api.security.CustomUserDetails;
import com.rentread.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        Optional<User> optional = userRepository.findByEmail(registerDto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setRole(registerDto.getRole());

        userRepository.save(user);
        log.info("User registered successfully with email: {}", user.getEmail());
    }

    @Override
    public UserResponse login(LoginDto loginDto) {
        // Manually create the token and authenticate using an injected AuthenticationManager bean if needed
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        log.info("Login request received: {}", email);

        // Perform manual authentication here without depending on CustomAuthenticationManager
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userOpt.get();
        log.info("Login successful for user: {}", email);
        return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null);
    }


    @Override
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        return CustomUserDetails.create(user);
    }

//    @Override
//    public UserDetails loadUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
//        return CustomUserDetails.create(user);
//    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                return findById(getPrincipal(authentication).getId());
            } catch (ClassCastException | NotFoundException e) {
                log.warn("[BASIC_AUTH] User details not found!");
                throw new BadCredentialsException("bad_credentials");
            }
        } else {
            log.warn("[BASIC_AUTH] User not authenticated!");
            throw new BadCredentialsException("bad_credentials");
        }
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private CustomUserDetails getPrincipal(Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateProfile(UpdateUserDto updateUserDto) {
        User user = getUser();
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());

        userRepository.save(user);
        log.info("User profile updated successfully for user: {}", user.getEmail());
    }

	@Override
	public UserResponse getProfile() {
		User user = getUser();
		return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found for given id"));
		userRepository.delete(user);
 	}
}
