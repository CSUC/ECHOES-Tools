/**
 * 
 */
package org.Recollect.Core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author amartinez
 *
 */
public class ItemIterator <T> implements Iterator<T> {

	private List<T> items = new ArrayList<T>();
    private Source<T> source;
    private int position = 0;

    public ItemIterator(Source<T> source) {
        this.source = source;
        hasNext();
    }

    @Override
    public boolean hasNext() {
        if (items.size() > position)
            return true;
        else {
            if (source.endReached()) return false;
            else {
            	List<T> result = source.nextIteration();
            	if(result != null) {
            		items.addAll(result);
            		return hasNext();
            	}
            	return false;       			
//				items.addAll(source.nextIteration());				
//              return hasNext();
            }
        }
    }

    @Override
    public T next() {
        return items.get(position++);
    }
	

}
