package com.github.tx.jsite.utils;

import com.github.tx.common.security.Digests;
import com.github.tx.common.util.Encodes;
import com.github.tx.jsite.modules.sys.security.ShiroAuthorizingRealm;

public class DigestUtil {
	
	/**
	 * 随机生成盐值字符串
	 * @return 盐值
	 */
	public static String generateSalt() {
		byte[] salt = Digests.generateSalt(ShiroAuthorizingRealm.SALT_SIZE);
		return Encodes.encodeHex(salt);
	}
	
	/**
	 * 基于SHA1算法生成密码散列值
	 * @param plainPassword 密码明文
	 * @param salt 盐值
	 * @return 密码散列值
	 */
	public static String generateHash(String plainPassword, String salt) {
		byte[] saltByte = Encodes.decodeHex(salt);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), saltByte, ShiroAuthorizingRealm.HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码是否相符
	 * @param plainPassword 密码明文
	 * @param password 数据库中的密码散列值
	 * @param salt 盐值
	 * @return 
	 */
	public static boolean isCredentialsMatch(String plainPassword, String password, String salt){
		String hashPassword = generateHash(plainPassword, salt);
		return hashPassword.equals(password);
	}
}
