package com.staytuned.staytuned.endpoint.voicemail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.net.*;

@Slf4j
@Component
public class UserStringDecoder {

    private static final String KEY = "6PBLqyP4Q41234567890123456789012";
    private static final String ivString = "abcdefghijklmnop";

    public String decoder(String value) throws Exception {
        return AESDecrypt(UrlDecoder(value));
    }

    private String UrlDecoder(String value){
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private String AESDecrypt(String encryptedText) throws Exception {
        byte[] byteIv = ivString.getBytes();
        byte[] byteKey = KEY.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(byteKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(byteIv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}



