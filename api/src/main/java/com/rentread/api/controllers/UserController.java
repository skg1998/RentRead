package com.rentread.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentread.api.dto.AppResponse;
import com.rentread.api.dto.LoginDto;
import com.rentread.api.dto.RegisterDto;
import com.rentread.api.dto.SuccessResponse;
import com.rentread.api.dto.UpdateUserDto;
import com.rentread.api.dto.UserResponse;
import com.rentread.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PostMapping("/register")
    public ResponseEntity<AppResponse> register(@Valid @RequestBody RegisterDto registerDto) {
		userService.register(registerDto);
        return responseMaker("user register successfully", HttpStatus.CREATED, null);
    }
	
	@PostMapping("/login")
    public ResponseEntity<AppResponse> login(@Valid @RequestBody LoginDto loginDto) {
		UserResponse userResponse = userService.login(loginDto);
        return responseMaker("user login successfully", HttpStatus.OK, userResponse);
    }
	
	@PutMapping("/update-profile")
    public ResponseEntity<AppResponse> updateProfile(@Valid @RequestBody UpdateUserDto updateUserDto) {
		userService.updateProfile(updateUserDto);
        return responseMaker("update user profile successfully", HttpStatus.OK, null);
    }
	
	@GetMapping("/me")
    public ResponseEntity<AppResponse> myProfile() {
		UserResponse userResponse =  userService.getProfile();
        return responseMaker("user profile fetch successfully", HttpStatus.OK, userResponse);
    }
	
	@GetMapping("/")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse> users() {
		List<UserResponse> userResponses =  userService.getAllUsers();
        return responseMaker("user all user successfully", HttpStatus.OK, userResponses);
    }
	
	@DeleteMapping("/{userId}")
    public ResponseEntity<AppResponse> deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
        return responseMaker("delete user profile successfully", HttpStatus.OK, null);
    }
    
    @SuppressWarnings("rawtypes")
	private <T> ResponseEntity<AppResponse> responseMaker(String message, HttpStatus httpStatus, T data){
		SuccessResponse successResponse = new SuccessResponse<>(message, data);
        AppResponse response = new AppResponse(true, successResponse, null);
        return response.toResponseEntity(httpStatus);
	}

}
