package com.chen.remark.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import com.chen.remark.R;
import com.chen.remark.constants.RemarkConstants;
import com.chen.remark.dao.RemarkDAO;
import com.chen.remark.listener.NotesListListener;
import com.chen.remark.model.Remark;
import com.chen.remark.tool.ResourceParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class NotesListActivity extends Activity {

    private static final String TAG = "NotesListActivity";

    private static final String PREFERENCE_ADD_INTRODUCTION = "com.chen.remark.introduction";

    public final static int REQUEST_CODE_OPEN_NODE = 102;

    public final static int REQUEST_CODE_NEW_NOTE = 103;

    private NotesListAdapter listAdapter;

    private Button mAddNewNote;

    private String contentQuery;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);

        this.setAppInfoFromRawRes();
        this.parseIntent(this.getIntent());
        this.initResources();
    }

    private void parseIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.contentQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "search key is : " +this.contentQuery);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
        this.parseIntent(intent);
        this.initResources();
    }

    private void initResources() {
        ListView listView = (ListView)this.findViewById(R.id.notes_list);
        View listFooterView = LayoutInflater.from(this).inflate(R.layout.note_list_footer, null);
        listView.addFooterView(listFooterView, null, false);

        RemarkDAO remarkDAO = new RemarkDAO(this);
        List<Remark> itemList = remarkDAO.findByContent(this.contentQuery);

        this.listAdapter = new NotesListAdapter(this, R.layout.note_item, itemList);
        listView.setAdapter(listAdapter);

        NotesListListener noteNewListener = new NotesListListener(this, listAdapter);
        this.mAddNewNote = (Button)this.findViewById(R.id.btn_new_note);
        mAddNewNote.setOnClickListener(noteNewListener);

        listView.setOnItemClickListener(noteNewListener);
        listView.setOnItemLongClickListener(noteNewListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.note_list, menu);

        return true;
    }

    @Override
    public boolean onSearchRequested() {
        this.startSearch(null, false, null, false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.initResources();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search :
                this.onSearchRequested();
                break;

            case R.id.menu_export_text :
                this.exportRemarkToText();
                break;

            case R.id.menu_setting :
                this.startPreferenceActivity();
                break;

            default:
                super.onOptionsItemSelected(item);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setAppInfoFromRawRes() {
        SharedPreferences sharedPrefer = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPrefer.getBoolean(PREFERENCE_ADD_INTRODUCTION, false)) {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = null;
            try {
                inputStream = this.getResources().openRawResource(R.raw.introduction);
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    char[] charArray = new char[1024];
                    int readLength = 0;
                    while ((readLength = bufferedReader.read(charArray)) > 0) {
                        stringBuilder.append(charArray, 0, readLength);
                    }
                } else {
                    Log.e(TAG, "Read introduction file error");
                    return;
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }

            Remark remark = new Remark(AppWidgetManager.INVALID_APPWIDGET_ID, RemarkConstants.WIDGET_TYPE_INVALID,
                    ResourceParser.REMARK_BG_BLUE, stringBuilder.toString());
            RemarkDAO remarkDAO = new RemarkDAO(this);
            remarkDAO.saveRemark(remark);
            sharedPrefer.edit().putBoolean(PREFERENCE_ADD_INTRODUCTION, true).commit();
        }
    }

    private void startPreferenceActivity() {
        Activity from = this.getParent() != null ? this.getParent() : this;
        Intent intent = new Intent(from, NotesPreferenceActivity.class);
        from.startActivityIfNeeded(intent, -1);
    }

    private void exportRemarkToText() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {

            }
        }.execute();
    }
}
