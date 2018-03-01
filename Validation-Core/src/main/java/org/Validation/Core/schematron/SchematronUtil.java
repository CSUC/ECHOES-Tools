/**
 * 
 */
package org.Validation.Core.schematron;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.transform.stream.StreamSource;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import org.apache.logging.log4j.message.StringMapMessage;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

/**
 * @author amartinez
 *
 */
public class SchematronUtil {

	private final File sch;
	private final StreamSource xml;
	private ISchematronResource aResPure;

	public SchematronUtil(@Nonnull File schema, @Nonnull File xml) {
		this.sch = schema;
		this.xml = new StreamSource(xml);
	}

	public boolean isValid() throws Exception {
		aResPure = SchematronResourcePure.fromFile(sch);
		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		return aResPure.getSchematronValidity(xml).isValid();
	}
//
//
//	public String getReport() throws Exception {
//		StringBuffer buffer = new StringBuffer();
//
//		if (!aResPure.isValidSchematron())
//			throw new IllegalArgumentException("Invalid Schematron!");
//
//        SVRLHelper.getAllSuccessfulReports(aResPure.applySchematronValidationToSVRL(xml)).forEach(svrlSuccessfulReport -> {
//            buffer.append(String.format("\tTest: %s\n", svrlSuccessfulReport.getTest()));
//            buffer.append(String.format("\tFlag: %s\n", svrlSuccessfulReport.getFlag()));
//            buffer.append(String.format("\tRol: %s\n", svrlSuccessfulReport.getRole()));
//            buffer.append(String.format("\tText: %s\n", svrlSuccessfulReport.getText()));
//            buffer.append(String.format("\tLocation: %s\n\n", svrlSuccessfulReport.getLocation()));
//        });
//
//        SVRLHelper.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(xml)).forEach(svrlFailedAssert -> {
//            buffer.append(String.format("\tTest: %s\n", svrlFailedAssert.getTest()));
//            buffer.append(String.format("\tFlag: %s\n", svrlFailedAssert.getFlag()));
//            buffer.append(String.format("\tRol: %s\n", svrlFailedAssert.getRole()));
//            buffer.append(String.format("\tText: %s\n", svrlFailedAssert.getText()));
//            buffer.append(String.format("\tLocation: %s\n\n", svrlFailedAssert.getLocation()));
//        });
//
//		return buffer.toString();
//	}

	public String getFailedAssert() throws Exception {
		StringBuffer buffer = new StringBuffer();
		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		List<SVRLFailedAssert> failedAsserts = SVRLHelper
				.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(xml));
		
		failedAsserts.forEach(FailedAssert -> {
			buffer.append(String.format("\tTest: %s\n", FailedAssert.getTest()));
			buffer.append(String.format("\tFlag: %s\n", FailedAssert.getFlag()));
			buffer.append(String.format("\tRole: %s\n", FailedAssert.getRole()));
			buffer.append(String.format("\tText: %s\n", FailedAssert.getText()));
			buffer.append(String.format("\tLocation: %s\n\n", FailedAssert.getLocation()));

			if(!FailedAssert.getDiagnisticReferences().isEmpty()) {
				buffer.append(String.format("\tDiagnistic References:\n\n"));
				
				FailedAssert.getDiagnisticReferences().forEach(diagnistic->{
					buffer.append(String.format("\t\tDiagnistic: %s\n\n", diagnistic.getDiagnostic()));
					buffer.append(String.format("\t\tText: %s\n\n", diagnistic.getText()));
				});
			}
		});

//        SVRLHelper.getAllSuccessfulReports(aResPure.applySchematronValidationToSVRL(xml)).forEach(svrlSuccessfulReport -> {
//            System.out.print(MessageFormat.format("{0}:     {1}\n", "Test", svrlSuccessfulReport.getTest()));
//            System.out.print(MessageFormat.format("{0}:     {1}\n", "Text", svrlSuccessfulReport.getText()));
//            System.out.print("\n\n");
//        });

		return buffer.toString();
	}
	
	public SVRLFailed getSVRLFailedAssert() throws Exception {
		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		List<SVRLFailedAssert> failedAsserts = SVRLHelper
				.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(xml));
		
		return new SVRLFailed(failedAsserts);
	}

	public static boolean isValid(File schema, File xml) throws Exception {
		final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);
		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		return aResPure.getSchematronValidity(new StreamSource(xml)).isValid();
	}

	public static String getFailedAssert(File schema, File xml) throws Exception {
		final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);

		StringBuffer buffer = new StringBuffer();
		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		List<SVRLFailedAssert> failedAsserts = SVRLHelper
				.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(new StreamSource(xml)));

		failedAsserts.forEach(FailedAssert -> {
			buffer.append(String.format("\tTest: %s\n", FailedAssert.getTest()));
			buffer.append(String.format("\tFlag: %s\n", FailedAssert.getFlag()));
			buffer.append(String.format("\tRol: %s\n", FailedAssert.getRole()));
			buffer.append(String.format("\tText: %s\n", FailedAssert.getText()));
			buffer.append(String.format("\tLocation: %s\n\n", FailedAssert.getLocation()));
			
			if(!FailedAssert.getDiagnisticReferences().isEmpty()) {
				buffer.append(String.format("\tDiagnistic References:\n\n"));
				
				FailedAssert.getDiagnisticReferences().forEach(diagnistic->{
					buffer.append(String.format("\t\tDiagnistic: %s\n\n", diagnistic.getDiagnostic()));
					buffer.append(String.format("\t\tText: %s\n\n", diagnistic.getText()));
				});
			}			
		});

		return buffer.toString();
	}
	
	public static SVRLFailed getSVRLFailedAssert(File schema, File xml) throws Exception {
		final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);

		if (!aResPure.isValidSchematron())
			throw new IllegalArgumentException("Invalid Schematron!");

		List<SVRLFailedAssert> failedAsserts = SVRLHelper
				.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(new StreamSource(xml)));

		return new SVRLFailed(failedAsserts);
	}
}
