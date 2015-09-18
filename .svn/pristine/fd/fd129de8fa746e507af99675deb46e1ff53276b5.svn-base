package com.example.RSA;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class Key {

	private static String PublicModulus = "a2d1974d7a5d6a137e7062c194804c66ff93689263a94c46e8e6e2801dc65b5caea07a16de230f7c25f45a1fa9336fa7a631f9fa4c5911d29d6402af6f22a3f454f4d3891da77f";
	private static String PublicExponent = "10001";
	
	
	public static RSAPublicKey getPublicKey(){
		try {
			return RSAUtils568.getPublicKey(PublicModulus, PublicExponent);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
