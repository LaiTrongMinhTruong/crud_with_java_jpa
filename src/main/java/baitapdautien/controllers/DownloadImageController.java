package baitapdautien.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import baitapdautien.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/image") // ?fname=abc.png
public class DownloadImageController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fname");
		if (fileName != null && !fileName.isEmpty()) {
			File file = new File(Constant.DIR + File.separator + fileName);
			resp.setContentType("image/jpeg");
			if (file.exists()) {
				IOUtils.copy(new FileInputStream(file), resp.getOutputStream());
			}
		}
	}
}
