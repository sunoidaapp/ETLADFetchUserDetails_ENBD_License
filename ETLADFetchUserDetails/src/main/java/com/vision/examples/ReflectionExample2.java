package com.vision.examples;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class UserSample {
	private int userID;
	private String userName = "";

	public UserSample() {}

	public UserSample(int userID, String userName) {
		this.setUserID(userID);
		this.setUserName(userName);
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

class UserSample2 {
	public int userID;
	public String userName = "";

	public UserSample2() {}

	public UserSample2(int userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}

}

public class ReflectionExample2 {
	public static void main(String[] args) {
		try {
			method2();
		} catch (Exception e) {
		}
	}
	
	//with getters & setters
	public static void method1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final String uniqueCombo = "UserID,UserName";
		String comboArr[] = uniqueCombo.split(",");
		List<UserSample> objList = Arrays.asList(new UserSample(1, "DD"), new UserSample(2, "Rekha"));
		int index = 1;
		for(UserSample userVb : objList) {
			StringBuffer uniqueValueBuf = new StringBuffer();
			Class cls = userVb.getClass();
			for(String strStr : comboArr) {
				Method method = cls.getDeclaredMethod("get"+strStr);
				uniqueValueBuf.append(method.invoke(userVb));
			}
			// System.out.println(index+"-"+uniqueValueBuf);
			index++;
		}
	}
	
	//without getters & setters
	public static void method2() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final String uniqueCombo = "userID,userName";
		String comboArr[] = uniqueCombo.split(",");
		List<UserSample2> objList = Arrays.asList(new UserSample2(1, "DD"), new UserSample2(2, "Rekha"));
		int index = 1;
		for(UserSample2 userVb : objList) {
			StringBuffer uniqueValueBuf = new StringBuffer();
			Class cls = userVb.getClass();
			for(String strStr : comboArr) {
				Field f = cls.getDeclaredField(strStr);
				uniqueValueBuf.append(f.get(userVb));
			}
			// System.out.println(index+"-"+uniqueValueBuf);
			index++;
		}
	}
	
}
