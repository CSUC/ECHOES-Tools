/**
 * 
 */
package org.csuc.Validation.Core.schematron;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import com.helger.schematron.svrl.SVRLFailedAssert;


/**
 * @author amartinez
 *
 */
public class SVRLFailed extends JSONArray {

	public SVRLFailed() {		
	}
	
	public SVRLFailed(List<SVRLFailedAssert> svrl) {
		svrl.forEach(failedAssert -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("test", check(failedAssert.getTest()));
            jsonObject.put("flag", check(failedAssert.getFlag()));
            jsonObject.put("role", check(failedAssert.getRole()));
            jsonObject.put("text", check(failedAssert.getText()));
            jsonObject.put("location", check(failedAssert.getLocation()));
			
			JSONObject diagnisticReferences = new JSONObject();
			if(!failedAssert.getDiagnisticReferences().isEmpty()) {
				failedAssert.getDiagnisticReferences().forEach(diagnistic->{
					diagnisticReferences.put("diagnistic", check(diagnistic.getDiagnostic()));
					diagnisticReferences.put("text", check(diagnistic.getText()));
				});
			}
			if(diagnisticReferences.length() == 0)	jsonObject.put("diagnisticReferences", JSONObject.NULL);
			else jsonObject.put("diagnisticReferences", diagnisticReferences);

			put(jsonObject);
		});
	}

	private Object check(Object input){
		if(input instanceof String || input instanceof List )	return (Objects.isNull(input) || ((String)input).isEmpty()) ? JSONObject.NULL : input;
        return Objects.isNull(input) ? JSONObject.NULL : input;
	}

    @Override
    public String toString() {
        return super.toString();
    }
}
