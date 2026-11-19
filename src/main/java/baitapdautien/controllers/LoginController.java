package baitapdautien.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import baitapdautien.models.User;
import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.util.Constant;
import baitapdautien.util.PasswordUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("account") != null) {
			resp.sendRedirect(req.getContextPath() + "/waiting");
			return;
		}

		// Check cookie
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constant.COOKIE_REMEMBER)) {
					session = req.getSession(true);
					session.setAttribute("username", cookie.getValue());
					resp.sendRedirect(req.getContextPath() + "/waiting");
					return;
				}
			}
		}

		if (session != null && session.getAttribute("successMessage") != null) {
			req.setAttribute("successMessage", session.getAttribute("successMessage"));
			session.removeAttribute("successMessage");
		} else if ("true".equals(req.getParameter("activated"))) {
			req.setAttribute("successMessage", "Tài khoản đã kích hoạt thành công! Vui lòng đăng nhập.");
		} else if ("true".equals(req.getParameter("resetSuccess"))) {
			req.setAttribute("successMessage", "Đổi mật khẩu thành công! Vui lòng đăng nhập bằng mật khẩu mới.");
		}

		req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		boolean isRememberMe = "on".equals(req.getParameter("remember"));

		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			req.setAttribute("username", username != null ? username.trim() : "");
			req.setAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng!");
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
			return;
		}
		if (username.trim().length() < 3 || password.trim().length() < 3) {
			req.setAttribute("username", username.trim());
			req.setAttribute("alert", "Tài khoản và mật khẩu phải có ít nhất 3 ký tự!");
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
			return;
		}

		UserService service = new UserServiceImpl();
		User existingUser = service.get(username.trim());
		if (existingUser == null) {
			existingUser = service.findByEmail(username.trim());
		}

		if (existingUser != null) {
			// Kiểm tra nếu tài khoản chưa được kích hoạt OTP
			if (existingUser.getStatus() == 0) {
				req.setAttribute("unactivatedEmail", existingUser.getEmail());
				req.setAttribute("alert", "Tài khoản của bạn chưa được kích hoạt! Vui lòng xác thực mã OTP gửi về email.");
				req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
				return;
			}
		}

		User user = service.login(username.trim(), password.trim());

		if (user != null) {
			HttpSession session = req.getSession(true);
			session.setAttribute("account", user);
			if (isRememberMe) {
				saveRemeberMe(resp, user.getUserName());
			}
			resp.sendRedirect(req.getContextPath() + "/waiting");
		} else {
			req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng!");
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
		}
	}

	private void saveRemeberMe(HttpServletResponse response, String username) {
		Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
		cookie.setMaxAge(30 * 60);
		response.addCookie(cookie);
	}
}
