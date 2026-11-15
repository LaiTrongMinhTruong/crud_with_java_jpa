package baitapdautien.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import baitapdautien.models.Category;
import baitapdautien.service.CategoryService;
import baitapdautien.service.impl.CategoryServiceImpl;
import baitapdautien.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admin/category/edit" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CategoryEditController extends HttpServlet {
	CategoryService cateService = new CategoryServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (id == null || id.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/admin/category/list");
			return;
		}

		try {
			Category category = cateService.get(Integer.parseInt(id.trim()));
			if (category == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/category/list");
				return;
			}
			req.setAttribute("category", category);
			RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/edit-category.jsp");
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/admin/category/list");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		// Lấy ID an toàn từ nhiều nguồn (request parameter, query string hoặc multipart part)
		String paramId = req.getParameter("id");
		if (paramId == null || paramId.trim().isEmpty()) {
			paramId = req.getParameter("cateid");
		}
		if (paramId == null || paramId.trim().isEmpty()) {
			try {
				Part idPart = req.getPart("id");
				if (idPart != null && idPart.getSize() > 0) {
					paramId = new String(idPart.getInputStream().readAllBytes(), "UTF-8").trim();
				}
			} catch (Exception ignored) {
			}
		}
		if (paramId == null || paramId.trim().isEmpty()) {
			try {
				Part cateIdPart = req.getPart("cateid");
				if (cateIdPart != null && cateIdPart.getSize() > 0) {
					paramId = new String(cateIdPart.getInputStream().readAllBytes(), "UTF-8").trim();
				}
			} catch (Exception ignored) {
			}
		}

		// Nếu không lấy được ID thì quay về trang danh sách thay vì báo lỗi 500
		if (paramId == null || paramId.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/admin/category/list");
			return;
		}

		int categoryid = Integer.parseInt(paramId.trim());

		// Lấy tên danh mục
		String categoryname = req.getParameter("catename");
		if (categoryname == null || categoryname.trim().isEmpty()) {
			try {
				Part namePart = req.getPart("catename");
				if (namePart != null && namePart.getSize() > 0) {
					categoryname = new String(namePart.getInputStream().readAllBytes(), "UTF-8").trim();
				}
			} catch (Exception ignored) {
			}
		}

		Category category = new Category();
		category.setCateId(categoryid);
		category.setCateName(categoryname != null ? categoryname.trim() : "");

		// Lấy thông tin cũ của category để giữ ảnh nếu người dùng không chọn ảnh mới
		Category cateold = cateService.findById(categoryid);
		String fileold = (cateold != null && cateold.getIcon() != null && !cateold.getIcon().isEmpty())
				? cateold.getIcon()
				: "avata.png";

		// Xử lý upload ảnh mới nếu có
		String uploadPath = Constant.UPLOAD_DIRECTORY;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		boolean hasNewImage = false;
		try {
			Part part = req.getPart("icon");
			if (part == null || part.getSize() == 0) {
				part = req.getPart("images");
			}

			if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null
					&& !part.getSubmittedFileName().trim().isEmpty()) {
				String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
				int index = filename.lastIndexOf(".");
				String ext = (index > 0) ? filename.substring(index + 1) : "png";
				String fname = System.currentTimeMillis() + "." + ext;

				File targetFile = new File(uploadDir, fname);
				try (InputStream is = part.getInputStream(); OutputStream os = new FileOutputStream(targetFile)) {
					is.transferTo(os);
				}
				category.setIcon(fname);
				hasNewImage = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!hasNewImage) {
			// Giữ nguyên ảnh cũ
			category.setIcon(fileold);
		}

		cateService.update(category);
		resp.sendRedirect(req.getContextPath() + "/admin/category/list");
	}
}
