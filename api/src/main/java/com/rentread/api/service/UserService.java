package com.rentread.api.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.rentread.api.dto.LoginDto;
import com.rentread.api.dto.RegisterDto;
import com.rentread.api.dto.UpdateUserDto;
import com.rentread.api.dto.UserResponse;
import com.rentread.api.entity.User;

public interface UserService {
	public void register(RegisterDto registerDto);
	public UserResponse login(LoginDto loginDto);
	public UserResponse getProfile();
	public void deleteUser(Long userId);
	public List<UserResponse> getAllUsers();
	void updateProfile(UpdateUserDto updateUserDto);
	
	//internal use only
	public UserDetails loadUserByEmail(final String email);
	public User findByEmail(String email);
	//public UserDetails loadUserById(Long id);
	public User findById(Long id);
	public User getUser();
}
