/**
 * 
 */
package org.recollect.core.client;

import java.io.InputStream;

import org.recollect.core.parameters.Parameters;


/**
 * @author amartinez
 *
 */
public interface OAIClient {
    InputStream execute (Parameters parameters) throws Exception;
    String getURL();
}
