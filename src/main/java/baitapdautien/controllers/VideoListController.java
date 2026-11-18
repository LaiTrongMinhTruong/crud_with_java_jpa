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
@WebServlet(urlPatterns = { "/admin/video/list", "/admin/video" })
public class VideoListController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String keyword = req.getParameter("keyword");
		List<Video> videoList;

		if (keyword != null && !keyword.trim().isEmpty()) {
			videoList = videoService.search(keyword.trim());
			req.setAttribute("keyword", keyword.trim());
		} else {
			videoList = videoService.findAll();
		}

		req.setAttribute("videoList", videoList);
		req.getRequestDispatcher("/views/admin/list-video.jsp").forward(req, resp);
	}
}
