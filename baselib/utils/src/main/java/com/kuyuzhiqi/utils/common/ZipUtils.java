package com.kuyuzhiqi.utils.common;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by chengkai on 18/1/17.
 */
public class ZipUtils {

    /**
     * 解压zip文件
     * @param zipFile 压缩文件
     * @param targetDirectory 解压目录
     * @return 是否成功
     */
    public static boolean unpackZip(File zipFile, File targetDirectory) {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                }
                if (ze.isDirectory()) {
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } finally {
                    fout.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
