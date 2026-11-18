package baitapdautien.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import baitapdautien.models.User;
import baitapdautien.models.Video;
import baitapdautien.service.UserService;
import baitapdautien.service.VideoService;
import baitapdautien.service.impl.UserServiceImpl;
import baitapdautien.service.impl.VideoServiceImpl;
import baitapdautien.util.CloudinaryUtil;
import baitapdautien.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admin/video/add", "/video/add" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 150)
public class VideoAddController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();
	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/admin/add-video.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String title = req.getParameter("title");
		String description = req.getParameter("description");
		boolean active = "on".equalsIgnoreCase(req.getParameter("active")) || "true".equalsIgnoreCase(req.getParameter("active"));
		String posterUrl = req.getParameter("posterUrl");
		String videoUrl = req.getParameter("videoUrl");

		// 1. Xử lý Upload Poster (Ảnh đại diện thumbnail video)
		try {
			Part posterPart = req.getPart("posterFile");
			if (posterPart != null && posterPart.getSize() > 0 && posterPart.getSubmittedFileName() != null
					&& !posterPart.getSubmittedFileName().trim().isEmpty()) {
				// Ưu tiên Upload lên Cloudinary
				try {
					String uploadedPosterUrl = CloudinaryUtil.uploadImage(posterPart, "videos/posters");
					if (uploadedPosterUrl != null && !uploadedPosterUrl.isEmpty()) {
						posterUrl = uploadedPosterUrl;
					}
				} catch (Exception ex) {
					System.err.println("[CLOUDINARY] Upload poster thất bại, chuyển sang lưu cục bộ: " + ex.getMessage());
					// Fallback lưu cục bộ nếu Cloudinary gặp sự cố
					String localFileName = savePartLocally(posterPart, "poster_");
					if (localFileName != null) {
						posterUrl = localFileName;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2. Xử lý Upload Video File
		try {
			Part videoPart = req.getPart("videoFile");
			if (videoPart != null && videoPart.getSize() > 0 && videoPart.getSubmittedFileName() != null
					&& !videoPart.getSubmittedFileName().trim().isEmpty()) {
				// Ưu tiên Upload lên Cloudinary
				try {
					String uploadedVideoUrl = CloudinaryUtil.uploadVideo(videoPart, "videos/media");
					if (uploadedVideoUrl != null && !uploadedVideoUrl.isEmpty()) {
						videoUrl = uploadedVideoUrl;
					}
				} catch (Exception ex) {
					System.err.println("[CLOUDINARY] Upload video thất bại, chuyển sang lưu cục bộ: " + ex.getMessage());
					// Fallback lưu cục bộ
					String localFileName = savePartLocally(videoPart, "video_");
					if (localFileName != null) {
						videoUrl = req.getContextPath() + "/image?fname=" + localFileName;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3. Xác định người đăng (User sở hữu video theo quan hệ 1 - n)
		HttpSession session = req.getSession(false);
		User currentUser = null;
		if (session != null && session.getAttribute("account") != null) {
			currentUser = (User) session.getAttribute("account");
		}
		if (currentUser == null) {
			// Nếu chưa đăng nhập, gán cho tài khoản admin mặc định
			currentUser = userService.get("admin");
			if (currentUser == null) {
				currentUser = userService.findById(1);
			}
		}

		if (title == null || title.trim().isEmpty()) {
			title = "Video không tiêu đề";
		}

		if (posterUrl == null || posterUrl.trim().isEmpty()) {
			posterUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80";
		}

		if (videoUrl == null || videoUrl.trim().isEmpty()) {
			// URL video mẫu mặc định nếu không upload
			videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4";
		}

		Video video = new Video();
		video.setTitle(title.trim());
		video.setDescription(description != null ? description.trim() : "");
		video.setPoster(posterUrl.trim());
		video.setUrl(videoUrl.trim());
		video.setActive(active);
		video.setViews(0);
		video.setCreatedDate(new Date(System.currentTimeMillis()));
		video.setUser(currentUser);

		videoService.insert(video);

		resp.sendRedirect(req.getContextPath() + "/admin/video/list");
	}

	private String savePartLocally(Part part, String prefix) {
		try {
			String uploadPath = Constant.UPLOAD_DIRECTORY;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
			int idx = originalFileName.lastIndexOf(".");
			String ext = idx > 0 ? originalFileName.substring(idx) : ".bin";
			String newFileName = prefix + System.currentTimeMillis() + ext;
			File targetFile = new File(uploadDir, newFileName);
			try (InputStream is = part.getInputStream(); OutputStream os = new FileOutputStream(targetFile)) {
				is.transferTo(os);
			}
			return newFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
