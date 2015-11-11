package com.wcrs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wcrs.entity.base.BaseEntity;

@Entity
@Table(name="user")
public class User extends BaseEntity {

	private static final long serialVersionUID = -8234813674963865104L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;

	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
