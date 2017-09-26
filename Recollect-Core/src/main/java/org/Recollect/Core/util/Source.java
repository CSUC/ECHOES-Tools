/**
 * 
 */
package org.Recollect.Core.util;

import java.util.List;


/**
 * @author amartinez
 *
 */
public interface Source<T> {
    List<T> nextIteration ();
    boolean endReached ();
}