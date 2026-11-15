package baitapdautien;

import java.util.List;

import baitapdautien.configs.JPAConfig;
import baitapdautien.models.Category;
import baitapdautien.models.User;
import baitapdautien.service.CategoryService;
import baitapdautien.service.UserService;
import baitapdautien.service.impl.CategoryServiceImpl;
import baitapdautien.service.impl.UserServiceImpl;

public class AppTest {
	public static void main(String[] args) {
		System.out.println("==================================================");
		System.out.println("    KIỂM THỬ TOÀN DIỆN JPA + HIBERNATE BANHANG    ");
		System.out.println("==================================================");

		UserService userService = new UserServiceImpl();
		CategoryService cateService = new CategoryServiceImpl();

		try {
			// -----------------------------------------------------------------
			// 1. TEST USER LOGIN & AUTH
			// -----------------------------------------------------------------
			System.out.println("\n>>> PHẦN 1: TEST ĐĂNG NHẬP (LOGIN)");
			User userAdmin = userService.login("admin", "123");
			if (userAdmin != null) {
				System.out.println("[OK] Login admin thành công: " + userAdmin.getFullName() + ", role = "
						+ userAdmin.getRoleid());
			} else {
				System.err.println("[FAIL] Login admin thất bại!");
			}

			User invalidUser = userService.login("admin", "wrongpassword");
			if (invalidUser == null) {
				System.out.println("[OK] Đăng nhập với sai mật khẩu bị chặn chính xác!");
			} else {
				System.err.println("[FAIL] Lỗi: Đăng nhập sai mật khẩu nhưng vẫn trả về User!");
			}

			// -----------------------------------------------------------------
			// 2. TEST USER REGISTER (LOGUP) & DUPLICATE CHECKS
			// -----------------------------------------------------------------
			System.out.println("\n>>> PHẦN 2: TEST ĐĂNG KÝ (SIGNUP / LOGUP)");
			String testUsername = "testuser_" + System.currentTimeMillis();
			String testEmail = testUsername + "@example.com";
			String testPhone = "0987654321";

			boolean existEmailBefore = userService.checkExistEmail(testEmail);
			boolean existUserBefore = userService.checkExistUsername(testUsername);
			System.out.println("[OK] Kiểm tra tồn tại trước khi tạo: email=" + existEmailBefore + ", user="
					+ existUserBefore);

			boolean regSuccess = userService.register(testUsername, "password123", testEmail, "Người Dùng Mới",
					testPhone);
			if (regSuccess) {
				System.out.println("[OK] Đăng ký tài khoản mới thành công: " + testUsername);
			} else {
				System.err.println("[FAIL] Đăng ký thất bại!");
			}

			boolean existUserAfter = userService.checkExistUsername(testUsername);
			boolean existEmailAfter = userService.checkExistEmail(testEmail);
			System.out.println("[OK] Kiểm tra tồn tại sau khi tạo: email=" + existEmailAfter + ", user="
					+ existUserAfter);

			User registeredUser = userService.login(testUsername, "password123");
			if (registeredUser != null) {
				System.out.println("[OK] Đăng nhập ngay sau khi đăng ký thành công: ID = " + registeredUser.getId());
				// Dọn dẹp user test để không làm rác DB
				userService.delete(registeredUser.getId());
				System.out.println("[OK] Đã dọn dẹp user kiểm thử ID " + registeredUser.getId());
			}

			// -----------------------------------------------------------------
			// 3. TEST CATEGORY CRUD & SEARCH
			// -----------------------------------------------------------------
			System.out.println("\n>>> PHẦN 3: TEST QUẢN LÝ DANH MỤC (CRUD CATEGORY)");

			// 3.1 GetAll
			List<Category> list = cateService.getAll();
			System.out.println("[OK] Lấy danh sách danh mục (getAll). Tổng số: " + list.size());
			for (Category c : list) {
				System.out.println("  -> ID: " + c.getId() + " | Tên: " + c.getName() + " | Icon: " + c.getIcon());
			}

			// 3.2 Insert
			Category newCate = new Category("Thời Trang Thu Đông", "thudong.png");
			cateService.insert(newCate);
			System.out.println("[OK] Đã insert Category: " + newCate.getName() + ", ID sinh ra: " + newCate.getId());

			// 3.3 Get by Name
			Category found = cateService.get("Thời Trang Thu Đông");
			if (found != null) {
				System.out.println("[OK] Tìm thấy category theo tên: ID = " + found.getId() + ", Icon = "
						+ found.getIcon());

				// 3.4 Search keyword
				List<Category> searchResults = cateService.search("Thu Đông");
				System.out.println("[OK] Tìm kiếm với từ khóa 'Thu Đông': tìm thấy " + searchResults.size() + " kết quả.");

				// 3.5 Edit / Update
				found.setName("Thời Trang Mùa Đông 2026");
				found.setIcon("winter2026.png");
				cateService.edit(found);
				Category updated = cateService.get(found.getId());
				System.out.println("[OK] Sau khi cập nhật, Tên: " + updated.getName() + ", Icon: "
						+ updated.getIcon());

				// 3.6 Delete
				cateService.delete(found.getId());
				Category afterDelete = cateService.get(found.getId());
				if (afterDelete == null) {
					System.out.println("[OK] Đã xóa danh mục ID " + found.getId() + " thành công!");
				} else {
					System.err.println("[FAIL] Xóa danh mục thất bại!");
				}
			}

			System.out.println("\n==================================================");
			System.out.println("     TẤT CẢ TÍNH NĂNG JPA + HIBERNATE HOÀN TẤT!    ");
			System.out.println("==================================================");

		} finally {
			JPAConfig.close();
		}
	}
}
