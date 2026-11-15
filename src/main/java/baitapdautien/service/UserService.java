package baitapdautien.service;

import java.util.List;

import baitapdautien.models.User;

public interface UserService {
	User login(String username, String password);

	User get(String username);

	User findById(int id);

	List<User> findAll();

	void insert(User user);

	void update(User user);

	void delete(int id);

	boolean register(String username, String password, String email, String fullname, String phone);

	boolean checkExistEmail(String email);

	boolean checkExistUsername(String username);

	boolean checkExistPhone(String phone);
}
