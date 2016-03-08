package com.chen.remark.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import com.chen.remark.R;

import java.util.Calendar;

/**
 * Created by chenfayong on 16/3/7.
 */
public class DateTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private Calendar mDate = Calendar.getInstance();

    private boolean mIs24HourView;

    private OnDateTimeSetListener mOnDateTimeSetListener;

    private DateTimePicker mDateTimePicker;

    public interface OnDateTimeSetListener {
        void onDateTimeSet(AlertDialog dialog, long date);
    }

    protected DateTimePickerDialog(Context context, long date) {
        super(context);
        this.mDateTimePicker = new DateTimePicker(context);
        this.setView(this.mDateTimePicker);
        this.mDateTimePicker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {
            @Override
            public void onDateTimeChanged(DateTimePicker view, int year, int month, int dayOfMonth, int hourOfDay, int minute) {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, month);
                mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDate.set(Calendar.MINUTE, minute);
                updateTitle(mDate.getTimeInMillis());
            }
        });
        
        this.mDate.setTimeInMillis(date);
        this.mDate.set(Calendar.SECOND, 0);
        this.mDateTimePicker.setCurrentDate(this.mDate.getTimeInMillis());
        this.setButton(BUTTON_POSITIVE, context.getString(R.string.datetime_dialog_ok), this);
        this.setButton(BUTTON_NEGATIVE, context.getString(R.string.datetime_dialog_cancel), (OnClickListener)null);
        this.set24HourView(DateFormat.is24HourFormat(context));
        this.updateTitle(this.mDate.getTimeInMillis());
    }
    
    public void set24HourView(boolean is24HourView) {
        this.mIs24HourView = is24HourView;
    }
    
    public void setOnDateTimeSetListener(OnDateTimeSetListener callback) {
        this.mOnDateTimeSetListener = callback;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (this.mOnDateTimeSetListener != null) {
            this.mOnDateTimeSetListener.onDateTimeSet(this, this.mDate.getTimeInMillis());
        }
    }

    private void updateTitle(long date) {
        int flag = DateUtils.FORMAT_SHOW_YEAR |
                   DateUtils.FORMAT_SHOW_DATE |
                   DateUtils.FORMAT_SHOW_TIME |
                   DateUtils.FORMAT_SHOW_TIME;

        flag |= this.mIs24HourView ? DateUtils.FORMAT_24HOUR : DateUtils.FORMAT_12HOUR;
        this.setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }
}
