package com.chen.remark.dao;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.chen.remark.data.TableRemark;
import com.chen.remark.data.TableRemark.RemarkColumns;
import com.chen.remark.model.Remark;

import java.util.ArrayList;
import java.util.List;

import com.chen.remark.data.NotesProvider.NotesResolver;

/**
 * Created by chenfayong on 16/2/21.
 */
public class RemarkDAO {

    private Context context;

    private ContentResolver contentResolver;

    public RemarkDAO(Context context) {
        this.context = context;
        this.contentResolver = this.context.getContentResolver();
    }


    public void saveRemark(Remark remark) {
        Uri resultUri = this.contentResolver.insert(NotesResolver.AUTHORITY_REMARK, remark.transform2ContentValues());
        String insertId = resultUri.getPathSegments().get(1);
        remark.setRemarkId(Long.valueOf(insertId));
    }

    public List<Remark> findByContent(String content) {
        String selection = null;
        String[] selectionArgs = null;
        if (content != null) {
            selection = " remark_content LIKE '%' || ? || '%' ";
            selectionArgs = new String[]{content};
        }

        String orderOrder = " modified_date DESC ";
        Cursor cursor = this.contentResolver.query(NotesResolver.AUTHORITY_REMARK, null, selection, selectionArgs, orderOrder);

        List<Remark> remarkList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Long remarkId = cursor.getLong(cursor.getColumnIndex(RemarkColumns.REMARK_ID));
            String remarkTitle = cursor.getString(cursor.getColumnIndex(RemarkColumns.REMARK_CONTENT));
            Long remarkTime = cursor.getLong(cursor.getColumnIndex(RemarkColumns.CREATED_DATE));

            Remark remark = new Remark();
            remark.setRemarkId(remarkId);
            remark.setRemarkContent(remarkTitle);
            remark.setModifiedDate(remarkTime);
            remarkList.add(remark);
        }
        cursor.close();

        return remarkList;
    }

    public Remark findByRemarkId(Long remarkId) {
        String selection = RemarkColumns.REMARK_ID +" = ?";
        String[] selectionArgs = new String[]{remarkId.toString()};
        Cursor cursor = this.contentResolver.query(NotesResolver.AUTHORITY_REMARK, null, selection, selectionArgs, null);
        if (cursor == null) {
            return null;
        }

        Remark remark = null;
        if (cursor.moveToFirst()) {
            String remarkTitle = cursor.getString(cursor.getColumnIndex(RemarkColumns.REMARK_CONTENT));
            Integer checkListMode = cursor.getInt(cursor.getColumnIndex(RemarkColumns.CHECK_LIST_MODE));
            Long remarkTime = cursor.getLong(cursor.getColumnIndex(RemarkColumns.CREATED_DATE));
            Long alertTime = cursor.getLong(cursor.getColumnIndex(RemarkColumns.ALERTED_DATE));

            remark = new Remark();
            remark.setRemarkId(remarkId);
            remark.setRemarkContent(remarkTitle);
            remark.setModifiedDate(remarkTime);
            remark.setCheckListMode(checkListMode);
            remark.setAlertDate(alertTime);
        }
        cursor.close();

        return remark;
    }

    public Remark findByWidgetId(Integer widgetId) {
        String selection = RemarkColumns.WIDGET_ID +" = ?";
        String[] selectionArgs = new String[]{widgetId.toString()};
        Cursor cursor = this.contentResolver.query(NotesResolver.AUTHORITY_REMARK, null, selection, selectionArgs, null);
        if (cursor == null) {
            return null;
        }

        Remark remark = null;
        if (cursor.moveToFirst()) {
            Long remarkId = cursor.getLong(cursor.getColumnIndex(RemarkColumns.WIDGET_ID));
            String remarkTitle = cursor.getString(cursor.getColumnIndex(RemarkColumns.REMARK_CONTENT));
            Long remarkTime = cursor.getLong(cursor.getColumnIndex(RemarkColumns.CREATED_DATE));

            remark = new Remark();
            remark.setRemarkId(remarkId);
            remark.setRemarkContent(remarkTitle);
            remark.setModifiedDate(remarkTime);
        }
        cursor.close();

        return remark;
    }

    public int modifyByRemarkId(Remark remark) {
        ContentValues values = remark.transform2ContentValues();
        String where = "remark_id = ?" ;
        String[] selectionArgs = {remark.getRemarkId().toString()};

        return this.contentResolver.update(NotesResolver.AUTHORITY_REMARK, values, where, selectionArgs);
    }

    public int removeByRemarkId(Long remarkId) {
        String where = "remark_id = ?";
        String[] selectionArgs = {remarkId.toString()};

        return this.contentResolver.delete(NotesResolver.AUTHORITY_REMARK, where, selectionArgs);
    }


}
