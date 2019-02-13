package org.csuc.Validation.Core.schematron;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author amartinez
 */
public class Schematron {

    private Logger logger = LogManager.getLogger(Schematron.class);

    private SchematronUtil schUtil;

    public Schematron(File file) throws IOException {
        schUtil = new SchematronUtil(openSCH(), file);
    }

    public Schematron(File file, File sch){
        schUtil = new SchematronUtil(sch, file);
    }

    public boolean isValid() throws Exception {
        return schUtil.isValid();
    }

    public SVRLFailed getSVRLFailedAssert() throws Exception {
        return schUtil.getSVRLFailedAssert();
    }


    private File openSCH() throws IOException {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("SchematronEDM.sch");
        if (in == null) return null;

        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".sch");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            //copy stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        }
        return tempFile;
    }

}
