package com.example.ab.huston;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 */
public final class Decompress {
    private static final String TAG = Decompress.class.getName();

    public static void unziÁp(String zipFile, String location) throws IOException {
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        File root = createDirectory(new File(location));
        try {
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName() + "...");
                File fout = new File(root, ze.getName());
                if (ze.isDirectory()) {
                    createDirectory(fout);
                } else {
                    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fout));
                    byte[] buffer = new byte[8192];
                    int len;
                    try {
                        while ((len = zin.read(buffer)) != -1) {
                            bout.write(buffer, 0, len);
                        }
                    } finally {
                        bout.close();
                        zin.closeEntry();
                    }
                    setExecutable(fout);
                }
                setReadable(fout);
            }
        } finally {
            zin.close();
        }
    }

    public static File createDirectory(File f) throws IOException {
        if (!f.mkdirs()) {
            throw new IOException("Failed to create directory " + f.getAbsolutePath());
        }
        return f;
    }

    public static File setExecutable(File f) throws IOException {
        if (!f.setExecutable(true, true)) {
            throw new IOException("Failed to set executable " + f.getAbsolutePath());
        }
        return f;
    }

    public static File setReadable(File f) throws IOException {
        if (!f.setReadable(true, true)) {
            throw new IOException("Failed to set readable " + f.getAbsolutePath());
        }
        return f;
    }

}