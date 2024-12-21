package com.rentread.api.dto;


import com.rentread.api.enumeration.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
}
