package com.chen.remark.ui;

import android.provider.ContactsContract;

/**
 * Created by chenfayong on 16/1/14.
 */
public class NoteItemData {

    private Integer remarkId;

    private String noteTitle;

    private String noteTime;

    public NoteItemData() {}

    public NoteItemData(Integer remarkId, String noteTitle, String noteTime) {
        this.remarkId = remarkId;
        this.noteTitle = noteTitle;
        this.noteTime = noteTime;
    }

    public Integer getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(Integer remarkId) {
        this.remarkId = remarkId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
}
