package fr.mrcubee.util;

import java.io.File;

public class FileUtil {

	public static void remove(File file) {
		if ((file != null) && file.exists() && file.isFile()) {
			file.delete();
			return;
		}

		if (file.listFiles() == null)
			return;
		for (File f : file.listFiles()) {
			if (f.exists())
				remove(f);
			if (f.exists())
				f.delete();
		}
	}
}
