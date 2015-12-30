package za.co.mitchwongho.reduxandroid.example.app;


/**
 *
 */
public class MainActivityState implements ReduxState {
    private CharSequence text;
    private boolean clickable;

    public MainActivityState(CharSequence text, boolean clickable) {
        this.text = text;
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public CharSequence getText() {
        return text;
    }
}
