package org.csuc.csv;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class CellProcessors {

	/**
	 * dc.identifier,dc.creator,dc.rights,dc.title,dc.description,
	 * dc.coverage,dc.type,dc.source,dc.subject,dc.date, 
	 * dc.publisher,dc.format,dc.relation,dc.contributor,dc.language,
	 * edm.type
	 */
	final static CellProcessor[] processors = new CellProcessor[] { 
			//edm:ProvidedCHO
			new Optional(), 	//edm.ProvidedCHO.dcidentifier
			new Optional(), 	//edm.ProvidedCHO.dccreator
			new Optional(), 	//edm.ProvidedCHO.dcrights
			new Optional(),		//edm.ProvidedCHO.dctitle
			new Optional(),		//edm.ProvidedCHO.dcdescription
			new Optional(),		//edm.ProvidedCHO.dccoverage
			new Optional(),		//edm.ProvidedCHO.dctype
			new Optional(),		//edm.ProvidedCHO.dcsource
			new Optional(),		//edm.ProvidedCHO.dcsubject
			new Optional(),		//edm.ProvidedCHO.dcdate
			new Optional(),		//edm.ProvidedCHO.dcpublisher
			new Optional(),		//edm.ProvidedCHO.dcformat
			new Optional(),		//edm.ProvidedCHO.dcrelation
			new Optional(),		//edm.ProvidedCHO.dccontributor
			new Optional(),		//edm.ProvidedCHO.dclanguage			
			new Optional(),		//edm.ProvidedCHO.edmType
			//edm:Agent
			new Optional(),		//edm.Agent.dcidentifier
			new Optional(),		//edm.Agent.skospreflabel
			new Optional(),		//edm.Agent.skosaltlabel
			new Optional(),		//edm.Agent.edmisrelatedto
			new Optional(),		//edm.Agent.rdagr2professionoroccupation
			//skos:Concept
			new Optional(),		//skos.Concept.skospreflabel
			
    };

	public static CellProcessor[] getProcessors() {
		return processors;
	}
        
}
