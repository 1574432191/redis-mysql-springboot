package com.gjq.mapper;

import java.util.Map;

import com.gjq.pojo.User;

public interface UserMapper {
	boolean addUsers(User user) throws Exception;
	Map findByUsername(String username) throws Exception;
	Integer updateUser(User user) throws Exception;

}
