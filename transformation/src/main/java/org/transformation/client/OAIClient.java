/**
 * 
 */
package org.transformation.client;

import java.io.InputStream;

import org.transformation.parameters.Parameters;


/**
 * @author amartinez
 *
 */
public interface OAIClient {
    InputStream execute (Parameters parameters) throws Exception;
    String getURL();
}
