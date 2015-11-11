package com.wcrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wcrs.dao.UserDao;
import com.wcrs.entity.User;

@Component
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public void saveUser() {
		User u = new User();
		u.setAttributeValue("username", "XX");
		u.setAttributeValue("password", "*************");
		userDao.saveUser(u);
	}
}
