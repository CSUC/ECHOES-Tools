package org.csuc.schematron;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;

public class SchematronUtil {

	private static Logger logger = LogManager.getLogger(SchematronUtil.class);
	
	public static boolean isInvalid(@Nonnull File schema, @Nonnull StreamSource xml) {
    	final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);
        if (!aResPure.isValidSchematron())
            throw new IllegalArgumentException("Invalid Schematron!");
        try{
        	return aResPure.getSchematronValidity(xml).isInvalid();
        }catch (Exception e){
        	logger.error(e);
        	return true;        	
        }
    }
   
	
	public static String getFailedAssert(@Nonnull File schema, @Nonnull StreamSource xml) {
		try{
			final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);
			
			StringBuffer buffer = new StringBuffer();
			if (!aResPure.isValidSchematron())
				throw new IllegalArgumentException("Invalid Schematron!");
			
			List<SVRLFailedAssert> failedAsserts = 
	        		SVRLHelper.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(xml));
	        
			failedAsserts.forEach(FailedAssert->{
				buffer.append(String.format("\tTest: %s\n", FailedAssert.getTest()));
				buffer.append(String.format("\tFlag: %s\n", FailedAssert.getFlag()));
				buffer.append(String.format("\tRol: %s\n", FailedAssert.getRole()));
				buffer.append(String.format("\tText: %s\n", FailedAssert.getText()));
				buffer.append(String.format("\tLocation: %s\n\n", FailedAssert.getLocation()));			
	        });
			
			return buffer.toString();
		}catch (Exception e){
			System.err.println(e);
			return null;
		}		
		//return buffer.toString();
	}
	
	public static String getFailedAssertFilename(@Nonnull File schema, @Nonnull StreamSource xml) throws Exception{
		final ISchematronResource aResPure = SchematronResourcePure.fromFile(schema);
		
		 if (!aResPure.isValidSchematron())
          throw new IllegalArgumentException("Invalid Schematron!");
		
		List<SVRLFailedAssert> failedAsserts = 
       		SVRLHelper.getAllFailedAssertions(aResPure.applySchematronValidationToSVRL(xml));
             
       return failedAsserts.isEmpty() ? null : new File(xml.getSystemId()).getName();		
	}
	
		
}
