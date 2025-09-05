package com.vision.util;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CommonUtils {
	public static String getattributeValues(String s, String infoPattern) {
		String returnValuue = null;
		String pattern = infoPattern+"=([^|]*)\\|\\|";
		Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);
        if (m.find()) {
        	returnValuue = m.group(1);
        }
        return returnValuue;
	}
}
