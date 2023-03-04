package com.lcomputerstudy.testmvc.service;

import java.util.ArrayList;

import com.lcomputerstudy.testmvc.dao.UserDAO;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.User;

public class UserService {
	
	private static UserService service = null;
	private static UserDAO dao = null;
	
	private UserService() {
		
	}
	
	public static UserService getInstance() {
		if(service == null) {
			service = new UserService();
			dao = UserDAO.getInstance();
		}
		return service;
	}
	
	public ArrayList<User> getUsers(Pagination pagination) {
		return dao.getUsers(pagination);
	}

	public void insertUser(User user) {
		dao.insertUser(user);
	}

	public User detailUser(User user) {		//01-19 
		return dao.detailUser(user);
	}
	
	public User deleteUser(User user) {		//01-19
		return dao.deleteUser(user);
	}

	public void updateUser(User user) {		//01-19
		dao.updateUser(user);
	}

	public int getUsersCount() {
		return dao.getUsersCount();
	}
	
	public User loginUser(String id, String pw) {
		return dao.loginUser(id,pw);
	}
	
	public void adminOnUser(User user) {	// u_level 1 -> 9
		dao.adminOnUser(user);
	}
	
	public void adminOffUser(User user) {	// u_level 9 -> 1
		dao.adminOffUser(user);
	}
}