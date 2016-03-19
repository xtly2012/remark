package com.chen.remark.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.chen.remark.receiver.LocationReceiver;
import com.chen.remark.service.LocationService;
import com.chen.remark.tool.ResourceParser;
import com.chen.remark.widget.NoteWidgetProvider_2x;
import com.chen.remark.widget.NoteWidgetProvider_4x;

import java.io.File;

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

    private RemarkDAO mRemarkDAO;

    private Intent locationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.note_edit);

        this.mRemarkDAO = new RemarkDAO(this);

        if (savedInstanceState == null && !this.initActivityState(this.getIntent())) {
            this.finish();
            return;
        }

        this.initResources();
        this.registerBroadcastReceiver();
        this.startLocationService();
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

        if (TextUtils.equals(Intent.ACTION_VIEW, intent.getAction())) {
            long remarkId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            this.mRemark = this.mRemarkDAO.findByRemarkId(remarkId);
        } else if (TextUtils.equals(Intent.ACTION_INSERT_OR_EDIT, intent.getAction())) {
            long remarkId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            int widgetId = intent.getIntExtra(IntentConstants.INTENT_EXTRA_WIDGET_ID, 0);
            int widgetType = intent.getIntExtra(IntentConstants.INTENT_EXTRA_WIDGET_TYPE, 0);
            if (remarkId > 0) {
                this.mRemark = this.mRemarkDAO.findByRemarkId(remarkId);
            } else {
                this.mRemark.setWidgetId(widgetId);
                this.mRemark.setWidgetType(widgetType);
            }
        }

        return true;
    }

    public void appendNoteEditorText(String value) {
        String text = this.mNoteEditor.getText().toString();
        text += "\n";
        text += value;
        this.mNoteEditor.setText(text);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.isFinishing()) {
            return true;
        }

        menu.clear();
        this.getMenuInflater().inflate(R.menu.note_edit, menu);

        if (RemarkConstants.MODE_CHECK_LIST == this.mRemark.getCheckListMode()) {
            menu.findItem(R.id.menu_list_mode).setTitle(R.string.menu_normal_mode);
        } else {
            menu.findItem(R.id.menu_list_mode).setTitle(R.string.menu_list_mode);
        }

        if (this.mRemark.getAlertDate() > 0) {
            menu.findItem(R.id.menu_alert).setVisible(false);
        } else {
            menu.findItem(R.id.menu_delete_remind).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_font_size:
                break;

            case R.id.menu_list_mode:
                break;

            case R.id.menu_share:
                break;

            case R.id.menu_send_to_desktop:
                break;

            case R.id.menu_alert:
                setReminder();
                break;

            case R.id.menu_delete_remind:
                this.mRemark.setAlertDate(0L);
                break;

            case R.id.menu_take_picture:
                this.takePicture();
                break;

            default:
                break;
        }
        return true;
    }

    private void takePicture() {
        // 创建输出文件
        File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");
        Uri outputFileUri = Uri.fromFile(file);

        // 生成Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        // 启动摄像头应用程序
        this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.hasExtra("data")) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(thumbnail);
            }
        }
    }

    private void setReminder() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(this, System.currentTimeMillis());
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void onDateTimeSet(AlertDialog dialog, long date) {
                mRemark.setAlertDate(date);
                mRemarkDAO.saveRemark(mRemark);
            }
        });

        dialog.show();
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
        stopService(this.locationIntent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止服务
        stopService(this.locationIntent);
    }

    private void registerBroadcastReceiver() {
        LocationReceiver locationReceiver = new LocationReceiver();
        locationReceiver.setNoteEditActivity(this);
        IntentFilter filter = new IntentFilter(LocationReceiver.LOCATION_CHANGED_ACTION);
        this.registerReceiver(locationReceiver, filter);
    }

    private void startLocationService() {
        this.locationIntent = new Intent(this, LocationService.class);
        this.startService(this.locationIntent);
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
