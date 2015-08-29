package com.se.common;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Maged_Ibrahim
 */
public class CompressFiles {

    static final int BUFFER = 2048;

    public CompressFiles() {
    }

    public static void compressFiles(List<File> files, String compressFilePath) throws FileNotFoundException, IOException {

        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(compressFilePath);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte data[] = new byte[BUFFER];

        for (int i = 0; i < files.size(); i++) {

            FileInputStream fi = new FileInputStream(files.get(i));
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files.get(i).getName());
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0,BUFFER)) != -1) {
                out.write(data, 0, count);
            }

            origin.close();
            files.get(i).delete();

        }

        out.close();
    }

    public static void compressFile(File file, String compressFilePath) throws FileNotFoundException, IOException {

        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(compressFilePath);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte data[] = new byte[BUFFER];

        FileInputStream fi = new FileInputStream(file);
        origin = new BufferedInputStream(fi, BUFFER);
        ZipEntry entry = new ZipEntry(file.getName());
        out.putNextEntry(entry);
        int count;

        while ((count = origin.read(data, 0,
                BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
        file.delete();
        out.close();
    }

    public static void deCompressFile(File compressFile) {
        try {
            String destinationname = compressFile.getParent();
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(
                    new FileInputStream(compressFile));

            zipentry = zipinputstream.getNextEntry();

            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
//                String directory = newFile.getParent();
//
//                if(directory == null)
//                {
//                    if(newFile.isDirectory())
//                        break;
//                }

                fileoutputstream = new FileOutputStream(
                        destinationname + "\\" + entryName);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }//while

            zipinputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    	ArrayList<File> files =  new ArrayList<File>();
    	files.add(new File("D:\\Parametric_Generation\\vertical_export_0.21266818209359162.txt"));
    	files.add(new File("D:\\Parametric_Generation\\Gates.txt"));
        compressFiles(files, "D:\\Parametric_Generation\\hoda_baby2.zip");
        
    }
}
