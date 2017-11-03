/**
 * 
 */
package org.Validation.Core.schematron;

import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

import com.helger.schematron.svrl.SVRLFailedAssert;


/**
 * @author amartinez
 *
 */
public class SVRLFailed extends JSONObject {

	public SVRLFailed() {		
	}
	
	public SVRLFailed(List<SVRLFailedAssert> svrl) {		
		svrl.forEach(failedAssert -> {
			put("test", Objects.isNull(failedAssert.getTest()) ? JSONObject.NULL : failedAssert.getTest());
			put("flag", Objects.isNull(failedAssert.getFlag()) ? JSONObject.NULL : failedAssert.getFlag());
			put("role", Objects.isNull(failedAssert.getRole()) ? JSONObject.NULL : failedAssert.getRole());
			put("text", Objects.isNull(failedAssert.getText()) ? JSONObject.NULL : failedAssert.getText());
			put("location", Objects.isNull(failedAssert.getText()) ? JSONObject.NULL : failedAssert.getText());
			
			JSONObject diagnisticReferences = new JSONObject();
			if(!failedAssert.getDiagnisticReferences().isEmpty()) {
				failedAssert.getDiagnisticReferences().forEach(diagnistic->{
					diagnisticReferences.put("diagnistic", Objects.isNull(diagnistic.getDiagnostic()) ? JSONObject.NULL : diagnistic.getDiagnostic());
					diagnisticReferences.put("text", Objects.isNull(diagnistic.getText()) ? JSONObject.NULL : diagnistic.getText());					
				});
			}
			if(diagnisticReferences.length() == 0)	put("diagnisticReferences", JSONObject.NULL);
			else put("diagnisticReferences", diagnisticReferences);			
		});
	}
	
}
