/**
 * 
 */
package org.Recollect.Core.typesafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;

/**
 * @author amartinez
 *
 */
public class TypesafeConfig {

	private static Logger logger = LogManager.getLogger(TypesafeConfig.class);
			
	private List<? extends ConfigObject> objectList;
	private List<CollectionConfig> collectionConfig;
	private int forkJoinPool;
	private String applicationFolder;
	
	public TypesafeConfig(String file) {
		Config config = ConfigFactory.load(ConfigFactory.parseFile(new File(file)));
		
		logger.debug(config);
		
		objectList = config.getObjectList("config");
		forkJoinPool = config.getInt("ForkJoinPool");
		applicationFolder = config.getString("applicationFolder");
		
		collectionConfig = 
			objectList.stream().map(m-> {
				try {
					return new ObjectMapper().readValue(m.render(ConfigRenderOptions.concise()), CollectionConfig.class);
				} catch (IOException e1) {
					return null;
				}
			})
			.collect(Collectors.toList());
		
		
		createFolders();
		logger.info(String.format("ApplicationConfig\nForkJoinPool [%s]\napplicationFolder %s\ncollections %s", forkJoinPool, applicationFolder, collectionConfig));
	}
	
	public List<? extends ConfigObject> getObjectList() {
		return objectList;
	}
	
	public List<CollectionConfig> getCollectionConfig() {
		return collectionConfig;
	}
	
	public String getApplicationFolder() {
		return applicationFolder;
	}
	
	public int getForkJoinPool() {
		return forkJoinPool;
	}
	
	private void createFolders() {
		collectionConfig.forEach(c->{
			c.getSets().forEach(set->{
				Path path = Paths.get(applicationFolder + File.separator + c.getName() + File.separator + set);
				if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
					try {
						Files.createDirectories(path);						
						logger.debug(String.format("created folder %s", path));
					} catch (IOException e) {
						logger.error(e);
					}
				}
			});
		});
	}

}
