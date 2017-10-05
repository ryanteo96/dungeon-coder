import java.security.MessageDigest;
public class Hash {
	public static void main(String[] args) {
		String password = args[0];
		String hexSalt = args[1];
		String hash = args[2];
		String checkHash = "";
		
		int length = hexSalt.length();
		byte[]salt = new byte[length/2];
		for (int i = 0; i < length; i+=2) {
			salt[i / 2] = (byte)((Character.digit(hexSalt.charAt(i), 16) << 4) + Character.digit(hexSalt.charAt(i + 1), 16));
		}

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(password.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xFF) + 0x100, 16).substring(1));
			}
			checkHash = sb.toString();
		}
		catch (Exception e) {
		}
		if (hash.equals(checkHash)) {
			System.out.println("True");
		}
		System.out.println("False");
	}
}
