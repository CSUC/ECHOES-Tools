/**
 * 
 */
package org.Recollect.Core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 *
 */
public class StatusCollection {

	public AtomicInteger totalReadRecord;
	public AtomicInteger totalDownloadRecord;
	public AtomicInteger totalDeletedRecord;
	public AtomicInteger totalFileAlreadyExistsRecordAndReplace;
	
	public List<String> deletedRecord = new ArrayList<String>();
	
	public StatusCollection() {		
		totalReadRecord = new AtomicInteger(0);;
		totalDownloadRecord = new AtomicInteger(0);
		totalDeletedRecord = new AtomicInteger(0);
		totalFileAlreadyExistsRecordAndReplace = new AtomicInteger(0);
	}
	
}
