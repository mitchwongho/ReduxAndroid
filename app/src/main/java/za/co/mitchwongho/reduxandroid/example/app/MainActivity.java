package za.co.mitchwongho.reduxandroid.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import za.co.mitchwongho.reduxandroid.example.R;
import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;
import za.co.mitchwongho.reduxandroid.example.app.action.TextChangeAction;
import za.co.mitchwongho.reduxandroid.ext.Store;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();

    public static Store<ReduxState, ReduxAction> store = new ReduxStore(new InitialReducer());

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
                    final MainActivityState mas = (MainActivityState)state;
                    ((TextView)findViewById(R.id.main_textview)).setText(mas.getText());
                    final EditText et =  ((EditText)findViewById(R.id.main_edittext));
                    if (TextUtils.isEmpty(mas.getText()) && !TextUtils.isEmpty(et.getText().toString())) {
                        et.setText(mas.getText());
                    }
                });
        subs.add(sub1);
        final Subscription sub2 = RxTextView.textChanges((EditText)findViewById(R.id.main_edittext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    store.dispatch(new TextChangeAction(charSequence));
                });
        subs.add(sub2);
        final Subscription sub3 = RxView.clicks(findViewById(R.id.main_button))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    store.dispatch(new ButtonClickAction());
                });
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
    }
}
