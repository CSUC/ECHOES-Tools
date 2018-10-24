/**
 * 
 */
package org.Recollect.Core.util;

import java.io.UnsupportedEncodingException;

/**
 * @author amartinez
 *
 */
public class URLEncoder {

	public static final String SEPARATOR = "&";

    public static String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
}
}
