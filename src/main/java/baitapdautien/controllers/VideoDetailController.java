package baitapdautien.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import baitapdautien.models.Video;
import baitapdautien.service.VideoService;
import baitapdautien.service.impl.VideoServiceImpl;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/video/detail" })
public class VideoDetailController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idParam = req.getParameter("id");
		if (idParam == null || idParam.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/video");
			return;
		}

		try {
			int id = Integer.parseInt(idParam.trim());
			Video video = videoService.findById(id);
			if (video == null) {
				resp.sendRedirect(req.getContextPath() + "/video");
				return;
			}

			// Tăng lượt xem video khi người dùng mở xem chi tiết
			videoService.increaseViews(id);
			video.setViews(video.getViews() + 1);

			// Lấy danh sách video đề xuất (các video mới nhất khác)
			List<Video> relatedVideos = videoService.findTop10Latest();

			req.setAttribute("video", video);
			req.setAttribute("relatedVideos", relatedVideos);

			req.getRequestDispatcher("/views/video/detail.jsp").forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/video");
		}
	}
}
