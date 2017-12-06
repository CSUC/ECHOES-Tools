package org.Recollect.Core.observable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author amartinez
 */
public class ObservableSource<T> {

    protected BehaviorSubject<T> behaviorSubject;

    public ObservableSource() {
        this.behaviorSubject = BehaviorSubject.create();
    }

    public Observable<T> getObservable() {
        return behaviorSubject;
    }

    public void add(T element) {
        behaviorSubject.onNext(element);
    }

    public void onCompleted(){
        behaviorSubject.onComplete();
    }
}
