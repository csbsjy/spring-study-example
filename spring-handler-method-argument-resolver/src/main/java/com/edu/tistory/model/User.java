package com.edu.tistory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
	private String userId;
	private String userPassword;
	@Setter
	private UserType userType;
	
	public enum UserType {
		Manager, VIPMember, Member
	}
}

