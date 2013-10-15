package com.kienle.justinbieber.helper;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.kienle.justinbieber.Decompress;

public final class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static boolean copyDataFromAssetToSd(Context context, String fileName, String sdDir) throws IOException {
        if (checkFileExist(sdDir + "/" + fileName)) {
            Decompress d = new Decompress(sdDir + "/" + fileName, sdDir + "/");
            return d.unzip();
        }

        InputStream is = context.getAssets().open(fileName);
        OutputStream os = new FileOutputStream(sdDir + "/" + fileName);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        // Close the streams
        os.flush();
        os.close();
        is.close();

        Decompress d = new Decompress(sdDir + "/" + fileName, sdDir + "/");
        return d.unzip();
    }

    private static boolean checkFileExist(String filePath) {
        File dbfile = new File(filePath);
        return dbfile.exists();
    }

    public static void copyFile(String fileNamePath, String fileDestPath) throws IOException {
        File srcFile = new File(fileNamePath);
        InputStream inputStream = new FileInputStream(srcFile);
        FileOutputStream fileOutputStream = new FileOutputStream(fileDestPath);
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        int n;
        while ((n = inputStream.read(buf, 0, DEFAULT_BUFFER_SIZE)) > -1) {
            fileOutputStream.write(buf, 0, n);
        }
        inputStream.close();
        fileOutputStream.close();
    }

    public static ArrayList<String> extractAssetZipFile(Context context, String destPath, String zipName)
            throws IOException {

        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(zipName, AssetManager.ACCESS_STREAMING);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        ArrayList<String> entrysUnzip = new ArrayList<String>();

        while (zipEntry != null) {
            String entryName = zipEntry.getName();
            entrysUnzip.add(entryName);
            FileOutputStream fileOutputStream = new FileOutputStream(destPath + "/" + entryName);
            byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = zipInputStream.read(buf, 0, DEFAULT_BUFFER_SIZE)) > -1) {
                fileOutputStream.write(buf, 0, n);
            }

            fileOutputStream.close();
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        return entrysUnzip;
    }

    public static ArrayList<String> extractZipFile(Context context, String filePath,
            String destDirPath) throws IOException {

        ZipInputStream zipInputStream = null;
        FileOutputStream fileOutputStream = null;
        ArrayList<String> entrysUnzip = new ArrayList<String>();

        try {
            zipInputStream = null;
            fileOutputStream = null;
            File file = new File(filePath);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                File unzipEntryPath = new File(destDirPath, entryName);
                fileOutputStream = new FileOutputStream(unzipEntryPath);
                byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
                int n;
                while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
                    fileOutputStream.write(buf, 0, n);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                fileOutputStream = null;
                zipEntry = zipInputStream.getNextEntry();
                entrysUnzip.add(destDirPath + "/" + entryName);
            }
            zipInputStream.close();
            fileOutputStream = null;
            zipInputStream = null;
        } finally {
            if (fileOutputStream != null) try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (zipInputStream != null) try {
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entrysUnzip;
    }

    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            int start = TextUtils.lastIndexOf(path, '/') + 1;
            int end = path.length();
            return path.substring(start, end);
        }
        return path;
    }

    public static String fileNameWithoutExt(String path) {
        if (!TextUtils.isEmpty(path)) {
            int start = TextUtils.lastIndexOf(path, '/') + 1;
            int end = TextUtils.lastIndexOf(path, '.');
            if (end == -1) {
                end = path.length();
            }
            return path.substring(start, end);
        }
        return path;
    }

    public static String getZipFileName(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        String zipFileName = null;
        for (String fileName : assetManager.list("")) {
            if (fileName.endsWith(".zip")) {
                System.out.println("FileName : " + fileName);
                zipFileName = fileName;
            }
        }
        return zipFileName;
    }

    public static String getSdcardDir() {
        File mediaStorageDir = new File(Config.APP_FOLDER);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("KienLT", "Failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getPath();
    }

}
