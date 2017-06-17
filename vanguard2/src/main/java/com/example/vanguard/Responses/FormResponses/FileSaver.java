package com.example.vanguard.Responses.FormResponses;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by BertTurtle on 6/12/2017.
 */

public class FileSaver {

	public static final String filePath = "/path/for/app";

	// TODO set this up so that it will save responses.
	public static Object getFile(Context context, String fileName, Object defaultValue) {
		File dir = new File(context.getFilesDir() + filePath);
		dir.mkdirs();
		File file = new File(dir, fileName);
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object value = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			value = in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			saveFile(context, fileName, defaultValue);
		}
		return value == null ? defaultValue : value;
	}

	public static void saveFile(Context context, String fileName, Object value) {
		File dir = new File(context.getFilesDir() + filePath);
		dir.mkdirs();
		File file = new File(dir, fileName);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(value);
			out.close();
		} catch (Exception i) {
			i.printStackTrace();
		}
	}
}
