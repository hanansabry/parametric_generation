package com.se.common;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Make_Zip_file {

	private static void copy(InputStream in, OutputStream out)
			throws IOException {

		synchronized (in) {
			synchronized (out) {
				byte buffer[] = new byte[15048];
				do {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					out.write(buffer, 0, bytesRead);
				} while (true);
			}
		}
	}

	public static void makeZipFile(List<String> files, String path)
			throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				new File(path)));
		zos.setLevel(0); 
		for (int i = 0; i < files.size(); i++) {
			ZipEntry zen = new ZipEntry(files.get(i));
			FileInputStream fins = new FileInputStream(files.get(i));
			zos.putNextEntry(zen);
			copy(fins, zos);
			zos.closeEntry();
			fins.close();
			File f = new File(files.get(i));
			f.delete();
		}
		zos.close();
	}
}