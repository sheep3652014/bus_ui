package com.example.RSA;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;



public class RSAUtils568 {
	  private static String RSA = "RSA";
	   //RSA/ECB/NoPadding  RSA/ECB/PKCS1Padding
	 private static String RSA_JAVA="RSA/ECB/PKCS1Padding";
	 
	 /**
		 * 闅忔�?鐢熸垚RSA瀵嗛挜�?��(榛樿�?�嗛挜闀垮害涓�1024)
		 * 
		 * @return
		 */
		public static KeyPair generateRSAKeyPair()
		{
			return generateRSAKeyPair(1024);
		}
		/**
		 * 鐢熸垚�?�嗛挜�?��
		 * @param keyLength
		 * @return
		 */
		public static KeyPair generateRSAKeyPair(int keyLength){
			try
			{
				KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
				kpg.initialize(keyLength,new SecureRandom());
				return kpg.genKeyPair();
			} catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			} 
			return null;
		}
		
		/**
		 * 浣跨敤N銆乪鍊艰繕鍘熷叕閽�
		 * 
		 * @param modulus
		 * @param publicExponent
		 * @return
		 * @throws NoSuchAlgorithmException
		 * @throws InvalidKeySpecException
		 */
		public static RSAPublicKey getPublicKey(String modulus, String publicExponent)
				throws NoSuchAlgorithmException, InvalidKeySpecException
		{
			KeyFactory keyFac = null;
			try {
				keyFac = KeyFactory.getInstance(RSA);
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}
			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
					modulus, 16), new BigInteger(publicExponent, 16));
			try {
				return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
			} catch (InvalidKeySpecException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/**
		 * 浣跨敤N銆乨鍊艰繕鍘熺閽�
		 * 
		 * @param modulus
		 * @param privateExponent
		 * @return
		 * @throws NoSuchAlgorithmException
		 * @throws InvalidKeySpecException
		 */
		public static RSAPrivateKey getPrivateKey(String modulus, String privateExponent)
				throws NoSuchAlgorithmException, InvalidKeySpecException
		{
			KeyFactory keyFac = null;
			try {

				keyFac = KeyFactory.getInstance(RSA);
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}
			RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
					modulus, 16), new BigInteger(privateExponent, 16));
			try {
				return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
			} catch (InvalidKeySpecException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/**
		 * 鍔犲�? <br>
		 * 姣忔鍔犲瘑鐨勫瓧鑺傛暟锛屼笉鑳借秴杩囧瘑閽ョ殑�?垮害鍊煎噺鍘�?11
		 * 
		 * @param data
		 *            闇�鍔犲瘑鏁版嵁鐨刡yte鏁版�?
		 * @param pubKey
		 *            鍏�?
		 * @return 鍔犲瘑鍚庣殑byte鍨嬫暟鎹�?
		 */
		public static byte[] encryptData(byte[] data, Key key)
		{
			try
			{
				Cipher cipher = Cipher.getInstance(RSA_JAVA);
				// 缂栫爜鍓嶈瀹氱紪鐮佹柟寮忓強�?�嗛�?
				cipher.init(Cipher.ENCRYPT_MODE, key);
				// 浼犲叆缂栫爜鏁版嵁骞惰繑鍥炵紪鐮佺粨鏋�
				return cipher.doFinal(data);
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		public static String encryptDataStr(String data, Key key)
		{
			return StringCls.Bytes2HexString(encryptData(StringCls.HexString2Bytes(data, false), key), false);
			
		}
		/**
		 * 瑙ｅ�?
		 * 
		 * @param encryptedData
		 *            缁忚繃encryptedData()鍔犲瘑杩斿洖鐨刡yte鏁版�?
		 * @param privateKey
		 *            绉侀�?
		 * @return
		 */
		public static byte[] decryptData(byte[] encryptedData, Key key)
		{
			try
			{
				Cipher cipher = Cipher.getInstance(RSA_JAVA);
				cipher.init(Cipher.DECRYPT_MODE, key);
				return cipher.doFinal(encryptedData);
			} catch (Exception e)
			{
				return null;
			}
		}
		public static String decryptDataStr(String encryptedData, Key key){
			return StringCls.Bytes2HexString(decryptData(StringCls.HexString2Bytes(encryptedData, false), key), false);
		}
}
