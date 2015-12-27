package za.co.mitchwongho.reduxandroid.example.app.action;

import za.co.mitchwongho.reduxandroid.example.app.action.ReduxAction;

/**
 *
 */
public class TextChangeAction implements ReduxAction {
    private final CharSequence text;

    public TextChangeAction(final CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }
}
