package baitapdautien.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	/**
	 * Hash a plain text password with BCrypt.
	 */
	public static String hashPassword(String plainPassword) {
		if (plainPassword == null || plainPassword.isEmpty()) {
			return "";
		}
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	/**
	 * Check a plain text password against a stored hash or plain password.
	 * Supports both BCrypt hashes ($2a$, $2b$, $2y$) and legacy plain text.
	 */
	public static boolean checkPassword(String plainPassword, String storedPassword) {
		if (plainPassword == null || storedPassword == null) {
			return false;
		}
		if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")
				|| storedPassword.startsWith("$2y$")) {
			try {
				return BCrypt.checkpw(plainPassword, storedPassword);
			} catch (Exception e) {
				return false;
			}
		}
		// Fallback for legacy plain text passwords in the database
		return plainPassword.equals(storedPassword);
	}
}
