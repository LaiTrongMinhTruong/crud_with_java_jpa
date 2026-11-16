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
@WebServlet(urlPatterns = { "/verify-otp", "/resend-otp" })
public class VerifyOtpController extends HttpServlet {

	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();

		if ("/resend-otp".equals(servletPath)) {
			handleResendOtp(req, resp);
			return;
		}

		String email = req.getParameter("email");
		if (email == null || email.trim().isEmpty()) {
			HttpSession session = req.getSession(false);
			if (session != null && session.getAttribute("verifyEmail") != null) {
				email = (String) session.getAttribute("verifyEmail");
			}
		}

		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String email = req.getParameter("email");
		String otp = req.getParameter("otp");

		if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
			req.setAttribute("alert", "Vui lòng nhập đầy đủ email và mã OTP!");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
			return;
		}

		boolean isActivated = userService.activateAccount(email.trim(), otp.trim());
		if (isActivated) {
			HttpSession session = req.getSession();
			session.removeAttribute("verifyEmail");
			session.setAttribute("successMessage", "Tài khoản của bạn đã được kích hoạt thành công! Hãy đăng nhập ngay.");
			resp.sendRedirect(req.getContextPath() + "/login?activated=true");
		} else {
			req.setAttribute("alert", "Mã xác nhận OTP không chính xác hoặc đã hết hạn!");
			req.setAttribute("email", email.trim());
			req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
		}
	}

	private void handleResendOtp(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("email");
		if (email == null || email.trim().isEmpty()) {
			HttpSession session = req.getSession(false);
			if (session != null) {
				email = (String) session.getAttribute("verifyEmail");
			}
		}

		if (email != null && !email.trim().isEmpty()) {
			User user = userService.findByEmail(email.trim());
			if (user != null && user.getStatus() == 0) {
				String newOtp = EmailUtil.generateOtp();
				user.setCode(newOtp);
				userService.update(user);
				EmailUtil.sendActivationOtp(user.getEmail(),
						user.getFullName() != null ? user.getFullName() : user.getUserName(), newOtp);

				req.setAttribute("message", "Mã OTP mới đã được gửi lại vào email của bạn!");
			}
		}
		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
	}
}
