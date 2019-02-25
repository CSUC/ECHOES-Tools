/**
 * 
 */
package org.transformation.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author amartinez
 *
 */
public class ItemIterator <T> implements Iterator<T> {

	private List<T> items = new ArrayList<T>();
    private Source<T> source;
    private int position = 0;

    private List<String> exception = new ArrayList<>();

    public ItemIterator(Source<T> source) {
        this.source = source;
//        hasNext();
    }

    @Override
    public boolean hasNext() {
//        if (items.size() > position)
//            return true;
//        else {
//            if (source.endReached()) return false;
//            else {
//                List<T> result = null;
//                try {
//                    result = source.nextIteration();
//                } catch (Exception e) {
//                    exception.add(e.toString());
//                    return false;
//                }
//                if(result != null) {
//            		items.addAll(result);
//            		return hasNext();
//            	}
//            	return false;
//            }
//        }
        if (items.size() > position)
            return true;
        else {
            if (source.endReached()) return false;
            else {
                try {
                    items.addAll(source.nextIteration());
                } catch (Exception e) {
                    exception.add(e.toString());
                    return false;
                }
                return hasNext();
            }
        }
    }

    @Override
    public T next() {
        return items.get(position++);
    }

    public List<String> getException() {
        return new HashSet<>(exception).stream().collect(Collectors.toList());
    }
}
