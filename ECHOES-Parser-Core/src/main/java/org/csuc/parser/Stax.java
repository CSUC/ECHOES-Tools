/**
 * 
 */
package org.csuc.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Stax implements ParserStrategy{

	private static Logger logger = LogManager.getLogger(Stax.class.getName());
			
	public Stax(String url) {

		try{
			XMLInputFactory factory = XMLInputFactory.newInstance();
		    //Reader fileReader = new FileReader("Source.xml");
		    XMLEventReader reader = factory.createXMLEventReader(new URL(url).openStream());

		    while (reader.hasNext()) {
		      XMLEvent event = reader.nextEvent();
		      if (event.isStartElement()) {
		        StartElement element = (StartElement) event;
		        System.out.println("Start Element: " + element.getName());
		      }
		      if (event.isCharacters()) {
		        Characters characters = (Characters) event;
		        System.out.println("Text: " + characters.getData());
		      }
		    }
		}catch(FileNotFoundException e) {			
			logger.error(e);
		}catch(XMLStreamException e){
			logger.error(e);
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} 
	}

	@Override
	public void execute() {
		
	}

	@Override
	public Map<String, String> getMapValues() {
		return null;
	}

	@Override
	public Map<String, String> getNamespaces() {
		return null;
	}

	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		return null;
	}
}
