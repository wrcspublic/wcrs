package com.wcrs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.wcrs.entity.base.BaseEntity;

@Entity
@Table(name="user")
public class User extends BaseEntity {

	private static final long serialVersionUID = -8234813674963865104L;

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
