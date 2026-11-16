package baitapdautien;

import baitapdautien.configs.JPAConfig;
import baitapdautien.models.User;
import baitapdautien.service.UserService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.util.EmailUtil;
import baitapdautien.util.PasswordUtil;

public class AuthOtpPasswordTest {

	public static void main(String[] args) {
		System.out.println("===============================================================");
		System.out.println("   KIỂM THỬ 3 CHỨC NĂNG: OTP ĐĂNG KÝ - HASH PASSWORD - QUÊN MẬT KHẨU");
		System.out.println("===============================================================");

		UserService userService = new UserServiceImpl();
		long timestamp = System.currentTimeMillis();
		String username = "user_otp_" + timestamp;
		String email = "test_otp_" + timestamp + "@example.com";
		String password = "MySecurePassword@123";
		String fullname = "Nguyễn Văn OTP";
		String phone = "0901234567";

		try {
			// =========================================================
			// 1. KIỂM THỬ ĐĂNG KÝ VÀ KÍCH HOẠT TÀI KHOẢN BẰNG MÃ OTP
			// =========================================================
			System.out.println("\n[TEST 1] ĐĂNG KÝ VÀ KÍCH HOẠT TÀI KHOẢN BẰNG OTP");
			String otpRegister = EmailUtil.generateOtp();
			System.out.println("-> Mã OTP sinh ra cho đăng ký: " + otpRegister);

			boolean regOk = userService.registerWithOtp(username, password, email, fullname, phone, otpRegister);
			if (!regOk) {
				throw new RuntimeException("Đăng ký tài khoản với OTP thất bại!");
			}
			System.out.println("[PASS] 1.1 Đăng ký tài khoản thành công!");

			User u = userService.get(username);
			if (u == null || u.getStatus() != 0 || !otpRegister.equals(u.getCode())) {
				throw new RuntimeException("Tài khoản chưa ở trạng thái chờ kích hoạt (status=0) hoặc mã OTP không khớp!");
			}
			System.out.println("[PASS] 1.2 Tài khoản ở trạng thái CHƯA KÍCH HOẠT (status = " + u.getStatus() + ", code = " + u.getCode() + ")");

			// Thử kích hoạt với OTP sai
			boolean wrongOtpOk = userService.activateAccount(email, "000000");
			if (wrongOtpOk) {
				throw new RuntimeException("Lỗi: Mã OTP sai nhưng hệ thống vẫn cho kích hoạt!");
			}
			System.out.println("[PASS] 1.3 Nhập mã OTP sai bị chặn chính xác!");

			// Kích hoạt với OTP đúng
			boolean rightOtpOk = userService.activateAccount(email, otpRegister);
			if (!rightOtpOk) {
				throw new RuntimeException("Kích hoạt với OTP đúng bị thất bại!");
			}
			User uActivated = userService.get(username);
			if (uActivated.getStatus() != 1 || uActivated.getCode() != null) {
				throw new RuntimeException("Sau khi kích hoạt, status phải là 1 và code phải là null!");
			}
			System.out.println("[PASS] 1.4 Kích hoạt tài khoản thành công (status = " + uActivated.getStatus() + ", code = " + uActivated.getCode() + ")");

			// =========================================================
			// 2. KIỂM THỬ ĐĂNG NHẬP VỚI MẬT KHẨU MÃ HÓA (BCRYPT)
			// =========================================================
			System.out.println("\n[TEST 2] MÃ HÓA MẬT KHẨU (BCRYPT) VÀ ĐĂNG NHẬP / ĐĂNG XUẤT");
			System.out.println("-> Mật khẩu lưu trong DB: " + uActivated.getPassWord());
			if (!uActivated.getPassWord().startsWith("$2a$") && !uActivated.getPassWord().startsWith("$2b$")) {
				throw new RuntimeException("Mật khẩu trong DB không phải dạng mã hóa BCrypt!");
			}
			System.out.println("[PASS] 2.1 Mật khẩu được băm an toàn chuẩn BCrypt!");

			// Đăng nhập mật khẩu sai
			User loginWrong = userService.login(username, "SaiMatKhau123");
			if (loginWrong != null) {
				throw new RuntimeException("Lỗi: Đăng nhập sai mật khẩu nhưng vẫn thành công!");
			}
			System.out.println("[PASS] 2.2 Đăng nhập với mật khẩu sai bị từ chối!");

			// Đăng nhập mật khẩu đúng bằng username
			User loginOk = userService.login(username, password);
			if (loginOk == null) {
				throw new RuntimeException("Đăng nhập bằng username và mật khẩu đúng bị thất bại!");
			}
			System.out.println("[PASS] 2.3 Đăng nhập thành công bằng Username: " + loginOk.getUserName());

			// Đăng nhập bằng Email
			User loginByEmail = userService.login(email, password);
			if (loginByEmail == null) {
				throw new RuntimeException("Đăng nhập bằng Email và mật khẩu đúng bị thất bại!");
			}
			System.out.println("[PASS] 2.4 Đăng nhập thành công bằng Email: " + loginByEmail.getEmail());

			// =========================================================
			// 3. KIỂM THỬ QUÊN MẬT KHẨU GỬI OTP QUA MAIL & ĐẶT LẠI MẬT KHẨU
			// =========================================================
			System.out.println("\n[TEST 3] QUÊN MẬT KHẨU - GỬI MÃ OTP QUA EMAIL VÀ ĐẶT LẠI MẬT KHẨU");
			boolean sendOtpOk = userService.sendResetPasswordOtp(username);
			if (!sendOtpOk) {
				throw new RuntimeException("Yêu cầu gửi mã OTP quên mật khẩu thất bại!");
			}
			User uReset = userService.get(username);
			String resetOtp = uReset.getCode();
			System.out.println("-> Mã OTP khôi phục mật khẩu gửi vào email: " + resetOtp);
			if (resetOtp == null || resetOtp.length() != 6) {
				throw new RuntimeException("Mã OTP khôi phục mật khẩu không hợp lệ!");
			}
			System.out.println("[PASS] 3.1 Hệ thống đã sinh mã OTP 6 số và gửi qua email thành công!");

			// Thử đổi mật khẩu với OTP sai
			String newPassword = "NewBrandPassword@2026";
			boolean resetWrongOtp = userService.resetPassword(email, "111111", newPassword);
			if (resetWrongOtp) {
				throw new RuntimeException("Lỗi: Nhập sai OTP nhưng hệ thống vẫn cho đổi mật khẩu!");
			}
			System.out.println("[PASS] 3.2 Nhập sai OTP khi đặt lại mật khẩu bị từ chối!");

			// Đổi mật khẩu với OTP đúng
			boolean resetSuccess = userService.resetPassword(email, resetOtp, newPassword);
			if (!resetSuccess) {
				throw new RuntimeException("Đặt lại mật khẩu với OTP đúng bị thất bại!");
			}
			System.out.println("[PASS] 3.3 Đặt lại mật khẩu mới thành công!");

			// Kiểm tra mật khẩu cũ không còn đăng nhập được
			User loginOldPass = userService.login(username, password);
			if (loginOldPass != null) {
				throw new RuntimeException("Mật khẩu cũ vẫn đăng nhập được sau khi đổi mật khẩu!");
			}
			System.out.println("[PASS] 3.4 Mật khẩu cũ đã vô hiệu hóa hoàn toàn!");

			// Đăng nhập với mật khẩu mới
			User loginNewPass = userService.login(username, newPassword);
			if (loginNewPass == null) {
				throw new RuntimeException("Không thể đăng nhập bằng mật khẩu mới!");
			}
			System.out.println("[PASS] 3.5 Đăng nhập bằng mật khẩu mới thành công: " + loginNewPass.getUserName());
			System.out.println("-> Mật khẩu mới trong DB: " + loginNewPass.getPassWord());

			// Dọn dẹp dữ liệu kiểm thử
			userService.delete(loginNewPass.getId());
			System.out.println("\n[CLEANUP] Đã dọn dẹp tài khoản kiểm thử ID " + loginNewPass.getId());

			System.out.println("\n===============================================================");
			System.out.println("   >>> TẤT CẢ 3 TÍNH NĂNG ĐÃ VƯỢT QUA TEST THÀNH CÔNG 100%! <<<");
			System.out.println("===============================================================");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[FAIL] " + e.getMessage());
			System.exit(1);
		} finally {
			JPAConfig.close();
			System.exit(0);
		}
	}
}
