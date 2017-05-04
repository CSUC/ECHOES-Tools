package org.csuc.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.testng.Assert;


/**
 * @author amartinez
 *
 */
public class AssertXML extends Assert {

    /**
     * Asserts that a <code>XmlObject</code> is valid. If it isn't, an
     * AssertionError, with the given message, is thrown.
     * 
     * @param actual
     *            the <code>XmlObject</code> to evaluate
     * @param message
     *            the assertion error message
     */
    static public void assertValid(XmlObject actual, String message) {
        final boolean condition = actual.validate();

        if (!condition) {
            String errorMessage = "expected xml <valid>, actual xml <invalid>";

            if (message != null) {
                errorMessage = message + " " + errorMessage;

                final List validationErrors = new ArrayList();
                final XmlOptions validationOptions = new XmlOptions();
                validationOptions.setErrorListener(validationErrors);
                boolean isValid = actual.validate(validationOptions);

                if (!isValid) {
                    final Iterator iter = validationErrors.iterator();
                    while (iter.hasNext()) {
                        errorMessage = message + "\n>>Problem:" + iter.next();
                    }
                }
            }

            fail(errorMessage);
        }
    }

    /**
     * Asserts that a <code>XmlObject</code> is valid. If it isn't, an
     * AssertionError is thrown.
     * 
     * @param actual
     *            the <code>XmlObject</code> to evaluate
     */
    static public void assertValid(XmlObject actual) {
        assertValid(actual, null);
    }

    /**
     * Asserts that a <code>XmlObject</code> is invalid. If it isn't, an
     * AssertionError, with the given message, is thrown.
     * 
     * @param actual
     *            the <code>XmlObject</code> to evaluate
     * @param message
     *            the assertion error message
     */
    static public void assertInvalid(XmlObject actual, String message) {
        final boolean condition = actual.validate();

        if (condition) {

            String errorMessage = "expected xml <invalid>, actual xml <valid>";
            if (message != null) {
                errorMessage = message + " " + errorMessage;
            }

            fail(errorMessage);
        }
    }

    /**
     * Asserts that a <code>XmlObject</code> is invalid. If it isn't, an
     * AssertionError is thrown.
     * 
     * @param actual
     *            the <code>XmlObject</code> to evaluate
     */
    static public void assertInvalid(XmlObject actual) {
        assertInvalid(actual, null);
    }

}
