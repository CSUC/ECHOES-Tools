package org.csuc.loadBlazegraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 
 * @author amartinez
 *
 */
public class App {
	
    private static Logger logger = LogManager.getLogger(App.class);
	
	private static String host = "localhost"; //default
	private static int port = 9999; //default	
	private static String contentType = "application/rdf+xml"; //default
	private static String context = "blazegraph"; //default
	private static String charset = Charsets.UTF_8.name(); //default
	private static int threads = 4; // default;
	private static String folderOrFile = null;
	private static String namespace = "kb"; //default
	
	
	/**
	 * 
	 * @param args
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void main( String[] args ) throws ClientProtocolException, IOException {
		Instant now = Instant.now();		
		
		logger.info(String.format("start %s %s", logger.getName(), now));
		
		for(int i = 0; i < args.length; i++){
			if(args[i].equals("--host"))	host = args[i+1];
			if(args[i].equals("--port"))	port = Integer.parseInt(args[i+1]);
			if(args[i].equals("--folderOrFile"))	folderOrFile = args[i+1];
			if(args[i].equals("--contentType"))	contentType = args[i+1];			
			if(args[i].equals("--context"))	context = args[i+1];
			if(args[i].equals("--charset"))	charset = args[i+1];
			if(args[i].equals("--threads"))	threads = Integer.parseInt(args[i+1]);
			if(args[i].equals("--namespace"))	namespace = args[i+1];
		}
		
		logger.info(
			String.format(
				"Properties: [host: %s, port: %s, contentType: %s, context: %s, charset: %s, threads: %s, folderOrFile: %s]",
				host, port, contentType, context, charset, threads, folderOrFile));
		
		try {
			if(Objects.nonNull(folderOrFile)) {
				new ForkJoinPool(threads).submit(()->{
					try{
						Files.walk(Paths.get(folderOrFile))
						.filter(Files::isRegularFile)
				        .filter(f -> f.toString().endsWith(".xml"))
				        .parallel()
				        .forEach(f->{
			        		httpPost(contentType, f.toFile());
				        });
					} catch (IOException e) {
						logger.error(e);
					}					
				}).join();
			}else{
				logger.info("select folderOrFile");
			}			
		}finally{
			long diff  = Duration.between(now, Instant.now()).getSeconds();
			logger.info(String.format("end %s %02d:%02d:%02d", logger.getName(), diff / 3600, diff % 3600 / 60, diff % 60));
		}		
    }
	
	/**
	 * 
	 * 
	 * @param type
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void httpPost(String type, File data) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
	    HttpPost httppost = new HttpPost(String.format("http://%s:%s/%s/namespace/%s/sparql", host, port, context, namespace));

	    RequestConfig requestConfig = 
	    		RequestConfig.copy(RequestConfig.DEFAULT)
	    			.setProxy(new HttpHost(host, port))
	    			.build();
	    
	    httppost.setConfig(requestConfig);

	    httppost.addHeader("content-type", type + ";charset=" + charset);

	    FileEntity entity = new FileEntity(data);

	    httppost.setEntity(entity);

	    //logger.info("executing request " + httppost.getRequestLine() + httppost.getConfig());	    
	    try{
	    	HttpResponse response = httpclient.execute(httppost);
		    HttpEntity resEntity = response.getEntity();

		    if (resEntity != null) {		    	
		    	BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
		    	if(Objects.nonNull(blazegraphResponse)){
		    		if(Integer.parseInt(blazegraphResponse.getModified()) != 0) {
		    			logger.info(String.format("%s %s %s", "OK", data.getAbsolutePath(), blazegraphResponse.toString()));
		    		
		    			Files.delete(Paths.get(data.toURI()));
		    			logger.info(String.format("%s deleted %s", "OK", data));
		    		}else
			    		logger.error(String.format("%s %s %s", "ERROR", data.getAbsolutePath(), blazegraphResponse.toString()));
		    	}else
		    		logger.error(String.format("%s %s", "ERROR", data.getAbsolutePath()));
		    			    	
		    }
		    httpclient.close();
		    
	    }catch( IOException e){
	    	logger.error(String.format("%s %s\n%s", "ERROR", data.getAbsolutePath(), e));
	    }	    
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static BlazegraphResponse xmlMapper(String value) throws JsonParseException, JsonMappingException, IOException {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(true);
  	  	XmlMapper xmlMapper = new XmlMapper(module);	    
		return xmlMapper.readValue(value, BlazegraphResponse.class);		   
	}
}
