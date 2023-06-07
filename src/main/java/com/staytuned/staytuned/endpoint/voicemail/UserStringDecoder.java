package com.staytuned.staytuned.endpoint.voicemail;

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
//        byte[] iv = Base64.getDecoder().decode(ivString);
        byte[] iv = ivString.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);

//       byte[] iv = Base64.getDecoder().decode(encodedIV);
//       byte[] keyBytes = secretKey.getBytes("UTF-8");
//       byte[] encrypted = Base64.getDecoder().decode(encryptedData);
//
//       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//       SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
//       IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
//       cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//       byte[] decrypted = cipher.doFinal(encrypted);
//       return new String(decrypted);

   }
}



