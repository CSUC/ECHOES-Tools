package org.csuc.echoes.gui.consumer.quality.schematron;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author amartinez
 */
public class Schematron {

    public static boolean isValid(File xml) throws Exception {
        final ISchematronResource aResPure = new SchematronResourcePure(new ClassPathResource("schematron.sch"));

        if (!aResPure.isValidSchematron())
            throw new IllegalArgumentException("Invalid Schematron!");

        return aResPure.getSchematronValidity(new StreamSource(xml)).isValid();
    }

    public static List<SVRLFailedAssert> getSVRLFailedAssert(File xml) throws Exception {
        final ISchematronResource aResPure = new SchematronResourcePure(new ClassPathResource("schematron.sch"));

        if (!aResPure.isValidSchematron())
            throw new IllegalArgumentException("Invalid Schematron!");

        List<SVRLFailedAssert> failedAsserts = SVRLHelper
                .getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(new StreamSource(xml)));

        return failedAsserts;
    }
}
