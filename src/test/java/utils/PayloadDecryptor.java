package utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

public class PayloadDecryptor {
    public static String decryptor(String encryptedBody,String secretKey) throws Exception {
        // Replace this placeholder with your encrypted body.
        //
//        String encryptedBody = "U2FsdGVkX19ztSwaVeggC7qs7eaEcLsw0zDBAufaCAWJWCUZ8LYvZuJoMl8AZWqPHyJYssPOXkYO2ZPip+CLQSm83RpRKB/mC0QwI69j4RfXQXPU45XRUUiGEs82la7mZlVD4ICiArxMXi6QkRrnfMCnnMCgzU5tHCDX1gOJ4sn8u5kJ6RhhpcWDsC+jbuKOACD6eYqeg0kYOqrWIzGXaL4Q//KeK8kmsqo0o/yg/8et8DOd3CWJBFUtr+0g9j7E00YCd1VPcM0ebNQynQfBhNDgeG/PsrHAPGwxU0eJzhaIQSqf0Nb0Vq/jeSckH2DF4sap2+/uZ7SuckEJAXPtvvSuGnsIKH15Zm3wCTtrojM4wHoUG7J/vtre6qcA3LSDX9sL9gNN6yYE1Jmwaz2KZaG5gX9GCjlDuoKKLg7jqBnV51IZB8NtGpWCaTREDPx4i45q2soF8k+0N1a8xY+M/oFWcpPx9+IuUgJWsQyH5ChWiw+TyCXxyz29Vy/Ie2R+HtEBo4WjiWtVh87D8JQrtNkQg+GSKaYu7xnVEzsRHgt1pgjWNr/zRonZR7RntsFj2VIWoK7DbsZip3/pjoxV8rVbMTYpfmss6s2MK3SDvxw=";
        // Set API_SK as an environment variable in your run configuration.
        //First Screen
//        String secretKey = "46b11d228e9d2d28db7e5739329f50016ebf8c5fc8e98bb9fdbb692cffdb7025";
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "Please configure the API_SK environment variable."
            );
        }

        String plaintext = decrypt(encryptedBody, secretKey);

        System.out.println("Decryption completed.");
        System.out.println("Decrypted response:");
        System.out.println(plaintext);


        return plaintext;
    }
    static String decrypt(String encryptedBody, String secretKey) throws Exception {
        // Step 1: Decode the Base64 payload
        byte[] decoded = Base64.getDecoder().decode(encryptedBody);

        //Step 2: Validate and OpenSSL salted format
        byte[] prefix = "Salted__".getBytes(StandardCharsets.US_ASCII);
        if (decoded.length < 32 || !Arrays.equals(Arrays.copyOfRange(decoded, 0, 8), prefix)) {
            throw new IllegalArgumentException("Invalid or unsupported salted payload format.");
        }

        // Step 3: Extract the 8-byte salt
        byte[] saltPrefix = Arrays.copyOfRange(decoded, 0, 8);
        byte[] salt = Arrays.copyOfRange(decoded, 8, 16);

        // Step 4: Extract the encrypted bytes
        byte[] ciphertext = Arrays.copyOfRange(decoded, 16, decoded.length);

        // AES-256 key = 32 bytes; IV = 16 bytes
        byte[] derived = evpBytesToKey(secretKey.getBytes(StandardCharsets.UTF_8), salt, 48);
        byte[] key = Arrays.copyOfRange(derived, 0, 32);
        byte[] iv = Arrays.copyOfRange(derived, 32, 48);

        // Step 5: Decrypt using AES/CBC with PKCS5 padding

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);

        byte[] decryptedBytes = cipher.doFinal(ciphertext);

        System.out.println("Decrypted byte length: " + decryptedBytes.length);
        System.out.println("First decrypted byte: "
                + (decryptedBytes.length > 0
                ? decryptedBytes[0] & 0xFF : -1));


        System.out.println("First 50 decrypted bytes as text: "
                + new String(
                decryptedBytes,
                0,
                Math.min(50, decryptedBytes.length),
                StandardCharsets.UTF_8));

        System.out.println("Decrypted text length: " + plaintext.length());
        System.out.println("Plaintext starts with: " + plaintext.substring(0, Math.min(30, plaintext.length())));
        System.out.println(plaintext);


        if (isDecimalByteList(plaintext)){
            byte[] binary = decimalStringToBytes(plaintext);
            if(binary.length>=2 && (binary[0] & 0xFF) == 0x78){
                binary = decompressZlib(binary);
            }
            plaintext = new String(binary, StandardCharsets.UTF_8);

        }else{
            byte[] binary = Base64.getDecoder().decode(plaintext);
            binary = decompressZlib(binary);
            plaintext = new String(binary, StandardCharsets.UTF_8);
        }

        System.out.println(Arrays.toString(decoded));
        System.out.println(Arrays.toString(saltPrefix));
        System.out.println(Arrays.toString(prefix));
        System.out.println(Arrays.toString(salt));
        System.out.println(Arrays.toString(ciphertext));
        System.out.println(Arrays.toString(derived));


        return plaintext;
    }

    private static byte[] decompressZlib(byte[] compressedData) throws Exception {

        try (
                ByteArrayInputStream input =
                        new ByteArrayInputStream(compressedData);
                InflaterInputStream inflater =
                        new InflaterInputStream(input);
                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {
            inflater.transferTo(output);
            return output.toByteArray();
        }
    }

    private static byte[] decimalStringToBytes(String value) {
        String[] parts = value.trim().split("\\s*,\\s*");
        byte[] result = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int number = Integer.parseInt(parts[i].trim());

            if (number < 0 || number > 255) {
                throw new IllegalArgumentException(
                        "Invalid byte value at index " + i
                );
            }

            result[i] = (byte) number;
        }
        return result;
    }

    private static byte[] evpBytesToKey(byte[] password, byte[] salt, int requiredLength) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = new byte[requiredLength];
        byte[] previous = new byte[0];
        int offset = 0;

        while (offset < requiredLength) {
            md5.reset();
            md5.update(previous);
            md5.update(password);
            md5.update(salt);
            previous = md5.digest();

            int length = Math.min(previous.length, requiredLength - offset);
            System.arraycopy(previous, 0, result, offset, length);
            offset += length;
        }
        return result;
    }

    private static boolean isDecimalByteList(String plaintext) {
        if (plaintext == null || plaintext.isBlank()) {
            return false;
        }
        String[] parts = plaintext.trim().split(",", -1);
        if (parts.length < 2) {
            return false;
        }
        for (String part : parts) {
            String number = part.trim();

            if (number.isEmpty() || number.length() > 3) {
                return false;
            }

            for (int i = 0; i < number.length(); i++) {
                if (!Character.isDigit(number.charAt(i))) {
                    return false;
                }
            }
            int value = Integer.parseInt(number);
            if (value > 255) {
                return false;
            }

        }
        return true;
    }
}