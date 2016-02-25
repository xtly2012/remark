package com.chen.remark.ui;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import com.chen.remark.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenfayong on 16/1/17.
 */
public class NoteEditText extends EditText {

    private static final  String TAG = "NoteEditText";

    private int mIndex;
    private int mSelectionStartBeforeDelete;
    private OnTextViewChangeListener mOnTextViewChangeListener;

    private static final String SCHEMA_TEL = "tel:";
    private static final String SCHEMA_HTTP = "http:";
    private static final String SCHEMA_EMAIL = "mail to:";

    private  static final Map<String, Integer> sSchemaActionResMap = new HashMap<>();
    static  {
        sSchemaActionResMap.put(SCHEMA_TEL, R.string.note_link_tel);
        sSchemaActionResMap.put(SCHEMA_HTTP, R.string.note_link_web);
        sSchemaActionResMap.put(SCHEMA_EMAIL, R.string.note_link_email);

    }

    public NoteEditText(Context context) {
        super(context);
        this.mIndex = 0;
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public void setOnTextViewChangeListener(OnTextViewChangeListener listener) {
        this.mOnTextViewChangeListener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                x -= getTotalPaddingLeft();
                y -= getTotalPaddingTop();
                x += getScrollX();
                y += getScrollY();

                Layout layout = getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                Selection.setSelection(getText(), off);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case  KeyEvent.KEYCODE_DEL:
                if (this.mOnTextViewChangeListener != null) {
                    if (0 == this.mSelectionStartBeforeDelete && this.mIndex != 0) {
                        this.mOnTextViewChangeListener.onEditTextDelete(this.mIndex, this.getText().toString());
                        return true;
                    }
                } else {
                    Log.d(TAG, "onTextViewChangeListener was not set");
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                if (this.mOnTextViewChangeListener != null) {
                    int selectionStart = this.getSelectionStart();
                    String text = this.getText().subSequence(selectionStart, length()).toString();
                    this.setText(this.getText().subSequence(0, selectionStart));
                    this.mOnTextViewChangeListener.onEditTextEnter(this.mIndex +1, text);
                } else {
                    Log.d(TAG, "OnTextViewChangeListener was not set");
                }
                break;

            default:
                break;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        if (this.getText() instanceof Spanned) {
            int selStart = this.getSelectionStart();
            int selEnd = this.getSelectionEnd();

            int min = Math.min(selStart, selEnd);
            int max = Math.max(selStart, selEnd);

            final URLSpan[] urls = ((Spanned)this.getText()).getSpans(min, max, URLSpan.class);
            if (urls.length == 1) {
                int defaultResId = 0;
                for (String schema : sSchemaActionResMap.keySet()) {
                    if (urls[0].getURL().indexOf(schema) >= 0) {
                        defaultResId = sSchemaActionResMap.get(schema);
                        break;
                    }
                }

                if (defaultResId == 0) {
                    defaultResId = R.string.note_link_other;
                }

                menu.add(0, 0, 0, defaultResId).setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                urls[0].onClick(NoteEditText.this);
                                return false;
                            }
                        }
                );
            }
        }

        super.onCreateContextMenu(menu);
    }

    public interface OnTextViewChangeListener {

        void onEditTextDelete(int index, String text);


        void onEditTextEnter(int index, String text);

        void onTextChange(int index, boolean hasText);
    }
}
