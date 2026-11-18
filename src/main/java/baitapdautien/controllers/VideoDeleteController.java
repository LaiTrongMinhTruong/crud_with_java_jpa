package baitapdautien.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import baitapdautien.service.VideoService;
import baitapdautien.service.impl.VideoServiceImpl;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admin/video/delete" })
public class VideoDeleteController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idParam = req.getParameter("id");
		if (idParam != null && !idParam.trim().isEmpty()) {
			try {
				int id = Integer.parseInt(idParam.trim());
				videoService.delete(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resp.sendRedirect(req.getContextPath() + "/admin/video/list");
	}
}
