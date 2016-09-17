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
    public static void unzip(String zipFile, String location) throws IOException {
        ZipInputStream zin = null;
        BufferedOutputStream bout = null;
        try {
            zin = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            File root = mkdir(location);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());
                if (ze.isDirectory()) {
                    mkdir(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(new File(root, ze.getName()));
                    bout = new BufferedOutputStream(fout);
                    // FIXME:
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        bout.write(c);
                    }
                    zin.closeEntry();
                    bout.close();
                }

            }
        } finally {
            if (zin != null) {
                zin.close();
            }
            if (bout != null) {
                bout.close();
            }
        }
    }

    private static File mkdir(String dir) throws IOException {
        File f = new File(dir);
        if (!f.isDirectory() && !f.mkdirs()) {
            throw new IOException("Failed to mkdirs for " + f.getAbsolutePath());
        }
        return f;
    }
}