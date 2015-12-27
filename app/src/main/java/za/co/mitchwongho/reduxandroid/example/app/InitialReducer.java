package za.co.mitchwongho.reduxandroid.example.app;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;
import za.co.mitchwongho.reduxandroid.example.app.action.TextChangeAction;
import za.co.mitchwongho.reduxandroid.ext.Reducer;

/**
 *
 */
public class InitialReducer extends Reducer<ReduxState, ReduxAction> {


    @Override
    public ReduxState reduce(@NonNull final ReduxState state, @Nullable final ReduxAction action) {
        if (action instanceof TextChangeAction) {
            //
            final TextChangeAction tca = (TextChangeAction)action;
            return new MainActivityState(tca.getText(), true);
        } else if (action instanceof ButtonClickAction){
            return new MainActivityState("", true);
        } else {
            return state;
        }
    }
}
