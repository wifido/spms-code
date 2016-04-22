package com.sf.module.ossinterface.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

/**
 * HRS提供的解密方法
 * 第一个参数data表示加密字段的密文；第二个参数表示动态密钥，密钥由HRS提供，目前研发环境使用的密钥是：RocnRwSJF27LqoON+l2A0Q==
 * 调用方式如下： DeCoder.decrypt(密文,密钥)
 *
 * @author 文俊 (337291) Jun 20, 2014
 */
public class DeCoder {

	public static final String KEY = "RocnRwSJF27LqoON+l2A0Q==";
	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 转换密钥
	 *
	 * @param key
	 *            二进制密钥
	 * @return 密钥
	 */
	private static Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * <pre>
	 * 解密
	 * 默认使用研发环境使用的密钥
	 * 目前研发环境使用的密钥是：RocnRwSJF27LqoON+l2A0Q==
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 20, 2014
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data) throws Exception {
		return decrypt(data, KEY);
	}

	/**
	 * 解密
	 *
	 * @param data
	 *            待解密数据 加密后的密文
	 * @param key
	 *            密钥 动态密钥 由数据提供方提供
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		if (data == null) {
			return null;
		}
		BASE64Decoder base64 = new sun.misc.BASE64Decoder();
		byte[] kb = base64.decodeBuffer(key);
		Key k = toKey(kb);
		byte[] decryptData = decrypt(base64.decodeBuffer(data), k,
				DEFAULT_CIPHER_ALGORITHM);
		return new String(decryptData,"UTF-8");
	}

	private static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm)
			throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}
}