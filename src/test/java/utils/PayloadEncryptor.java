package utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class PayloadEncryptor {





    public static String encryptor(String json, String secretKey) throws Exception {

//        String json = "{\"operationId\":\"da037363-ae5d-4351-9d00-1d6a71042c4d\",\"code\":200,\"data\":{\"mod_time\":\"2026-10-29T12:44:08.788Z\",\"applicant\":\"primary\"}}\n";
//        String secretKey = "46b11d228e9d2d28db7e5739329f50016ebf8c5fc8e98bb9fdbb692cffdb7025";


        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "Please configure the API_SK environment variable."
            );
        }

        String encryptedBody = encrypt(json, secretKey);

        System.out.println("Encrypted body generated: "
                + !encryptedBody.isEmpty());

        System.out.println("Encrypted body length: "
                + encryptedBody.length());

        System.out.println("Encrypted body prefix: "
                + encryptedBody.substring(0, 10));

        String decryptedJson = PayloadDecryptor.decrypt(encryptedBody, secretKey);

        System.out.println("Round-trip successful: "
                + json.equals(decryptedJson));


        System.out.println("Encrypted body: "
                + encryptedBody);
        return encryptedBody;
    }
    private static String encrypt(String plainText, String secretKey)
            throws Exception {

        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);

        // 1. Compress JSON using ZLIB
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (DeflaterOutputStream deflater =
                     new DeflaterOutputStream(outputStream)) {
            deflater.write(plainBytes);
        }

        byte[] compressedBytes = outputStream.toByteArray();
        String decimalPayload = bytesToDecimalString(compressedBytes);
        byte[] payloadBytes = decimalPayload.getBytes(StandardCharsets.UTF_8);

        // 2. Generate an 8-byte salt
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);

        // 3. Derive the AES key and IV
        byte[] derived = evpBytesToKey(
                secretKey.getBytes(StandardCharsets.UTF_8),
                salt,
                48
        );

        byte[] key = Arrays.copyOfRange(derived, 0, 32);
        byte[] iv = Arrays.copyOfRange(derived, 32, 48);

        // 4. Encrypt the compressed bytes
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(
                Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new IvParameterSpec(iv)
        );

//        byte[] ciphertext = cipher.doFinal(compressedBytes);
        byte[] ciphertext = cipher.doFinal(payloadBytes);

        // 5. Build Salted__ + salt + ciphertext
        ByteArrayOutputStream payloadOutput = new ByteArrayOutputStream();

        payloadOutput.write(
                "Salted__".getBytes(StandardCharsets.US_ASCII)
        );
        payloadOutput.write(salt);
        payloadOutput.write(ciphertext);

        // 6. Base64-encode the complete payload
        return java.util.Base64.getEncoder()
                .encodeToString(payloadOutput.toByteArray());
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
    private static String bytesToDecimalString(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                result.append(',');
            }
            result.append(bytes[i] & 0xFF);
        }

        return result.toString();
    }

}