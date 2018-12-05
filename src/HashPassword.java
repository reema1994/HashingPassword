import com.sun.security.ntlm.NTLMException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HashPassword {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		String password = "Secrete@343";
        String saltKey = "PveFT7isDjGYFTaYhc2Fzw==";
        String hash1,hash2 = null;

        // Assume from UI
        HashPassword encoder1 = HashPassword.getInstance();
        hash1 = encoder1.encode(password, saltKey);
        System.out.println(hash1);

        // Assume the same present in db
        HashPassword encoder2 = HashPassword.getInstance();
        hash2 = encoder2.encode(password, saltKey);
        System.out.println(hash2);

        if(hash1.equalsIgnoreCase(hash2))
            System.out.println("Both hash Matches..");
        else
            System.out.println("Hash matches fails..");
	}
	
	private static HashPassword instance;
    private final static int ITERATION_COUNT = 5;

    private HashPassword() {  }

    public static synchronized HashPassword getInstance() {
        if (instance == null) {
        	HashPassword returnPasswordEncoder = new HashPassword();
            return returnPasswordEncoder;
        }
        else
            return instance;
    }

    public synchronized String encode(String password, String saltKey)throws NoSuchAlgorithmException, IOException {
        String encodedPassword = null;
        byte[] salt = base64ToByte(saltKey);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);

        byte[] btPass = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < ITERATION_COUNT; i++) {
            digest.reset();
            btPass = digest.digest(btPass);
        }

        encodedPassword = byteToBase64(btPass);
        return encodedPassword;
    }

    private byte[] base64ToByte(String str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] returnbyteArray = decoder.decodeBuffer(str);
        return returnbyteArray;
    }

    private String byteToBase64(byte[] bt) {
        BASE64Encoder endecoder = new BASE64Encoder();
        String returnString = endecoder.encode(bt);
        return returnString;
    }

}
