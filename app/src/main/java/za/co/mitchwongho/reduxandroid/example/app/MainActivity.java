package za.co.mitchwongho.reduxandroid.example.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import kotlin.jvm.functions.Function2;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import za.co.mitchwongho.reduxandroid.example.R;
import za.co.mitchwongho.reduxandroid.example.app.action.ButtonClickAction;
import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;
import za.co.mitchwongho.reduxandroid.example.app.action.TextChangeAction;
import za.co.mitchwongho.reduxandroid.ext.Store;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();

    public static Function2<ReduxState, ReduxAction, ReduxState> textchangeReducer = new Function2<ReduxState, ReduxAction, ReduxState>() {
        @Override
        public ReduxState invoke(@NonNull ReduxState reduxState, @NonNull ReduxAction reduxAction) {
            if (reduxAction instanceof TextChangeAction) {
                final TextChangeAction action = (TextChangeAction) reduxAction;
                return new MainActivityState(action.getText(), true);
            }
            return reduxState;
        }
    };

    public static Function2<ReduxState, ReduxAction, ReduxState> clickableReducer = new Function2<ReduxState, ReduxAction, ReduxState>() {
        @Override
        public ReduxState invoke(@NonNull ReduxState reduxState, @NonNull ReduxAction reduxAction) {
            if (reduxAction instanceof TextChangeAction) {
                final TextChangeAction action = (TextChangeAction) reduxAction;
                return new MainActivityState(action.getText(), action.getText().length() != 0);
            }
            return reduxState;
        }
    };

    public static Function2<ReduxState, ReduxAction, ReduxState> buttonClickedReducer = new Function2<ReduxState, ReduxAction, ReduxState>() {
        @Override
        public ReduxState invoke(@NonNull ReduxState reduxState, @NonNull ReduxAction reduxAction) {
            if (reduxAction instanceof ButtonClickAction) {
                return new MainActivityState("", true);
            }
            return reduxState;
        }
    };

    public static Function2<ReduxState, ReduxAction, ReduxState>[] reducers = new Function2[]{textchangeReducer, buttonClickedReducer, clickableReducer};

    public static Store<ReduxState, ReduxAction> store = new ReduxStore(new ReduxState() {
    }, reducers);


    private CompositeSubscription subs;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        subs = new CompositeSubscription();
        final Subscription sub1 = store.observe()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(state -> {
                    Log.d(TAG, "onNext");
                    final MainActivityState mas = (MainActivityState) state;
                    ((TextView) findViewById(R.id.main_textview)).setText(mas.getText());
                    final EditText et = ((EditText) findViewById(R.id.main_edittext));
                    if (TextUtils.isEmpty(mas.getText()) && !TextUtils.isEmpty(et.getText().toString())) {
                        et.setText(mas.getText());
                    }
                    findViewById(R.id.main_button).setEnabled(mas.isClickable());
                });
        subs.add(sub1);

        final Subscription sub2 = store.subscribe(RxTextView.textChanges((EditText) findViewById(R.id.main_edittext))
                .observeOn(AndroidSchedulers.mainThread()).map(charSequence -> new TextChangeAction(charSequence)));
        subs.add(sub2);

        final Subscription sub3 = store.subscribe(RxView.clicks(findViewById(R.id.main_button))
                .observeOn(AndroidSchedulers.mainThread()).map(aVoid -> new ButtonClickAction()));

        subs.add(sub3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subs != null && !subs.isUnsubscribed()) {
            subs.unsubscribe();
            subs.clear();
            subs = null;
        }
        store.close();
    }
}
