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
@WebServlet(urlPatterns = { "/admin/category/add" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CategoryAddController extends HttpServlet {
	CategoryService cateService = new CategoryServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/add-category.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

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
		category.setCateName(categoryname != null ? categoryname.trim() : "");

		String uploadPath = Constant.UPLOAD_DIRECTORY;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		boolean hasImage = false;
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
				hasImage = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!hasImage) {
			category.setIcon("avata.png");
		}

		cateService.insert(category);
		resp.sendRedirect(req.getContextPath() + "/admin/category/list");
	}
}
