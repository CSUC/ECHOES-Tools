package org.Recollect.Core.observable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class ObservableStream<T> {

    protected Stream<T> list;
    protected final BehaviorSubject<T> onAdd;

    public ObservableStream() {
        this.list = Stream.empty();
        this.onAdd = BehaviorSubject.create();
    }
    public void add(T value) {
        list = Stream.concat(Stream.of(value), list);
        onAdd.onNext(value);
    }
    public Observable<T> getObservable() {
        return onAdd;
    }

    public void onCompleted(){
        onAdd.onComplete();
    }

}
