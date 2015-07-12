package com.minhtdh.common.util;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.InputStream;

public class XmlUtil {
	public static <T> T fromString(String xml, Class<T> returnType) {
		T ret = null;
		if (xml != null) {
			try {
				Serializer serializer = new Persister();
				ret = serializer.read(returnType, xml);
			} catch (Exception e) {
			}
		}
		return ret;
	}
	
	public static <T> T fromStream(InputStream is, Class<T> returnType) {
	    T ret = null;
        if (is != null) {
            try {
                Serializer serializer = new Persister();
                ret = serializer.read(returnType, is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
	
	/**
	 * COMMENTS: write a object as XML content to file
	 * @param obj
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	public static boolean toFile(Object obj, String folderPath, String fileName) {
	    boolean ret = false;
	    if (obj != null) {
	        File folder = new File(folderPath);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }
            if (folder.exists() && folder.isDirectory()) {
                File file = new File(folder, fileName);
                Serializer serializer = new Persister();
                try {
                    serializer.write(obj, file);
                    ret = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
	    }
	    return ret;
	}
}
