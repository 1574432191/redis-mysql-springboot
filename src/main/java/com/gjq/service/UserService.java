package com.gjq.service;

import java.util.Map;

import com.gjq.pojo.User;

public interface UserService {
	boolean addUsers(User user) throws Exception;
	Map findByUsername(String username) throws Exception;
	Map updateUser(User user) throws Exception;

}
