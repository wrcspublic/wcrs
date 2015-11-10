package com.wrcs.session;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class SessionManager {
	private static final HashMap<String, HttpSession> sessionMap = new HashMap<String, HttpSession>(10);
	
}
