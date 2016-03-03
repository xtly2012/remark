package com.chen.remark.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.text.TextUtils;
import android.util.Log;
import com.chen.remark.data.NotesDatabaseHelper.TABLE;
import com.chen.remark.data.TableRemark.RemarkColumns;

/**
 * Created by chenfayong on 16/2/20.
 */
public class NotesProvider extends ContentProvider {

    private static final String TAG = "NotesProvider";

    public static final String AUTHORITY = "com.chen.remark.data.NotesProvider";

    private static final UriMatcher uriMatcher;

    private NotesDatabaseHelper databaseHelper;

    private static final int URI_REMARK = 1;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "remark", URI_REMARK);
    }

    public interface NotesResolver {
        public Uri AUTHORITY_REMARK = Uri.parse("content://" + AUTHORITY + "/remark");
    };

    @Override
    public boolean onCreate() {
        databaseHelper = this.databaseHelper = NotesDatabaseHelper.getInstance(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readDatabase = this.databaseHelper.getReadableDatabase();
        Cursor cursor = readDatabase.query(TABLE.REMARK, projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor != null) {
            cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase writeDatabase = this.databaseHelper.getWritableDatabase();
        long insertId = writeDatabase.insert(TABLE.REMARK, null, values);

        Log.e(TAG, "insert id is " +insertId);
        return ContentUris.withAppendedId(uri, insertId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase writeDatabase = this.databaseHelper.getWritableDatabase();
        selection = "(" +selection +") AND " + RemarkColumns.REMARK_ID +">0";
        int count = writeDatabase.delete(TABLE.REMARK, selection, selectionArgs);
        if (count > 0) {
            this.getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase writeDatabase = this.databaseHelper.getWritableDatabase();
        int count = writeDatabase.update(TABLE.REMARK, values, selection, selectionArgs);

        if (count > 0) {
            this.getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
