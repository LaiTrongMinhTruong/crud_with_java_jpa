package baitapdautien.util;

import java.io.File;

public class Constant {
	public static final String SESSION_USERNAME = "username";
	public static final String COOKIE_REMEMBER = "username";
	public static final String REGISTER = "/views/register.jsp";

	// Thư mục upload hình ảnh (hỗ trợ Linux và Windows)
	public static final String DIR = System.getProperty("os.name").toLowerCase().contains("win")
			? "E:\\upload"
			: System.getProperty("user.home") + File.separator + "upload";

	public static final String UPLOAD_DIRECTORY = DIR;

	public static class Path {
		public static final String REGISTER = "/views/register.jsp";
		public static final String LOGIN = "/views/login.jsp";
	}
}
