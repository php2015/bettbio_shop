package com.bettbio.core.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordHelper {

	private static String algorithmName = "sha";

	public static String newPassword(String password) {
		return new SimpleHash(
				algorithmName, 
				password, 
				null, 
				0).toHex();
	}
	/**
	 * 生成随机密码
	 * @return
	 */
	public static String generatePassword() {  
	    String[] pa = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};  
	    StringBuffer sb = new StringBuffer("");  
	    for (int i = 0; i < 3; i++) {  
	        sb.append(pa[(Double.valueOf(Math.random() * pa.length).intValue())]);  
	    }  
	    String[] spe = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };  
	    sb.append(spe[(Double.valueOf(Math.random() * spe.length).intValue())]);  
	    sb.append((int)(Math.random()*100));  
	    return sb.toString();  
	}  

	public static void main(String[] args) {
		System.out.println(newPassword("1"));
	}
}
