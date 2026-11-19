package baitapdautien.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.util.Constant;
import baitapdautien.util.EmailUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {

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
					session.setAttribute(Constant.SESSION_USERNAME, cookie.getValue());
					resp.sendRedirect(req.getContextPath() + "/waiting");
					return;
				}
			}
		}
		req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String fullname = req.getParameter("fullname");
		String phone = req.getParameter("phone");

		UserService service = new UserServiceImpl();
		String alertMsg = "";

		String u = username != null ? username.trim() : "";
		String p = password != null ? password.trim() : "";
		String e = email != null ? email.trim() : "";
		String f = fullname != null ? fullname.trim() : "";
		String ph = phone != null ? phone.trim() : "";

		req.setAttribute("username", u);
		req.setAttribute("fullname", f);
		req.setAttribute("email", e);
		req.setAttribute("phone", ph);

		if (u.isEmpty() || p.isEmpty() || e.isEmpty() || f.isEmpty() || ph.isEmpty()) {
			alertMsg = "Vui lòng nhập đầy đủ tất cả thông tin bắt buộc!";
		} else if (!u.matches("^[a-zA-Z0-9_]{3,50}$")) {
			alertMsg = "Tài khoản phải từ 3–50 ký tự và chỉ gồm chữ cái, số, dấu gạch dưới!";
		} else if (f.length() < 2 || f.length() > 100) {
			alertMsg = "Họ tên phải từ 2–100 ký tự!";
		} else if (!e.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			alertMsg = "Địa chỉ email không đúng định dạng!";
		} else if (!ph.matches("^0[0-9]{9,10}$")) {
			alertMsg = "Số điện thoại phải bắt đầu bằng số 0 và gồm 10–11 chữ số!";
		} else if (p.length() < 6) {
			alertMsg = "Mật khẩu phải có ít nhất 6 ký tự!";
		}

		if (!alertMsg.isEmpty()) {
			req.setAttribute("alert", alertMsg);
			req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
			return;
		}

		if (service.checkExistEmail(email.trim())) {
			alertMsg = "Email đã tồn tại trên hệ thống!";
			req.setAttribute("alert", alertMsg);
			req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
			return;
		}

		if (service.checkExistUsername(username.trim())) {
			alertMsg = "Tên tài khoản đã tồn tại trên hệ thống!";
			req.setAttribute("alert", alertMsg);
			req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
			return;
		}

		String otp = EmailUtil.generateOtp();
		boolean isSuccess = service.registerWithOtp(username.trim(), password.trim(), email.trim(),
				fullname != null ? fullname.trim() : "", phone != null ? phone.trim() : "", otp);

		if (isSuccess) {
			HttpSession session = req.getSession(true);
			session.setAttribute("verifyEmail", email.trim());
			resp.sendRedirect(req.getContextPath() + "/verify-otp?email="
					+ URLEncoder.encode(email.trim(), StandardCharsets.UTF_8));
		} else {
			alertMsg = "Đăng ký thất bại hoặc không thể gửi mã OTP qua email! Vui lòng kiểm tra lại địa chỉ email.";
			req.setAttribute("alert", alertMsg);
			req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
		}
	}
}
