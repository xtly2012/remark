package com.chen.remark.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.chen.remark.data.TableRemark.RemarkColumns;

/**
 * Created by chenfayong on 16/2/19.
 */
public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static NotesDatabaseHelper databaseHelper;

    private static final String DB_NAME = "remark.db";

    private static final int DB_VERSION = 4;

    public interface TABLE {
        public static final String REMARK = "REMARK";

    }

    private static final String CREATE_REMARK_TABLE_SQL =
            "CREATE TABLE " +TABLE.REMARK +"("
                    + RemarkColumns.REMARK_ID +" INTEGER PRIMARY KEY,"
                    + RemarkColumns.CREATED_DATE + " INTEGER NOT NULL DEFAULT 0,"
                    + RemarkColumns.MODIFIED_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000),"
                    + RemarkColumns.ALERTED_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000),"
                    + RemarkColumns.WIDGET_ID + " INTEGER NOT NULL DEFAULT 0,"
                    + RemarkColumns.WIDGET_TYPE + " INTEGER NOT NULL DEFAULT -1,"
                    + RemarkColumns.BG_COLOR_ID + " INTEGER NOT NULL DEFAULT 0,"
                    + RemarkColumns.REMARK_CONTENT + " TEXT NOT NULL DEFAULT '',"
                    + RemarkColumns.CHECK_LIST_MODE + " INTEGER NOT NULL DEFAULT 0"
                    + ")";

    static synchronized NotesDatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new NotesDatabaseHelper(context);
        }

        return databaseHelper;
    }

    public NotesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createRemarkTable(db);
    }

    public void createRemarkTable(SQLiteDatabase db) {
        db.execSQL(CREATE_REMARK_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
