/**
 * 
 */
package org.Recollect.Core.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.Recollect.Core.parameters.Parameters;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author amartinez
 *
 */
@SuppressWarnings("deprecation")
public class HttpOAIClient implements OAIClient {

	private static Logger logger = LogManager.getLogger(HttpOAIClient.class);
	
	private String baseUrl;
	private CloseableHttpClient httpclient;
	private int timeout = 10000; // 10 seg


	public HttpOAIClient(String baseUrl)  {
		this.baseUrl = isAvailableHost(baseUrl);
		
//		httpclient = new DefaultHttpClient(createHttpParams());
	}
	
	@Override
	public InputStream execute(Parameters parameters) {
		try {
			logger.debug(String.format("%s GET %s", baseUrl, parameters.toUrl("")));
			HttpResponse response = httpclient.execute(createGetRequest(parameters));

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK )			
				return response.getEntity().getContent();
			else
				logger.error(String.format("%s error querying service. Returned HTTP Status Code: %s", baseUrl, response.getStatusLine().getStatusCode()));//				
			return null;
		} catch (IOException e) {
			logger.error(baseUrl + " " + e);
			return null;
		}
	}

	
	
	private HttpUriRequest createGetRequest(Parameters parameters) {
		return new HttpGet(parameters.toUrl(baseUrl));
	}

	

	/**
	 * Creates a HttpParams with the options connection and socket timeout (default timeout if none is defined: 1
	 * minute)
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private HttpParams createHttpParams() {
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    HttpConnectionParams.setSoTimeout(httpParams, timeout);
	    // We only set the user agent if it was initialized. Otherwise, since we will not set this parameter,
	    // the apache default will be used.
//	    if(userAgent != null) {
//		    httpParams.setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
//	    }
		return httpParams;
	}

	
	public String isAvailableHost(String url) {		
		try {			
			SSLContext sslContext = new SSLContextBuilder()
				      .loadTrustMaterial(null, (certificate, authType) -> true).build();
		
			RequestConfig config = RequestConfig.custom()
					  .setConnectTimeout(timeout)
					  .setConnectionRequestTimeout(timeout)
					  .setSocketTimeout(timeout).build();
			
			httpclient = HttpClients.custom()
				      .setSSLContext(sslContext)
				      .setSSLHostnameVerifier(new NoopHostnameVerifier())
				      .setDefaultRequestConfig(config)
				      .build();
				    
		    HttpGet httpGet = new HttpGet(url);
		    
		    HttpClientContext context = HttpClientContext.create();
		    
		    httpclient.execute(httpGet, context);
		    List<URI> redirectURIs = context.getRedirectLocations();
		    if (redirectURIs != null && !redirectURIs.isEmpty()) {
		        for (URI redirectURI : redirectURIs) {
		        	logger.debug(String.format("%s redirect to %s", url , redirectURI));
		        }
		        return redirectURIs.get(redirectURIs.size() - 1).toString();
		    }
		    return url;
		} catch (IOException e) {
			logger.error(baseUrl + " " + e);
			return url;
		} catch (KeyManagementException e) {
			logger.error(baseUrl + " " + e);
			 return url;
		} catch (NoSuchAlgorithmException e) {
			logger.error(baseUrl + " " + e);
			 return url;
		} catch (KeyStoreException e) {
			logger.error(baseUrl + " " + e);
			 return url;
		}	
	}

	@Override
	public String getURL() {
		return baseUrl;
	}
	
	
}
