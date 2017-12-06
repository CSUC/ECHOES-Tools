package org.Recollect.Core.observable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class ObservableList<T> {

    protected List<T> list;
    protected BehaviorSubject<T> behaviorSubject;

    public ObservableList() {
        if(Objects.isNull(list))    list = new ArrayList<>();
        this.behaviorSubject = BehaviorSubject.create();
    }

    public Observable<T> getObservable() {
        return behaviorSubject;
    }

    public void add(T element) {
        list.add(element);
        behaviorSubject.onNext(element);
    }

    public void onCompleted(){
        behaviorSubject.onComplete();
    }
}
