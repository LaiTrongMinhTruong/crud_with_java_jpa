package baitapdautien.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import baitapdautien.models.User;
import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.util.EmailUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {

	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String account = req.getParameter("account");

		if (account == null || account.trim().isEmpty()) {
			req.setAttribute("alert", "Vui lòng nhập tên tài khoản hoặc email của bạn!");
			req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
			return;
		}

		User user = userService.findByUsernameOrEmail(account.trim());
		if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
			String otp = EmailUtil.generateOtp();
			user.setCode(otp);
			userService.update(user);

			// Gửi mã OTP xác nhận quên mật khẩu qua mail
			EmailUtil.sendForgotPasswordOtp(user.getEmail(),
					user.getFullName() != null ? user.getFullName() : user.getUserName(), otp);

			HttpSession session = req.getSession(true);
			session.setAttribute("resetEmail", user.getEmail());

			resp.sendRedirect(req.getContextPath() + "/reset-password?email="
					+ URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8));
		} else {
			req.setAttribute("alert", "Không tìm thấy tài khoản hoặc email này trong hệ thống!");
			req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
		}
	}
}
