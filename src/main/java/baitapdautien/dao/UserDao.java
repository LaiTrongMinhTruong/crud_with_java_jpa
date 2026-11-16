package baitapdautien.dao;

import java.util.List;

import baitapdautien.models.User;

public interface UserDao {
	User get(String username);

	User findById(int id);

	List<User> findAll();

	void insert(User user);

	void update(User user);

	void delete(int id);

	boolean checkExistEmail(String email);

	boolean checkExistUsername(String username);

	boolean checkExistPhone(String phone);

	User findByEmail(String email);

	User findByCode(String code);

	User findByUsernameOrEmail(String keyword);
}
