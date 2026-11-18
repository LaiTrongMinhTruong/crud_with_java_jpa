package baitapdautien.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import baitapdautien.models.User;
import baitapdautien.models.Video;
import baitapdautien.service.VideoService;
import baitapdautien.service.impl.VideoServiceImpl;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/video" })
public class VideoController extends HttpServlet {

	private final VideoService videoService = new VideoServiceImpl();
	private static final int PAGE_SIZE = 6; // Yêu cầu phân trang đúng 6 sản phẩm/trang

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Xác định trang hiện tại
		int page = 1;
		String pageParam = req.getParameter("page");
		if (pageParam != null && !pageParam.trim().isEmpty()) {
			try {
				page = Math.max(1, Integer.parseInt(pageParam.trim()));
			} catch (NumberFormatException ignored) {
				page = 1;
			}
		}

		// 2. Xác định phạm vi xem (videos của user cụ thể hoặc tất cả)
		String scope = req.getParameter("scope");
		String userIdParam = req.getParameter("userId");
		Integer targetUserId = null;

		HttpSession session = req.getSession(false);
		User currentUser = null;
		if (session != null && session.getAttribute("account") != null) {
			currentUser = (User) session.getAttribute("account");
		}

		if (userIdParam != null && !userIdParam.trim().isEmpty()) {
			try {
				targetUserId = Integer.parseInt(userIdParam.trim());
			} catch (NumberFormatException ignored) {
			}
		} else if ("all".equalsIgnoreCase(scope)) {
			targetUserId = null; // Xem tất cả
		} else if (currentUser != null) {
			// Mặc định khi user đã đăng nhập: hiển thị video của user này
			targetUserId = currentUser.getId();
		}

		List<Video> videoList;
		int totalItems;

		if (targetUserId != null) {
			videoList = videoService.findByUserIdWithPaging(targetUserId, page, PAGE_SIZE);
			totalItems = videoService.countByUserId(targetUserId);
		} else {
			videoList = videoService.findAllWithPaging(page, PAGE_SIZE);
			totalItems = videoService.countAll();
		}

		int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
		if (totalPages < 1) {
			totalPages = 1;
		}

		req.setAttribute("videoList", videoList);
		req.setAttribute("currentPage", page);
		req.setAttribute("totalPages", totalPages);
		req.setAttribute("totalItems", totalItems);
		req.setAttribute("targetUserId", targetUserId);
		req.setAttribute("scope", scope);

		req.getRequestDispatcher("/views/video/list.jsp").forward(req, resp);
	}
}
