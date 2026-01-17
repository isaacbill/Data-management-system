package com.student.data.management.system.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.student.data.management.system.config.StorageProperties;

public class OsPaths {

	  public static Path baseDir(StorageProperties props) {
	    String os = System.getProperty("os.name").toLowerCase();
	    String dir = os.contains("win") ? props.getBaseDirWindows() : props.getBaseDirLinux();
	    return Paths.get(dir);
	  }
	}
