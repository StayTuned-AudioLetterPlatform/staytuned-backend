package com.staytuned.staytuned.endpoint.voicemail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class AES256 {

    private static final String KEY = "6PBLqyP4Q41234567890123456789012";
    private static final String ivString = "abcdefghijklmnop";

    public String decoder(String value) throws Exception {
        return AESDecrypt(value);
    }

    private String AESDecrypt(String encryptedText) throws Exception {
        byte[] byteIv = ivString.getBytes();
        byte[] byteKey = KEY.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(byteKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(byteIv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] decoded = Base64Utils.decodeFromUrlSafeString(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    public String AESEncrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] byteIv = ivString.getBytes();
            byte[] byteKey = KEY.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(byteKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(byteIv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encodeToUrlSafeString(encryptData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("encrypt fail : " + e.getMessage());
        }
    }
}



