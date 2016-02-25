package com.chen.remark.dao;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.chen.remark.data.NotesProvider;
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
        this.contentResolver.insert(NotesResolver.AUTHORITY_REMARK, remark.transform2ContentValues());

    }

    public List<Remark> findAll() {
        Cursor cursor = this.contentResolver.query(NotesResolver.AUTHORITY_REMARK, null, null, null, null);

        List<Remark> remarkList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Integer remarkId = cursor.getInt(cursor.getColumnIndex(RemarkColumns.REMARK_ID));
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

    public Remark findByRemarkId(Integer remarkId) {
        Uri uri = ContentUris.withAppendedId(NotesResolver.AUTHORITY_REMARK, remarkId);
        Cursor cursor = this.contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        Remark remark = null;
        if (cursor.moveToFirst()) {
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
}
