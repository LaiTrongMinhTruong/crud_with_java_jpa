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
@WebServlet(urlPatterns = { "/home", "/admin/home", "/manager/home", "/admin" })
public class HomeController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Lấy 10 video mới nhất để hiển thị lên trang chủ theo đúng yêu cầu
		List<Video> top10Videos = videoService.findTop10Latest();
		req.setAttribute("top10Videos", top10Videos);

		req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
	}
}
