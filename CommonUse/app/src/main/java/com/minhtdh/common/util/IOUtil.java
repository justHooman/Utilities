/*
 * Copyright (C) 2013 TinhVan Outsourcing.
 */
package com.minhtdh.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author MinhTDH Aug 26, 2013
 */
public class IOUtil {
    public static boolean saveImageToSdcard(byte[] bytes, String path, String fileName) {
        File wallpaperDirectory = new File(path);
        // have the object build the directory structure, if needed.
        wallpaperDirectory.mkdirs();
        // create a File object for the output file
        File f = new File(wallpaperDirectory, fileName);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fo = null;
        boolean ret = false;
        try {
            f.createNewFile();
            fo = new FileOutputStream(f);
            fo.write(bytes);
            fo.flush();
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (Exception exp) {}
            }
        }
        return ret;
    }
}
