package org.csuc.entities.quality;

/**
 * @author amartinez
 */
public class Schematron {

    private String test;
    private String message;

    public Schematron() {
    }

    public Schematron(String test, String message) {
        this.test = test;
        this.message = message;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
