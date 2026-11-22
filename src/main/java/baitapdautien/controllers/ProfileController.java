package baitapdautien.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import baitapdautien.models.User;
import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.util.CloudinaryUtil;
import baitapdautien.util.Constant;
import baitapdautien.util.EmailUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/profile", "/profile/verify-email", "/profile/resend-email-otp" })
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 2,
		maxFileSize = 1024 * 1024 * 10,
		maxRequestSize = 1024 * 1024 * 20)
public class ProfileController extends HttpServlet {

	private static final long OTP_TTL_MS = 10 * 60 * 1000L;
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final String PHONE_REGEX = "^0[0-9]{9,10}$";

	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		User sessionUser = (session != null) ? (User) session.getAttribute("account") : null;
		if (sessionUser == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		if ("true".equals(req.getParameter("cancelEmail"))) {
			clearPendingEmail(session);
			req.setAttribute("message", "Đã hủy yêu cầu thay đổi email.");
		}

		User user = userService.findById(sessionUser.getId());
		if (user != null) {
			session.setAttribute("account", user);
			req.setAttribute("user", user);
		} else {
			req.setAttribute("user", sessionUser);
		}

		req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession(false);
		User sessionUser = (session != null) ? (User) session.getAttribute("account") : null;
		if (sessionUser == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		User user = userService.findById(sessionUser.getId());
		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String servletPath = req.getServletPath();
		if ("/profile/verify-email".equals(servletPath)) {
			handleVerifyEmailOtp(req, resp, session, user);
			return;
		}
		if ("/profile/resend-email-otp".equals(servletPath)) {
			handleResendEmailOtp(req, resp, session, user);
			return;
		}

		handleUpdateProfile(req, resp, session, user);
	}

	private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp, HttpSession session, User user)
			throws ServletException, IOException {
		String fullname = req.getParameter("fullname");
		String phone = req.getParameter("phone");
		String newEmail = req.getParameter("email");

		String cleanFullname = fullname != null ? fullname.trim() : "";
		String cleanPhone = phone != null ? phone.trim() : "";
		String cleanEmail = newEmail != null ? newEmail.trim() : "";

		if (cleanFullname.length() < 2 || cleanFullname.length() > 100) {
			req.setAttribute("error", "Họ tên phải từ 2 đến 100 ký tự!");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}
		if (!cleanPhone.matches(PHONE_REGEX)) {
			req.setAttribute("error", "Số điện thoại phải bắt đầu bằng số 0 và gồm 10–11 chữ số!");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}
		if (!cleanEmail.matches(EMAIL_REGEX)) {
			req.setAttribute("error", "Địa chỉ email không đúng định dạng!");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		// Xử lý upload avatar (images) lên Cloudinary bằng multipart
		try {
			Part avatarPart = req.getPart("avatarFile");
			if (avatarPart != null && avatarPart.getSize() > 0 && avatarPart.getSubmittedFileName() != null
					&& !avatarPart.getSubmittedFileName().trim().isEmpty()) {
				String contentType = avatarPart.getContentType();
				if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
					req.setAttribute("error", "Vui lòng chỉ chọn file hình ảnh hợp lệ (JPG, PNG, WebP, GIF)!");
					req.setAttribute("user", user);
					req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
					return;
				}

				String avatarUrl = uploadAvatar(avatarPart);
				if (avatarUrl != null && !avatarUrl.isEmpty()) {
					user.setAvatar(avatarUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Có lỗi xảy ra khi tải ảnh đại diện lên: " + e.getMessage());
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		user.setFullName(cleanFullname);
		user.setPhone(cleanPhone);

		String currentEmail = user.getEmail() != null ? user.getEmail().trim() : "";
		boolean emailChanged = !cleanEmail.equalsIgnoreCase(currentEmail);

		if (!emailChanged) {
			userService.update(user);
			session.setAttribute("account", user);
			req.setAttribute("user", user);
			req.setAttribute("message", "Cập nhật thông tin hồ sơ thành công!");
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		// Kiểm tra email mới đã tồn tại ở tài khoản khác chưa
		if (userService.checkExistEmail(cleanEmail)) {
			// Lưu lại fullname, phone, avatar nếu đã thay đổi
			userService.update(user);
			session.setAttribute("account", user);
			req.setAttribute("user", user);
			req.setAttribute("error", "Email mới (" + cleanEmail + ") đã được sử dụng bởi tài khoản khác!");
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		// Lưu thông tin cơ bản trước, giữ email cũ cho tới khi xác nhận OTP thành công
		userService.update(user);
		session.setAttribute("account", user);

		String otp = EmailUtil.generateOtp();
		session.setAttribute("pendingEmail", cleanEmail);
		session.setAttribute("pendingEmailOtp", otp);
		session.setAttribute("pendingEmailOtpExpiry", System.currentTimeMillis() + OTP_TTL_MS);

		boolean sent = EmailUtil.sendUpdateEmailOtp(cleanEmail, cleanFullname, otp);
		req.setAttribute("user", user);
		if (sent) {
			req.setAttribute("message",
					"Đã lưu họ tên/SĐT/ảnh đại diện. Hệ thống đã gửi mã OTP đến email mới (" + cleanEmail
							+ "). Vui lòng nhập mã OTP bên dưới để hoàn tất cập nhật email!");
		} else {
			req.setAttribute("error", "Không thể gửi mã OTP đến email mới. Vui lòng kiểm tra lại cấu hình email hoặc thử lại!");
		}
		req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
	}

	private void handleVerifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session, User user)
			throws ServletException, IOException {
		String otpInput = req.getParameter("otp");
		String pendingEmail = (String) session.getAttribute("pendingEmail");
		String pendingOtp = (String) session.getAttribute("pendingEmailOtp");
		Long expiry = (Long) session.getAttribute("pendingEmailOtpExpiry");

		if (pendingEmail == null || pendingOtp == null || expiry == null) {
			req.setAttribute("error", "Không có yêu cầu cập nhật email nào đang chờ xác nhận.");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		if (System.currentTimeMillis() > expiry) {
			clearPendingEmail(session);
			req.setAttribute("error", "Mã OTP đã hết hạn (quá 10 phút). Vui lòng thực hiện cập nhật email lại!");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		if (otpInput == null || !otpInput.trim().equals(pendingOtp)) {
			req.setAttribute("error", "Mã OTP không chính xác! Vui lòng kiểm tra lại email " + pendingEmail + ".");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		if (userService.checkExistEmail(pendingEmail)) {
			clearPendingEmail(session);
			req.setAttribute("error", "Email " + pendingEmail + " vừa được tài khoản khác sử dụng!");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		user.setEmail(pendingEmail);
		userService.update(user);
		session.setAttribute("account", user);
		clearPendingEmail(session);

		req.setAttribute("user", user);
		req.setAttribute("message", "Xác thực OTP thành công! Email của bạn đã được cập nhật thành " + pendingEmail + ".");
		req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
	}

	private void handleResendEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session, User user)
			throws ServletException, IOException {
		String pendingEmail = (String) session.getAttribute("pendingEmail");
		if (pendingEmail == null || pendingEmail.trim().isEmpty()) {
			req.setAttribute("error", "Không tìm thấy email mới đang chờ xác nhận.");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		String otp = EmailUtil.generateOtp();
		session.setAttribute("pendingEmailOtp", otp);
		session.setAttribute("pendingEmailOtpExpiry", System.currentTimeMillis() + OTP_TTL_MS);

		boolean sent = EmailUtil.sendUpdateEmailOtp(pendingEmail, user.getFullName(), otp);
		req.setAttribute("user", user);
		if (sent) {
			req.setAttribute("message", "Mã OTP mới đã được gửi lại đến email " + pendingEmail + "!");
		} else {
			req.setAttribute("error", "Gửi lại mã OTP thất bại, vui lòng thử lại sau.");
		}
		req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
	}

	private String uploadAvatar(Part avatarPart) throws IOException {
		try {
			String uploadedUrl = CloudinaryUtil.uploadImage(avatarPart, "users/avatars");
			if (uploadedUrl != null && !uploadedUrl.isEmpty()) {
				return uploadedUrl;
			}
		} catch (Exception ex) {
			System.err.println("[CLOUDINARY] Upload avatar thất bại, chuyển sang lưu cục bộ: " + ex.getMessage());
		}

		File uploadDir = new File(Constant.UPLOAD_DIRECTORY);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String submittedName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
		int dotIdx = submittedName.lastIndexOf('.');
		String ext = (dotIdx > 0) ? submittedName.substring(dotIdx) : ".png";
		String fname = "avatar_" + System.currentTimeMillis() + ext;
		File targetFile = new File(uploadDir, fname);
		try (InputStream is = avatarPart.getInputStream(); OutputStream os = new FileOutputStream(targetFile)) {
			is.transferTo(os);
		}
		return fname;
	}

	private void clearPendingEmail(HttpSession session) {
		session.removeAttribute("pendingEmail");
		session.removeAttribute("pendingEmailOtp");
		session.removeAttribute("pendingEmailOtpExpiry");
	}
}
