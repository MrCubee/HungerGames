package fr.mrcubee.util;

import java.io.File;

public class FileUtil {

	public static void delete(File file) {
		File[] files;

		if (file == null || !file.exists())
			return;
		else if (file.isFile()) {
			file.delete();
			return;
		}
		files = file.listFiles();
		if (files == null)
			return;
		for (File f : files)
			delete(f);
		file.delete();
	}
}
