package za.co.mitchwongho.reduxandroid.example.app;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function2;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;
import za.co.mitchwongho.reduxandroid.ext.Store;

/**
 *
 */
public class ReduxStore extends Store<ReduxState, ReduxAction> {

    PublishSubject<ReduxState> obz = PublishSubject.create();

    public ReduxStore(@NotNull ReduxState initialState, @NotNull Function2<ReduxState, ReduxAction, ReduxState>[] reducers) {
        super(initialState, reducers);
    }

    @Override
    public Subscription subscribe(@NotNull Observable<ReduxAction> observable) {
        return observable.subscribe(reduxAction -> {
            //onNext
            dispatch(reduxAction);
        });
    }

    private void dispatch(@NotNull final ReduxAction action) {
        final ReduxState oldState = getState();
        final ReduxState newState = super.reduce(oldState, action);
        super.setState(newState);
        obz.onNext(newState);
    }

    @NotNull
    @Override
    public Observable<ReduxState> observe() {
        return obz;
    }

    @Override
    public void close() {
        obz.onCompleted();
    }
}
