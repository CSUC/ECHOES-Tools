package org.csuc.step;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.entity.QualityDetails;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Schematron implements StepInterface {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    private Content content;

    public Schematron(Content content) {
        logger.info("{}", getClass().getSimpleName());
        this.content = content;
    }

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

    public static boolean isValid(InputStream inputStream) throws Exception {
        final ISchematronResource aResPure = new SchematronResourcePure(new ClassPathResource("schematron.sch"));

        if (!aResPure.isValidSchematron())
            throw new IllegalArgumentException("Invalid Schematron!");

        return aResPure.getSchematronValidity(new StreamSource(inputStream)).isValid();
    }

    public static List<SVRLFailedAssert> getSVRLFailedAssert(InputStream inputStream) throws Exception {
        final ISchematronResource aResPure = new SchematronResourcePure(new ClassPathResource("schematron.sch"));

        if (!aResPure.isValidSchematron())
            throw new IllegalArgumentException("Invalid Schematron!");

        List<SVRLFailedAssert> failedAsserts = SVRLHelper
                .getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(new StreamSource(inputStream)));

        return failedAsserts;
    }

    @Override
    public QualityDetails quality(Path path) throws Exception {
        QualityDetails qualityDetails = new QualityDetails(path.getFileName().toString());

        if (isValid(path.toFile())) {
            qualityDetails.setValidSchematron(true);

            if (Objects.nonNull(content)) {
                QualityDetails q = content.quality(path);

                qualityDetails.setEdm(q.getEdm());
                qualityDetails.setValidContent(q.isValidContent());
            }

        } else {
            try {
                qualityDetails.setSchematron(
                        getSVRLFailedAssert(path.toFile())
                                .stream()
                                .map(m -> new org.csuc.dao.entity.Schematron(m.getTest(), m.getText()))
                                .collect(Collectors.toList())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return qualityDetails;
    }

    @Override
    public QualityDetails quality(URL url) throws Exception {
        QualityDetails qualityDetails = new QualityDetails(url.toString());

        if (isValid(url.openStream())) {
            qualityDetails.setValidSchematron(true);

            if (Objects.nonNull(content)) {
                QualityDetails q = content.quality(url);

                qualityDetails.setEdm(q.getEdm());
                qualityDetails.setValidContent(q.isValidContent());
            }

        } else {
            try {
                qualityDetails.setSchematron(
                        getSVRLFailedAssert(url.openStream())
                                .stream()
                                .map(m -> new org.csuc.dao.entity.Schematron(m.getTest(), m.getText()))
                                .collect(Collectors.toList())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return qualityDetails;
    }
}
