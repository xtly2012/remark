package com.chen.remark.model;

import android.content.ContentValues;

/**
 * Created by chenfayong on 16/2/24.
 */
public class BaseBean {

    protected void addContentValue(ContentValues values, String columnName, String columnValue) {
        if (columnValue == null) {
            return;
        }

        values.put(columnName, columnValue);
    }

    protected void addContentValue(ContentValues values, String columnName, Long columnValue) {
        if (columnValue == null) {
            return;
        }

        values.put(columnName, columnValue);
    }

    protected void addContentValue(ContentValues values, String columnName, Integer columnValue) {
        if (columnValue == null) {
            return;
        }

        values.put(columnName, columnValue);
    }
}
