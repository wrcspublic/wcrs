package com.wcrs.dao;

import java.util.List;

import com.wcrs.dao.base.BaseJpaDao;
import com.wcrs.entity.User;

public class UserDao extends BaseJpaDao {
	public User findById(String id) {
		return this.find(User.class, id);
	}
	
	public List<User> getUserList(int start, int max) {
		StringBuilder sb = new StringBuilder("from User u where 1 = 1");
		return this.queryByWhere(User.class, sb.toString(), null, start, max);
	}
}
