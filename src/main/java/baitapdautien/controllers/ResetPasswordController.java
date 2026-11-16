package baitapdautien.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {

	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		if (email == null || email.trim().isEmpty()) {
			HttpSession session = req.getSession(false);
			if (session != null && session.getAttribute("resetEmail") != null) {
				email = (String) session.getAttribute("resetEmail");
			}
		}

		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String email = req.getParameter("email");
		String otp = req.getParameter("otp");
		String newPassword = req.getParameter("newPassword");
		String confirmPassword = req.getParameter("confirmPassword");

		if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty() || newPassword == null
				|| newPassword.trim().isEmpty()) {
			req.setAttribute("alert", "Vui lòng điền đầy đủ tất cả các trường!");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			req.setAttribute("alert", "Mật khẩu xác nhận không khớp với mật khẩu mới!");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
			return;
		}

		boolean isSuccess = userService.resetPassword(email.trim(), otp.trim(), newPassword.trim());

		if (isSuccess) {
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.removeAttribute("resetEmail");
				session.setAttribute("successMessage", "Đổi mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
			}
			resp.sendRedirect(req.getContextPath() + "/login?resetSuccess=true");
		} else {
			req.setAttribute("alert", "Mã xác nhận OTP không đúng hoặc đã hết hạn!");
			req.setAttribute("email", email.trim());
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
		}
	}
}
