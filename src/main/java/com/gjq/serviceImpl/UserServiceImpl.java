package com.gjq.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjq.mapper.UserMapper;
import com.gjq.pojo.User;
import com.gjq.service.UserService;
import com.gjq.util.RedisUtil;
@Service("uService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/*
	 * 存入用户个人信息，若mysql存入无误，则存入redis并设置过期时间
	 */
	public boolean addUsers(User user) throws Exception {
		if (userMapper.addUsers(user)) {
			String key = user.getUsername()+"个人信息";
			Map map = new HashMap<>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("username", user.getUsername());
			map.put("age", user.getAge());
			map.put("email", user.getEmail());
			map.put("phoneNumber", user.getPhoneNumber());
			redisUtil.hmset(key, map, 300);
			return true;
			
		}else {

			return false;
		}
		
	}

	/*查询用户信息，优先从redis中查找
	 * redis中没有就从数据库中查找
	 */
	public Map findByUsername(String username) throws Exception {
			if (redisUtil.hmget(username+"个人信息").size()!=0) {
				return redisUtil.hmget(username+"个人信息");
			//redis中没有查询到用户的信息，就从数据库中查找
			}else {
				Map map = userMapper.findByUsername(username);
				//然后将查找到的信息保存到redis中，并设置过期时间，这里设置300s
				redisUtil.hmset(username+"个人信息", map, 300);
				return map;
			}
		}

	/**
	 *  修改用户个人信息，MySQL修改成功后修改redis中的信息
	 *  若redis中信息过期则重新存入
	 *  返回更改后的个人信息。
	 */
	public Map updateUser(User user) throws Exception {
		if (userMapper.updateUser(user)>=0){
            if(redisUtil.hmget(user.getUsername()+"个人信息").size()!=0){
                Map map = redisUtil.hmget(user.getName()+"个人信息");
                if (!map.get("username").equals(user.getUsername())){
                    redisUtil.hset(user.getName()+"个人信息","username",user.getUsername());
                }
                if (!map.get("age").equals(user.getAge())){
                    redisUtil.hset(user.getName()+"个人信息","age",user.getAge());
                }
                if (!map.get("email").equals(user.getEmail())){
                    redisUtil.hset(user.getName()+"个人信息","email",user.getEmail());
                }
                if (!map.get("phoneNumber").equals(user.getPhoneNumber())){
                    redisUtil.hset(user.getName()+"个人信息","phoneNumber",user.getPhoneNumber());
                }
                redisUtil.expire(user.getName()+"个人信息",300);
            }else {
                Map map = userMapper.findByUsername(user.getName());
                redisUtil.hmset(user.getName()+"个人信息",map,300);
            }
           
            return redisUtil.hmget(user.getName()+"个人信息");
        }else {
            Map map = new HashMap();
            map.put("errro","更改MYSQL个人信息表错误");
            return map;
        }

	}
	

}
