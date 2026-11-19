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
import jakarta.servlet.http.Part;

import baitapdautien.models.Video;
import baitapdautien.service.VideoService;
import baitapdautien.service.impl.VideoServiceImpl;
import baitapdautien.util.CloudinaryUtil;
import baitapdautien.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admin/video/edit" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 150)
public class VideoEditController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idParam = req.getParameter("id");
		if (idParam == null || idParam.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/admin/video/list");
			return;
		}

		try {
			int id = Integer.parseInt(idParam.trim());
			Video video = videoService.findById(id);
			if (video == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/video/list");
				return;
			}
			req.setAttribute("video", video);
			req.getRequestDispatcher("/views/admin/edit-video.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/admin/video/list");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String idParam = req.getParameter("id");
		if (idParam == null || idParam.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/admin/video/list");
			return;
		}

		int id = Integer.parseInt(idParam.trim());
		Video video = videoService.findById(id);
		if (video == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/video/list");
			return;
		}

		String title = req.getParameter("title");
		String description = req.getParameter("description");
		boolean active = "on".equalsIgnoreCase(req.getParameter("active")) || "true".equalsIgnoreCase(req.getParameter("active"));
		String posterUrl = req.getParameter("posterUrl");
		String videoUrl = req.getParameter("videoUrl");

		if (title == null || title.trim().length() < 3 || title.trim().length() > 200) {
			req.setAttribute("video", video);
			req.setAttribute("alert", "Tiêu đề video phải từ 3 đến 200 ký tự!");
			req.getRequestDispatcher("/views/admin/edit-video.jsp").forward(req, resp);
			return;
		}
		video.setTitle(title.trim());
		video.setDescription(description != null ? description.trim() : "");
		video.setActive(active);

		// 1. Kiểm tra Upload Poster mới
		try {
			Part posterPart = req.getPart("posterFile");
			if (posterPart != null && posterPart.getSize() > 0 && posterPart.getSubmittedFileName() != null
					&& !posterPart.getSubmittedFileName().trim().isEmpty()) {
				try {
					String uploadedPosterUrl = CloudinaryUtil.uploadImage(posterPart, "videos/posters");
					if (uploadedPosterUrl != null && !uploadedPosterUrl.isEmpty()) {
						video.setPoster(uploadedPosterUrl);
					}
				} catch (Exception ex) {
					System.err.println("[CLOUDINARY] Upload poster thất bại, chuyển sang lưu cục bộ: " + ex.getMessage());
					String localFileName = savePartLocally(posterPart, "poster_");
					if (localFileName != null) {
						video.setPoster(localFileName);
					}
				}
			} else if (posterUrl != null && !posterUrl.trim().isEmpty()) {
				video.setPoster(posterUrl.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2. Kiểm tra Upload Video mới
		try {
			Part videoPart = req.getPart("videoFile");
			if (videoPart != null && videoPart.getSize() > 0 && videoPart.getSubmittedFileName() != null
					&& !videoPart.getSubmittedFileName().trim().isEmpty()) {
				try {
					String uploadedVideoUrl = CloudinaryUtil.uploadVideo(videoPart, "videos/media");
					if (uploadedVideoUrl != null && !uploadedVideoUrl.isEmpty()) {
						video.setUrl(uploadedVideoUrl);
					}
				} catch (Exception ex) {
					System.err.println("[CLOUDINARY] Upload video thất bại, chuyển sang lưu cục bộ: " + ex.getMessage());
					String localFileName = savePartLocally(videoPart, "video_");
					if (localFileName != null) {
						video.setUrl(req.getContextPath() + "/image?fname=" + localFileName);
					}
				}
			} else if (videoUrl != null && !videoUrl.trim().isEmpty()) {
				video.setUrl(videoUrl.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		videoService.update(video);
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
