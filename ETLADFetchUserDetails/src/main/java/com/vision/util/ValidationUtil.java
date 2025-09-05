package com.vision.util;

public class ValidationUtil {
	static final String SECRET = "Spiral Architect";

	public static boolean isValid(Object pInput) {
		return !(pInput == null);
	}
}
