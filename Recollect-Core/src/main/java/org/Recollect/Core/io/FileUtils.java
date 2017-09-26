package org.Recollect.Core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {

	private static Logger logger = LogManager.getLogger(FileUtils.class);
	
	private Path path;
	private String uuid = StringUtils.replaceAll(UUID.randomUUID().toString(), "-", "");
	
	public FileUtils(Path path) {		
		this.path = path;		
	}
	
	public void createDirectories() {
		try {
			Files.createDirectories(Paths.get(path + File.separator + uuid));
		} catch (IOException e) {
			try {
				Files.createDirectories(Paths.get(e.getMessage()));
				Files.createDirectories(Paths.get(path + File.separator + uuid));
			} catch (IOException e1) {
				logger.error(e);
			}
		}
	}
	
	public Path getPath() {
		return path;
	}
	
	public String getUuid() {
		return uuid;
	}
}
