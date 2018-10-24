/**
 * 
 */
package org.Recollect.Core.client;

import java.io.IOException;
import java.io.InputStream;

import org.Recollect.Core.parameters.Parameters;


/**
 * @author amartinez
 *
 */
public interface OAIClient {
    InputStream execute (Parameters parameters) throws Exception;
    String getURL();
}
