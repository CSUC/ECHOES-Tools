/**
 * 
 */
package org.csuc.Validation.Core.schematron;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.Document;
import com.helger.schematron.svrl.SVRLFailedAssert;


/**
 * @author amartinez
 *
 */
public class SVRLFailed {


	private ArrayList<Document> result = new ArrayList<>();

	public SVRLFailed() {		
	}
	
	public SVRLFailed(List<SVRLFailedAssert> svrl) {
		svrl.forEach(failedAssert -> {
            Document jsonObject = new Document();

            jsonObject.put("test", check(failedAssert.getTest()));
            jsonObject.put("flag", check(failedAssert.getFlag()));
            jsonObject.put("role", check(failedAssert.getRole()));
            jsonObject.put("text", check(failedAssert.getText()));
            jsonObject.put("location", check(failedAssert.getLocation()));

			Document diagnisticReferences = new Document();

			if(!failedAssert.getDiagnisticReferences().isEmpty()) {
				failedAssert.getDiagnisticReferences().forEach(diagnistic->{
					diagnisticReferences.put("diagnistic", check(diagnistic.getDiagnostic()));
					diagnisticReferences.put("text", check(diagnistic.getText()));
				});
			}

			if(diagnisticReferences.isEmpty())	jsonObject.put("diagnisticReferences", null);
			else jsonObject.put("diagnisticReferences", diagnisticReferences);

			result.add(jsonObject);
		});
	}

	private Object check(Object input){
		if(input instanceof String || input instanceof List )	return (Objects.isNull(input) || ((String)input).isEmpty()) ? null : input;
        return Objects.isNull(input) ? null : input;
	}

    @Override
    public String toString() {
		return result.stream().map(m-> m.toJson()).collect(Collectors.joining());
    }

    public List<Document> toDocument(){
		return result.stream().collect(Collectors.toList());
	}

}
