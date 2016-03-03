package com.chen.remark.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chen.remark.R;
import com.chen.remark.constants.IntentConstants;
import com.chen.remark.constants.RemarkConstants;
import com.chen.remark.dao.RemarkDAO;
import com.chen.remark.listener.NoteEditListener;
import com.chen.remark.model.Remark;
import com.chen.remark.tool.ResourceParser;
import com.chen.remark.widget.NoteWidgetProvider_2x;
import com.chen.remark.widget.NoteWidgetProvider_4x;

/**
 * note new and modify Activity
 *
 * Created by chenfayong on 16/1/17.
 */
public class NoteEditActivity extends Activity {

    private final static String TAG = "NoteEditActivity";

    private Remark mRemark;

    private EditText mNoteEditor;

    private View mHeadViewPanel;

    private HeadViewHolder mHeadViewHolder;

    private LinearLayout mEditTextList;

    private View mNoteEditorPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.note_edit);

        if (savedInstanceState == null && !this.initActivityState(this.getIntent())) {
            this.finish();
            return;
        }

        this.initResources();
    }

    private void initResources() {
        this.mHeadViewPanel = this.findViewById(R.id.note_title);
        this.mHeadViewHolder = new HeadViewHolder();
        this.mHeadViewHolder.tvModified = (TextView)this.findViewById(R.id.tv_modified_date);
        this.mHeadViewHolder.ivAlertIcon = (ImageView)this.findViewById(R.id.iv_alert_icon);
        this.mHeadViewHolder.tvAlertDate = (TextView)this.findViewById(R.id.tv_alert_date);
        this.mHeadViewHolder.ibSetBgColor = (ImageView)this.findViewById(R.id.btn_set_bg_color);
        this.mNoteEditor = (EditText)this.findViewById(R.id.note_edit_view);
        this.mNoteEditorPanel = this.findViewById(R.id.sv_note_edit);

        NoteEditListener noteEditListener = new NoteEditListener(this);
        this.mHeadViewHolder.ibSetBgColor.setOnClickListener(noteEditListener);
        ((NoteEditText)this.mNoteEditor).setOnTextViewChangeListener(noteEditListener);
    }

    private boolean initActivityState(Intent intent) {
        this.mRemark = new Remark(AppWidgetManager.INVALID_APPWIDGET_ID, RemarkConstants.WIDGET_TYPE_INVALID,
                ResourceParser.REMARK_BG_BLUE, "");

        RemarkDAO remarkDAO = new RemarkDAO(this);
        if (TextUtils.equals(Intent.ACTION_VIEW, intent.getAction())) {
            long remarkId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            this.mRemark = remarkDAO.findByRemarkId(remarkId);
        } else if (TextUtils.equals(Intent.ACTION_INSERT_OR_EDIT, intent.getAction())) {
            long remarkId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            int widgetId = intent.getIntExtra(IntentConstants.INTENT_EXTRA_WIDGET_ID, 0);
            int widgetType = intent.getIntExtra(IntentConstants.INTENT_EXTRA_WIDGET_TYPE, 0);
            if (remarkId > 0) {
                this.mRemark = remarkDAO.findByRemarkId(remarkId);
            } else {
                this.mRemark.setWidgetId(widgetId);
                this.mRemark.setWidgetType(widgetType);
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initNoteScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveRemark();
    }

    private void saveRemark() {
        String remarkContent = this.mNoteEditor.getText().toString();
        if (!remarkContent.isEmpty()) {
            this.mRemark.setRemarkContent(remarkContent);
            RemarkDAO remarkDAO = new RemarkDAO(this);
            if (this.mRemark.getRemarkId() == null) {
                remarkDAO.saveRemark(this.mRemark);
            } else {
                remarkDAO.modifyByRemarkId(this.mRemark);
            }
        }

        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        if (RemarkConstants.WIDGET_TYPE_2X == this.mRemark.getWidgetType()) {
            intent.setClass(this, NoteWidgetProvider_2x.class);
        } else if (RemarkConstants.WIDGET_TYPE_4X == this.mRemark.getWidgetType()) {
            intent.setClass(this, NoteWidgetProvider_4x.class);
        } else {
            Log.e(TAG, "Unsupported widget type");
            return;
        }

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {
                this.mRemark.getWidgetId()
        });

        this.sendBroadcast(intent);
        this.setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        this.saveRemark();

        this.setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void initNoteScreen() {
        this.mNoteEditor.setText(this.mRemark.getRemarkContent());
        this.mHeadViewHolder.tvModified.setText(DateUtils.formatDateTime(this,
                this.mRemark.getModifiedDate(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_YEAR));
    }

    private class HeadViewHolder {
        public TextView tvModified;

        public ImageView ivAlertIcon;

        public TextView tvAlertDate;

        public ImageView ibSetBgColor;
    }
}
