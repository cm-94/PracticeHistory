package com.spring.elderlycare.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class SHAUtil {
	public String encryptSHA256(String str) {
		String sha = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer buf = new StringBuffer();
			for(int i = 0; i < byteData.length; i++) {
				buf.append(Integer.toString((byteData[i]&0xff)+0x100, 16).substring(1));
			}
			sha = buf.toString();
		}catch(NoSuchAlgorithmException e) {
			System.out.println("Encrypt error - NoSuchAlgorithmException");
			sha = null;
		}
		return sha;
	}
}
