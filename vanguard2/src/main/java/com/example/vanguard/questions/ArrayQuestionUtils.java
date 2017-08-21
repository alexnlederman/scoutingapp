package com.example.vanguard.questions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 8/18/2017.
 */

public class ArrayQuestionUtils {

	public static String[] getStringArrayFromProperty(Object property) {
		if (property instanceof String[]) {
			return (String[]) property;
		}
		else if (property instanceof List){
			return ((List<?>) property).toArray(new String[((List<?>) property).size()]);
		}
		return null;
	}

	public static List<String> trimList(List<String> strings) {
		List<String> trimmedStrings = new ArrayList<>();
		for (String string : strings) {
			String trimmed = string.trim();
			if (!trimmed.equals("")) {
				trimmedStrings.add(trimmed);
			}
		}
		return trimmedStrings;
	}
}
