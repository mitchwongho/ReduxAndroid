package za.co.mitchwongho.reduxandroid.example.app;

import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.subjects.PublishSubject;
import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;
import za.co.mitchwongho.reduxandroid.ext.*;

/**
 *
 */
public class ReduxStore extends Store<ReduxState, ReduxAction> {


    PublishSubject<ReduxState> sub = PublishSubject.create();

    ReduxState state = new MainActivityState("", true);

    public ReduxStore(@NotNull final Reducer<ReduxState, ReduxAction> reducer) {
        super(reducer);
    }

    @Override
    public void dispatch(@NotNull final ReduxAction action) {
        this.state = super.getReducer().reduce(state, action);
        sub.onNext(this.state);
    }

    @Override
    public ReduxState getState() {
        return state;
    }

    @NotNull
    @Override
    public Observable<ReduxState> observe() {
        return sub;
    }
}
