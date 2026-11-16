package baitapdautien.service.impl;

import java.util.List;

import baitapdautien.dao.UserDao;
import baitapdautien.dao.impl.UserDaoImpl;
import baitapdautien.models.User;
import baitapdautien.service.UserService;
import baitapdautien.util.EmailUtil;
import baitapdautien.util.PasswordUtil;

public class UserServiceImpl implements UserService {
	UserDao userDao = new UserDaoImpl();

	@Override
	public User login(String username, String password) {
		User user = this.get(username);
		if (user == null) {
			user = userDao.findByEmail(username);
		}

		if (user != null) {
			// Kiểm tra mật khẩu (hỗ trợ BCrypt và mật khẩu cũ)
			if (PasswordUtil.checkPassword(password, user.getPassWord())) {
				// Tự động nâng cấp sang mã hóa BCrypt nếu là mật khẩu thường chưa hash
				if (!user.getPassWord().startsWith("$2a$") && !user.getPassWord().startsWith("$2b$")) {
					user.setPassWord(PasswordUtil.hashPassword(password));
					userDao.update(user);
				}
				return user;
			}
		}
		return null;
	}

	@Override
	public User get(String username) {
		return userDao.get(username);
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void insert(User user) {
		userDao.insert(user);
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}

	@Override
	public void delete(int id) {
		userDao.delete(id);
	}

	@Override
	public boolean register(String username, String password, String email, String fullname, String phone) {
		if (userDao.checkExistUsername(username) || userDao.checkExistEmail(email)) {
			return false;
		}
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		String hashedPassword = PasswordUtil.hashPassword(password);

		User user = new User(email, username, fullname, hashedPassword, null, 5, phone, date);
		user.setStatus(1); // Đã kích hoạt trực tiếp nếu không yêu cầu OTP
		userDao.insert(user);
		return true;
	}

	@Override
	public boolean registerWithOtp(String username, String password, String email, String fullname, String phone,
			String otp) {
		if (userDao.checkExistUsername(username) || userDao.checkExistEmail(email)) {
			return false;
		}
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		String hashedPassword = PasswordUtil.hashPassword(password);

		User user = new User(email, username, fullname, hashedPassword, null, 5, phone, date);
		user.setStatus(0); // 0 = Chờ xác thực OTP
		user.setCode(otp);
		userDao.insert(user);

		// Gửi email OTP
		EmailUtil.sendActivationOtp(email, fullname != null ? fullname : username, otp);
		return true;
	}

	@Override
	public boolean activateAccount(String email, String otp) {
		if (email == null || otp == null) {
			return false;
		}
		User user = userDao.findByEmail(email.trim());
		if (user == null) {
			user = userDao.get(email.trim());
		}

		if (user != null && otp.trim().equals(user.getCode())) {
			user.setStatus(1); // Kích hoạt thành công
			user.setCode(null); // Xóa OTP
			userDao.update(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean sendResetPasswordOtp(String usernameOrEmail) {
		if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
			return false;
		}
		User user = userDao.findByUsernameOrEmail(usernameOrEmail.trim());
		if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
			String otp = EmailUtil.generateOtp();
			user.setCode(otp);
			userDao.update(user);
			return EmailUtil.sendForgotPasswordOtp(user.getEmail(),
					user.getFullName() != null ? user.getFullName() : user.getUserName(), otp);
		}
		return false;
	}

	@Override
	public boolean resetPassword(String email, String otp, String newPassword) {
		if (email == null || otp == null || newPassword == null || newPassword.trim().isEmpty()) {
			return false;
		}
		User user = userDao.findByUsernameOrEmail(email.trim());
		if (user != null && otp.trim().equals(user.getCode())) {
			user.setPassWord(PasswordUtil.hashPassword(newPassword.trim()));
			user.setCode(null);
			userDao.update(user);
			return true;
		}
		return false;
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public User findByUsernameOrEmail(String keyword) {
		return userDao.findByUsernameOrEmail(keyword);
	}

	@Override
	public boolean checkExistEmail(String email) {
		return userDao.checkExistEmail(email);
	}

	@Override
	public boolean checkExistUsername(String username) {
		return userDao.checkExistUsername(username);
	}

	@Override
	public boolean checkExistPhone(String phone) {
		return userDao.checkExistPhone(phone);
	}
}
