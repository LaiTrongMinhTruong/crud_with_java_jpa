package baitapdautien.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	private static final Properties mailConfig = new Properties();

	static {
		loadConfig();
	}

	/**
	 * Nạp cấu hình từ file mail.properties trong resources
	 */
	public static synchronized void loadConfig() {
		try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("mail.properties")) {
			if (is != null) {
				mailConfig.load(is);
			} else {
				System.err.println("[EMAIL SERVICE] Không tìm thấy file mail.properties trong thư mục tài nguyên!");
			}
		} catch (Exception e) {
			System.err.println("[EMAIL SERVICE] Lỗi đọc mail.properties: " + e.getMessage());
		}
	}

	public static String getConfig(String key, String defaultValue) {
		// Ưu tiên System property hoặc biến môi trường nếu có
		String val = System.getProperty(key);
		if (val == null || val.trim().isEmpty()) {
			String envKey = key.replace('.', '_').toUpperCase();
			val = System.getenv(envKey);
		}
		if (val == null || val.trim().isEmpty()) {
			val = mailConfig.getProperty(key, defaultValue);
		}
		return val != null ? val.trim() : defaultValue;
	}

	public static String getSenderEmail() {
		return getConfig("mail.username", "");
	}

	public static String getSenderPassword() {
		String pass = getConfig("mail.password", "");
		// Loại bỏ mọi khoảng trắng nếu người dùng copy paste mật khẩu Google có dấu cách
		return pass.replaceAll("\\s+", "");
	}

	/**
	 * Kiểm tra xem người dùng đã cấu hình tài khoản gửi mail thật hay chưa
	 */
	public static boolean isRealSmtpConfigured() {
		String email = getSenderEmail();
		String pass = getSenderPassword();
		return !email.isEmpty() && !pass.isEmpty()
				&& !"your_email@gmail.com".equalsIgnoreCase(email)
				&& !"your_app_password_here".equalsIgnoreCase(pass);
	}

	/**
	 * Tạo mã OTP ngẫu nhiên gồm 6 chữ số
	 */
	public static String generateOtp() {
		Random random = new Random();
		int otpNumber = 100000 + random.nextInt(900000);
		return String.valueOf(otpNumber);
	}

	/**
	 * Gửi email HTML qua máy chủ SMTP thực tế
	 */
	public static boolean sendEmail(String toEmail, String subject, String bodyHtml) {
		String senderEmail = getSenderEmail();
		String senderPassword = getSenderPassword();
		String fromName = getConfig("mail.from.name", "Hệ Thống Xác Thực Web");
		String host = getConfig("mail.smtp.host", "smtp.gmail.com");
		String port = getConfig("mail.smtp.port", "587");

		// Trường hợp 1: Người dùng chưa cấu hình tài khoản email thực tế trong mail.properties
		if (!isRealSmtpConfigured()) {
			System.err.println("==========================================================================");
			System.err.println("[EMAIL SERVICE - CẢNH BÁO]");
			System.err.println("Bạn chưa cấu hình tài khoản Gmail gửi thực tế trong 'src/main/resources/mail.properties'.");
			System.err.println("Vui lòng cấu hình mail.username và mail.password (Mật khẩu ứng dụng 16 ký tự).");
			System.err.println("-> Người nhận dự kiến: " + toEmail);
			System.err.println("-> Tiêu đề: " + subject);
			System.err.println("==========================================================================");
			return true;
		}

		// Trường hợp 2: Gửi email thực tế qua giao thức SMTP (Gmail TLS)
		Properties props = new Properties();
		props.put("mail.smtp.auth", getConfig("mail.smtp.auth", "true"));
		props.put("mail.smtp.starttls.enable", getConfig("mail.smtp.starttls.enable", "true"));
		props.put("mail.smtp.starttls.required", getConfig("mail.smtp.starttls.required", "true"));
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.ssl.protocols", getConfig("mail.smtp.ssl.protocols", "TLSv1.2"));
		props.put("mail.smtp.connectiontimeout", getConfig("mail.smtp.connectiontimeout", "10000"));
		props.put("mail.smtp.timeout", getConfig("mail.smtp.timeout", "10000"));
		props.put("mail.smtp.writetimeout", getConfig("mail.smtp.writetimeout", "10000"));

		try {
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(senderEmail, senderPassword);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail, fromName, "UTF-8"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setContent(bodyHtml, "text/html; charset=UTF-8");

			System.out.println("[EMAIL SERVICE] Đang kết nối tới " + host + ":" + port + " để gửi mail tới " + toEmail + "...");
			Transport.send(message);
			System.out.println("[EMAIL SERVICE] >>> GỬI EMAIL THÀNH CÔNG THỰC TẾ QUA SMTP TỚI: " + toEmail);
			return true;

		} catch (MessagingException me) {
			System.err.println("[EMAIL SERVICE - LỖI GỬI THỰC TẾ]");
			System.err.println("Không thể gửi email qua SMTP: " + me.getMessage());
			System.err.println("Gợi ý: Kiểm tra lại mail.username, mail.password (Mật khẩu ứng dụng 16 ký tự) hoặc kết nối mạng.");
			me.printStackTrace();
			return false;
		} catch (Exception e) {
			System.err.println("[EMAIL SERVICE - LỖI BẤT NGỜ]: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gửi mã OTP kích hoạt tài khoản
	 */
	public static boolean sendActivationOtp(String toEmail, String fullname, String otp) {
		String subject = "Mã xác nhận kích hoạt tài khoản của bạn";
		String bodyHtml = "<div style='font-family: Arial, sans-serif; padding: 25px; border: 1px solid #e0e0e0; border-radius: 8px; max-width: 520px; margin: auto; background: #ffffff;'>"
				+ "<div style='text-align: center; margin-bottom: 20px;'>"
				+ "<h2 style='color: #0088cc; margin: 0;'>XÁC NHẬN KÍCH HOẠT TÀI KHOẢN</h2>"
				+ "</div>"
				+ "<p>Xin chào <strong>" + (fullname != null && !fullname.trim().isEmpty() ? fullname : "Bạn") + "</strong>,</p>"
				+ "<p>Cảm ơn bạn đã đăng ký tài khoản. Đây là mã xác thực OTP dùng để kích hoạt tài khoản của bạn:</p>"
				+ "<div style='background: #f0f7fc; border: 1px dashed #0088cc; padding: 18px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #0088cc; border-radius: 8px; margin: 25px 0;'>"
				+ otp + "</div>"
				+ "<p style='color: #555; font-size: 13px; line-height: 1.5;'>Lưu ý: Mã OTP này có hiệu lực trong vòng <strong>10 phút</strong>. Vì lý do bảo mật, tuyệt đối không chia sẻ mã này cho bất kỳ ai.</p>"
				+ "<hr style='border: none; border-top: 1px solid #eee; margin: 25px 0;'>"
				+ "<p style='font-size: 12px; color: #888; text-align: center; margin: 0;'>Hệ thống Website Java Servlet - Khoa CNTT</p>"
				+ "</div>";
		return sendEmail(toEmail, subject, bodyHtml);
	}

	/**
	 * Gửi mã OTP quên mật khẩu
	 */
	public static boolean sendForgotPasswordOtp(String toEmail, String fullname, String otp) {
		String subject = "Mã xác nhận khôi phục mật khẩu";
		String bodyHtml = "<div style='font-family: Arial, sans-serif; padding: 25px; border: 1px solid #e0e0e0; border-radius: 8px; max-width: 520px; margin: auto; background: #ffffff;'>"
				+ "<div style='text-align: center; margin-bottom: 20px;'>"
				+ "<h2 style='color: #e65100; margin: 0;'>KHÔI PHỤC MẬT KHẨU</h2>"
				+ "</div>"
				+ "<p>Xin chào <strong>" + (fullname != null && !fullname.trim().isEmpty() ? fullname : "Bạn") + "</strong>,</p>"
				+ "<p>Hệ thống nhận được yêu cầu cấp lại mật khẩu cho tài khoản của bạn. Vui lòng nhập mã OTP sau đây để thiết lập mật khẩu mới:</p>"
				+ "<div style='background: #fff8f0; border: 1px dashed #e65100; padding: 18px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #e65100; border-radius: 8px; margin: 25px 0;'>"
				+ otp + "</div>"
				+ "<p style='color: #555; font-size: 13px; line-height: 1.5;'>Lưu ý: Mã OTP này có hiệu lực trong vòng <strong>10 phút</strong>. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
				+ "<hr style='border: none; border-top: 1px solid #eee; margin: 25px 0;'>"
				+ "<p style='font-size: 12px; color: #888; text-align: center; margin: 0;'>Hệ thống Website Java Servlet - Khoa CNTT</p>"
				+ "</div>";
		return sendEmail(toEmail, subject, bodyHtml);
	}

	/**
	 * Gửi mã OTP xác nhận cập nhật email mới trong trang Profile
	 */
	public static boolean sendUpdateEmailOtp(String toEmail, String fullname, String otp) {
		String subject = "Mã xác nhận thay đổi địa chỉ Email";
		String bodyHtml = "<div style='font-family: Arial, sans-serif; padding: 25px; border: 1px solid #e0e0e0; border-radius: 8px; max-width: 520px; margin: auto; background: #ffffff;'>"
				+ "<div style='text-align: center; margin-bottom: 20px;'>"
				+ "<h2 style='color: #198754; margin: 0;'>XÁC NHẬN CẬP NHẬT EMAIL</h2>"
				+ "</div>"
				+ "<p>Xin chào <strong>" + (fullname != null && !fullname.trim().isEmpty() ? fullname : "Bạn") + "</strong>,</p>"
				+ "<p>Bạn vừa yêu cầu đổi địa chỉ email tài khoản sang email này. Vui lòng nhập mã OTP dưới đây để hoàn tất cập nhật:</p>"
				+ "<div style='background: #eefaf3; border: 1px dashed #198754; padding: 18px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #198754; border-radius: 8px; margin: 25px 0;'>"
				+ otp + "</div>"
				+ "<p style='color: #555; font-size: 13px; line-height: 1.5;'>Lưu ý: Mã OTP này có hiệu lực trong vòng <strong>10 phút</strong>. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
				+ "<hr style='border: none; border-top: 1px solid #eee; margin: 25px 0;'>"
				+ "<p style='font-size: 12px; color: #888; text-align: center; margin: 0;'>Hệ thống Website Java Servlet - Khoa CNTT</p>"
				+ "</div>";
		return sendEmail(toEmail, subject, bodyHtml);
	}
}
