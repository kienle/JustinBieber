package com.kienle.justinbieber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class Decompress {
    private String _zipFile;
    private String _location;

    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public boolean unzip() {
        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    File file = new File(_location + ze.getName());
                    if (file.exists()) {
                        continue;
                    }
                    FileOutputStream fout = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zin.read(buffer)) > 0) {
                        fout.write(buffer, 0, length);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
            return true;
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
            return false;
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}