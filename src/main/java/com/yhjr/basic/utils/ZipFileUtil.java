package com.yhjr.basic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 压缩算法 实现文件压缩，文件夹压缩，以及文件和文件夹的混合压缩 
 * 完成的结果文件--输出的压缩文件
 */
public class ZipFileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipFileUtil.class);
    
    /**
     * 压缩文件
     * 
     * @param srcfilePath
     * @param targetZipFile
     */
    public static boolean zipFiles(String srcfilePath, String targetZipFile) {
        boolean resultFlag=false;
        File srcfile = new File(srcfilePath);
        if (!srcfile.exists()) {
            LOGGER.error(" 待压缩源文件信息[{}]不正确!",srcfilePath);
            return resultFlag;
        }
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(targetZipFile));
            if (srcfile.isFile()) {
                zipFile(srcfile, out, "");
            } else {
                File[] list = srcfile.listFiles();
                for (int i = 0; i < list.length; i++) {
                    compress(list[i], out, "");
                }
            }
            resultFlag=true;
            LOGGER.error(" 压缩源文件信息[{}]结束!",srcfilePath);
        } catch (Exception e) {
            LOGGER.error(" 压缩源文件信息异常!",e);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                LOGGER.error(" 压缩源文件信息关闭流异常!",e);
            }
        }
        return resultFlag;
    }

    /**
     * 压缩单个文件
     * @param srcfile
     */
    private static void zipFile(File srcfile, ZipOutputStream out, String basedir) {
        if (!srcfile.exists()){
            LOGGER.error(" 待压缩源文件信息[{}]不正确!",srcfile.getPath());
            return;
        }
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            int len;
            in = new FileInputStream(srcfile);
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            LOGGER.error(" 压缩单个文件信息异常!",e);
        } finally {
            try {
                if (out != null)
                    out.closeEntry();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                LOGGER.error(" 压缩单个文件信息关闭流异常!",e);
            }
        }
    }

    /**
     * 压缩文件夹里的文件 起初不知道是文件还是文件夹 统一调用该方法
     * 
     * @param file
     * @param out
     * @param basedir
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }

    /**
     * 压缩文件夹
     * 
     * @param dir
     * @param out
     * @param basedir
     */
    private static void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + File.separator);
        }
    }

    /**
     * 功能:解压缩
     * 
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    @SuppressWarnings({ "resource", "rawtypes", "unused" })
    public void unZipFiles(File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            String name = zf.getName().substring(zf.getName().lastIndexOf('\\') + 1, zf.getName().lastIndexOf('.'));
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                System.out.println("zipEntryName---" + descDir + zipEntryName);
                InputStream in = zf.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(new File(descDir + zipEntryName));
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                LOGGER.info(" 解压缩完成");
            }
        } catch (Exception e) {
            LOGGER.error(" 解压缩文件信息异常!",e);
        }
    }

}