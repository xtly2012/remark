package com.chen.remark.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.chen.remark.ui.NoteEditActivity;

/**
 * Created by chenfayong on 16/3/16.
 */
public class LocationReceiver extends BroadcastReceiver {

    public static final String LOCATION_CHANGED_ACTION = "com.chen.remark.action.LOCATION_CHANGED_ACTION";

    NoteEditActivity noteEditActivity = null;

    private String lastLocation;

    public void setNoteEditActivity(NoteEditActivity noteEditActivity) {
        this.noteEditActivity = noteEditActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String value = intent.getStringExtra("location");
        if (value != null) {
            if (value.equals(lastLocation)) {
                return;
            } else {
                lastLocation = value;
            }
            noteEditActivity.appendNoteEditorText(value);
        }
    }
}
