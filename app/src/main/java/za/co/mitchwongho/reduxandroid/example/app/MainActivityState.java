package za.co.mitchwongho.reduxandroid.example.app;


/**
 *
 */
public class MainActivityState implements ReduxState {
    private CharSequence text;
    private boolean editable;

    public MainActivityState(CharSequence text, boolean editable) {
        this.text = text;
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public CharSequence getText() {
        return text;
    }
}
