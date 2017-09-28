/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.SAXParser;

import org.apache.commons.io.FileExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * @author amartinez
 *
 */
public class FileParser {

	private Logger logger = LogManager.getLogger(FileParser.class);
	
	private Path path;
	
	private AtomicInteger iter = new AtomicInteger(0);	
	
	
	public FileParser(String fileOrPath) throws FileExistsException {
		this.path = Paths.get(fileOrPath);
		if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) throw new FileExistsException(String.format("%s file not exists", path));
	}
	
	
	public void parser(SAXParser saxparser, FragmentContentHandler contentHandler){
		if(Objects.nonNull(path) && Files.exists(this.path, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.walk(this.path)				
				.filter(Files::isRegularFile)
				.filter(f-> f.toString().endsWith(".xml"))
				.forEach(f->{					
					logger.info(String.format("%s file: %s", iter.incrementAndGet(), f.getFileName()));
					try {						
						saxparser.parse(f.toFile(), contentHandler);
					} catch (SAXException | IOException e) {
						logger.error(e);
					}				
				});
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}		
	
}
