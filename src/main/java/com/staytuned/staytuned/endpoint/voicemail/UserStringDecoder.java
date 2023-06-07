package com.staytuned.staytuned.endpoint.voicemail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.net.*;

@Slf4j
@Component
public class UserStringDecoder {

    private static final String KEY = "6PBLqyP4Q41234567890123456789012";
    private static final String ivString = "abcdefghijklmnop";

    public String decoder(String value) throws Exception {
        return AESDecrypt(value);
    }

    private String AESDecrypt(String encryptedText) throws Exception {
        byte[] iv = ivString.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}



