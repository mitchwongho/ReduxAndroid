package za.co.mitchwongho.reduxandroid.ext

import rx.Observable

/**
 *
 */


public abstract class Reducer<S, A> {
    public abstract fun reduce(state: S, action: A?): S
}

public abstract class Store<S, A>( public val reducer: Reducer<S, A> )  {
    public abstract fun getState(): S
    public abstract fun dispatch(action: A): Unit
    public abstract fun observe(): Observable<S>
}
