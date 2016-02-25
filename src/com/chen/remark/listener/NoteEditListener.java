package com.chen.remark.listener;

import android.app.Activity;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.chen.remark.R;
import com.chen.remark.ui.NoteEditText;

/**
 * listener of note edit activity
 *
 * Created by chenfayong on 16/1/18.
 */
public class NoteEditListener implements View.OnClickListener, NoteEditText.OnTextViewChangeListener {

    private Activity editActivity;

    private LinearLayout bgColorSelectorLayout;

    private ImageView yellowSelectImage;

    public NoteEditListener(Activity editActivity) {
        this.editActivity = editActivity;
        this.initResources();
    }

    private void initResources() {
        this.bgColorSelectorLayout = (LinearLayout)this.editActivity.findViewById(R.id.note_bg_color_selector);
        this.yellowSelectImage = (ImageView)this.editActivity.findViewById(R.id.iv_by_yellow_select);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.btn_set_bg_color :
                this.bgColorSelectorLayout.setVisibility(View.VISIBLE);
                this.yellowSelectImage.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onEditTextDelete(int index, String text) {

    }

    @Override
    public void onEditTextEnter(int index, String text) {

    }

    @Override
    public void onTextChange(int index, boolean hasText) {
        Toast.makeText(this.editActivity, "text is not null", Toast.LENGTH_SHORT).show();
    }
}
