package com.yhjr.basic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZIP文件或目录压缩工具
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2018年6月8日
 */
public final class MyFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFileUtils.class);

    public static boolean writeStringToFile(String sourceFilePath, String data) {
        return writeStringToFile(sourceFilePath, data, null);
    }
    
    public static boolean writeStringToFile(String sourceFilePath, String data,Charset charset) {
        if(charset==null){
            //charset=Charset.forName("utf-8");
            charset=Charset.forName("GBK");
        }
        try {
            FileUtils.writeStringToFile(new File(sourceFilePath), data, charset, true);
        } catch (IOException e) {
            LOGGER.error("写入文件信息异常!",e);
            return false;
        }
        return true;
    }
    
    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     * 
     * @param sourceFilePath :待压缩的文件路径或文件名
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称(不包含文件名后缀信息)
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String zipFileName) {
        return fileToZip(sourceFilePath, zipFilePath + File.separator + zipFileName);
    }

    /**
     * 
     * @param sourceFilePath 待压缩的文件或文件夹
     * @param zipFileFullName 压缩后的文件全路径+文件名信息(不包含文件名后缀信息)
     * @return
     * @see ZipFileUtil.zipFiles(...)
     */
    @Deprecated
    public static boolean fileToZip(String sourceFilePath, String zipFileFullName) {
        if(StringUtils.isBlank(zipFileFullName)){
            zipFileFullName=sourceFilePath+".zip";
            //return false;
        }else{
            zipFileFullName+=".zip";
        }
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            LOGGER.error("待压缩的文件或目录：{}不存在." , sourceFilePath);
            return flag;
        }
        try {
            File zipFile = new File(zipFileFullName);
            if(zipFile.isDirectory()){
                LOGGER.error("压缩后的目标文件信息不正确,不应该是一个目录{}文件!" , zipFileFullName);
                return flag;
            }
            if (!zipFile.exists()) {
                LOGGER.warn("压缩后的目标文件：{}不存在,进行创建." , zipFileFullName);
                zipFile.getParentFile().mkdirs(); 
            }else{
                LOGGER.warn("压缩后的目标文件：{}已经存在了." , zipFileFullName);
            }

            int bufSize = 1024 * 10;
            byte[] bufs = new byte[bufSize];
            if (!sourceFile.isDirectory()) {
                LOGGER.warn("待压缩的不是目录文件：{}，直接对该文件进行压缩.",sourceFilePath);
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                zos.putNextEntry(zipEntry);
                // 读取待压缩的文件并写进压缩包里
                fis = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, bufSize)) != -1) {
                    zos.write(bufs, 0, read);
                }
                flag = true;
            }else{
                LOGGER.warn("待压缩的是目录文件：{}，对该文件目录进行压缩.",sourceFilePath);
                Collection<File> sourceFiles = FileUtils.listFiles(sourceFile, null, true);
//                File[] array = ArrayUtils.toArray();
                ArrayList<File> arrayList = new ArrayList<>(sourceFiles);
//                File[] sourceFiles = sourceFile.listFiles();
//                if (ArrayUtils.isNotEmpty(sourceFiles)) {
                if (CollectionUtils.isNotEmpty(arrayList)) {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    for (int i = 0; i < arrayList.size(); i++) {
                        // 创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(arrayList.get(i).getName());
                        zos.putNextEntry(zipEntry);
                        // 读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(arrayList.get(i));
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, bufSize)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }
                    flag = true;
                }
            }
        } catch ( IOException e) {
            LOGGER.error("压缩文件/目录信息异常!",e);
            //throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (null != bis)
                    bis.close();
                if (null != zos)
                    zos.close();
            } catch (IOException e) {
                LOGGER.error("压缩文件/目录关闭流信息异常!",e);
                //throw new RuntimeException(e);
            }
        }
        return flag;
    }
    
    private MyFileUtils() {
    }
    

}