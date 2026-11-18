package baitapdautien.util;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.Part;

public class CloudinaryUtil {

	private static Cloudinary cloudinary;
	private static final Properties config = new Properties();

	static {
		loadConfig();
	}

	public static synchronized void loadConfig() {
		try (InputStream is = CloudinaryUtil.class.getClassLoader().getResourceAsStream("cloudinary.properties")) {
			if (is != null) {
				config.load(is);
			}
		} catch (Exception e) {
			System.err.println("[CLOUDINARY] Không thể đọc cloudinary.properties: " + e.getMessage());
		}

		String cloudName = getConfig("cloudinary.cloud-name", "");
		String apiKey = getConfig("cloudinary.api-key", "");
		String apiSecret = getConfig("cloudinary.api-secret", "");

		if (!cloudName.isEmpty() && !apiKey.isEmpty() && !apiSecret.isEmpty()) {
			cloudinary = new Cloudinary(ObjectUtils.asMap(
					"cloud_name", cloudName,
					"api_key", apiKey,
					"api_secret", apiSecret,
					"secure", true));
		}
	}

	public static String getConfig(String key, String defaultValue) {
		String val = System.getProperty(key);
		if (val == null || val.trim().isEmpty()) {
			String envKey = key.replace('.', '_').toUpperCase();
			val = System.getenv(envKey);
		}
		if (val == null || val.trim().isEmpty()) {
			val = config.getProperty(key, defaultValue);
		}
		return val != null ? val.trim() : defaultValue;
	}

	public static Cloudinary getCloudinary() {
		if (cloudinary == null) {
			loadConfig();
		}
		return cloudinary;
	}

	public static boolean isConfigured() {
		return cloudinary != null;
	}

	/**
	 * Upload file từ Servlet Part lên Cloudinary với resourceType (image hoặc video)
	 */
	public static String upload(Part part, String folder, String resourceType) throws Exception {
		if (part == null || part.getSize() == 0) {
			return null;
		}

		Cloudinary cld = getCloudinary();
		if (cld == null) {
			throw new IllegalStateException("Cloudinary chưa được cấu hình!");
		}

		byte[] fileBytes;
		try (InputStream is = part.getInputStream()) {
			fileBytes = is.readAllBytes();
		}

		Map<?, ?> uploadParams = ObjectUtils.asMap(
				"folder", folder != null ? folder : "baitapdautien",
				"resource_type", resourceType != null ? resourceType : "auto",
				"overwrite", false);

		Map<?, ?> result = cld.uploader().upload(fileBytes, uploadParams);
		Object secureUrl = result.get("secure_url");
		if (secureUrl != null) {
			return secureUrl.toString();
		}
		Object url = result.get("url");
		return url != null ? url.toString() : null;
	}

	/**
	 * Upload hình ảnh (poster, thumbnail) lên Cloudinary
	 */
	public static String uploadImage(Part part, String folder) throws Exception {
		return upload(part, folder != null ? folder : "videos/posters", "image");
	}

	/**
	 * Upload video lên Cloudinary
	 */
	public static String uploadVideo(Part part, String folder) throws Exception {
		return upload(part, folder != null ? folder : "videos/media", "video");
	}
}
