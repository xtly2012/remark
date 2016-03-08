package com.chen.remark.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import android.text.format.DateFormat;
import android.widget.NumberPicker;
import com.chen.remark.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by chenfayong on 16/3/4.
 */
public class DateTimePicker extends FrameLayout {

    private static final boolean DEFAULT_ENABLE_STATE = true;

    private static final int HOURS_IN_HALF_DAY = 12;

    private static final int HOURS_IN_ALL_DAY = 24;

    private static final int DAYS_IN_ALL_WEEK = 7;

    private static final int DATE_SPINNER_MIN_VAL = 0;

    private static final int DATE_SPINNER_MAX_VAL = DAYS_IN_ALL_WEEK - 1;

    private static final int HOUR_SPINNER_MIN_VAL_24_HOUR_VIEW = 0;

    private static final int HOUR_SPINNER_MAX_VAL_24_HOUR_VIEW = 23;

    private static final int HOUR_SPINNER_MIN_VAL_12_HOUR_VIEW = 1;

    private static final int HOUR_SPINNER_MAX_VAL_12_HOUR_VIEW = 12;

    private static final int MINUTE_SPINNER_MIN_VAL = 0;

    private static final int MINUTE_SPINNER_MAX_VAL = 59;

    private static final int AMPM_SPINNER_MIN_VAL = 0;

    private static final int AMPM_SPINNER_MAX_VAL = 0;

    private final NumberPicker mDateSpinner;

    private final NumberPicker mHourSpinner;

    private final NumberPicker mMinuteSpinner;

    private final NumberPicker mAmPmSpinner;

    private Calendar mDate;

    private String[] mDateDisplayValues = new String[DAYS_IN_ALL_WEEK];

    private boolean mIsAm;

    private boolean mIs24HourView;

    private boolean mIsEnabled = DEFAULT_ENABLE_STATE;

    private boolean mInitialising;

    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    private NumberPicker.OnValueChangeListener mOnDateChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mDate.add(Calendar.DAY_OF_YEAR, newVal - oldVal);
            updateDateControl();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            boolean isDateChanged = false;
            Calendar cal = Calendar.getInstance();
            if (!mIs24HourView) {
                if (!mIsAm && oldVal == HOURS_IN_HALF_DAY - 1 && newVal == HOURS_IN_HALF_DAY) {
                    cal.setTimeInMillis(mDate.getTimeInMillis());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    isDateChanged = true;
                } else if (mIsAm && oldVal == HOURS_IN_HALF_DAY && newVal == HOURS_IN_HALF_DAY - 1) {
                    cal.setTimeInMillis(mDate.getTimeInMillis());
                    cal.add(Calendar.DAY_OF_YEAR, - 1);
                    isDateChanged = true;
                }

                if (oldVal == HOURS_IN_HALF_DAY - 1 && newVal  == HOURS_IN_HALF_DAY ||
                        oldVal == HOURS_IN_HALF_DAY && newVal == HOURS_IN_HALF_DAY - 1) {
                    mIsAm = !mIsAm;
                    updateAmPmControl();
                }
            } else {
                if (oldVal == HOURS_IN_ALL_DAY - 1 && newVal == 0) {
                    cal.setTimeInMillis(mDate.getTimeInMillis());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    isDateChanged = true;
                } else if (oldVal == 0 && newVal == HOURS_IN_ALL_DAY - 1) {
                    cal.setTimeInMillis(mDate.getTimeInMillis());
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                    isDateChanged = true;
                }
            }
            int newHour = mHourSpinner.getValue() % HOURS_IN_HALF_DAY + (mIsAm ? 0 :HOURS_IN_HALF_DAY);
            mDate.set(Calendar.HOUR_OF_DAY, newHour);
            onDateTimeChanged();
            if (isDateChanged) {
                setCurrentYear(cal.get(Calendar.YEAR));
                setCurrentMonth(cal.get(Calendar.MONTH));
                setCurrentDay(cal.get(Calendar.DAY_OF_MONTH));
            }
        }
    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            int minValue = mMinuteSpinner.getMinValue();
            int maxValue = mMinuteSpinner.getMaxValue();
            int offset = 0;
            if (oldVal == maxValue && newVal == minValue) {
                offset += 1;
            } else if (oldVal == minValue && newVal == maxValue) {
                offset -= 1;
            }

            if (offset != 0) {
                mDate.add(Calendar.HOUR_OF_DAY, offset);
                mHourSpinner.setValue(getCurrentHour());
                updateDateControl();
                int newHour = getCurrentHourOfDay();
                if (newHour >= HOURS_IN_HALF_DAY) {
                    mIsAm = false;
                    updateAmPmControl();
                } else {
                    mIsAm = true;
                    updateAmPmControl();
                }
            }
            mDate.set(Calendar.MINUTE, newVal);
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnAmPmChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mIsAm = !mIsAm;
            if (mIsAm) {
                mDate.add(Calendar.HOUR_OF_DAY, -HOURS_IN_HALF_DAY);
            } else {
                mDate.add(Calendar.HOUR_OF_DAY, HOURS_IN_HALF_DAY);
            }

            updateAmPmControl();
            onDateTimeChanged();
        }
    };

    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker view, int year, int month, int dayOfMonth, int hourOfDay, int minute);
    }

    public DateTimePicker(Context context) {
        this(context, System.currentTimeMillis());
    }

    public DateTimePicker(Context context, long date) {
        this(context, date, DateFormat.is24HourFormat(context));
    }

    public DateTimePicker(Context context, long date, boolean is24HourView) {
        super(context);
        this.mDate = Calendar.getInstance();
        this.inflate(context, R.layout.datetime_picker, this);

        this.mDateSpinner = (NumberPicker)this.findViewById(R.id.date);
        this.mDateSpinner.setMinValue(DATE_SPINNER_MIN_VAL);
        this.mDateSpinner.setMaxValue(DATE_SPINNER_MAX_VAL);
        this.mDateSpinner.setOnValueChangedListener(this.mOnDateChangedListener);

        this.mHourSpinner = (NumberPicker)this.findViewById(R.id.hour);
        this.mHourSpinner.setOnValueChangedListener(this.mOnHourChangedListener);

        this.mMinuteSpinner = (NumberPicker)this.findViewById(R.id.minute);
        this.mMinuteSpinner.setMinValue(MINUTE_SPINNER_MIN_VAL);
        this.mMinuteSpinner.setMaxValue(MINUTE_SPINNER_MAX_VAL);
        this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
        this.mMinuteSpinner.setOnValueChangedListener(this.mOnMinuteChangedListener);

        String[] stringsForAmPm = new DateFormatSymbols().getAmPmStrings();
        this.mAmPmSpinner = (NumberPicker)this.findViewById(R.id.amPm);
        this.mAmPmSpinner.setMinValue(AMPM_SPINNER_MIN_VAL);
        this.mAmPmSpinner.setMaxValue(AMPM_SPINNER_MAX_VAL);
        this.mAmPmSpinner.setDisplayedValues(stringsForAmPm);
        this.mAmPmSpinner.setOnValueChangedListener(this.mOnAmPmChangedListener);

        // update controls to initial state
        updateDateControl();
        updateHourControl();
        updateAmPmControl();

        this.set24HourView(is24HourView);

        // set to current time
        this.setCurrentDate(date);

        // set the content descriptions
        this.mInitialising = false;
    }

    private void updateDateControl() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.mDate.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -DAYS_IN_ALL_WEEK / 2 -1);
        this.mDateSpinner.setDisplayedValues(null);
        for (int i = 0; i < DAYS_IN_ALL_WEEK; ++i) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            this.mDateDisplayValues[i] = (String)DateFormat.format("MM.dd EEEE", cal);
        }

        this.mDateSpinner.setDisplayedValues(this.mDateDisplayValues);
        this.mDateSpinner.setValue(DAYS_IN_ALL_WEEK / 2);
        this.mDateSpinner.invalidate();
    }

    private void updateHourControl() {
        if (this.mIs24HourView) {
            this.mHourSpinner.setMinValue(HOUR_SPINNER_MIN_VAL_24_HOUR_VIEW);
            this.mHourSpinner.setMaxValue(HOUR_SPINNER_MAX_VAL_24_HOUR_VIEW);
        } else {
            this.mHourSpinner.setMinValue(HOUR_SPINNER_MIN_VAL_12_HOUR_VIEW);
            this.mHourSpinner.setMaxValue(HOUR_SPINNER_MAX_VAL_12_HOUR_VIEW);
        }
    }

    private void updateAmPmControl() {
        if (this.mIs24HourView) {
            this.mAmPmSpinner.setValue(View.GONE);
        } else {
            this.mAmPmSpinner.setMinValue(AMPM_SPINNER_MIN_VAL);
            this.mAmPmSpinner.setMaxValue(AMPM_SPINNER_MAX_VAL);
        }
    }

    /**
     * Set current hour in 24 hour mode, in the range (0~23)
     *
     * @param hourOfDay
     */
    public void setCurrentHour(int hourOfDay) {
        if (!this.mInitialising && hourOfDay == this.getCurrentHourOfDay()) {
            return;
        }

        this.mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        if (!this.mIs24HourView) {
            if (hourOfDay >= HOURS_IN_HALF_DAY) {
                this.mIsAm = false;
                if (hourOfDay > HOURS_IN_HALF_DAY) {
                    hourOfDay -= HOURS_IN_HALF_DAY;
                }
            } else {
                this.mIsAm = true;
                if (hourOfDay == 0) {
                    hourOfDay = HOURS_IN_HALF_DAY;
                }
            }
            updateAmPmControl();
        }

        this.mHourSpinner.setValue(hourOfDay);
        this.onDateTimeChanged();
    }

    public int getCurrentMinute() {
        return this.mDate.get(Calendar.MINUTE);
    }

    public void setCurrentMinute(int minute) {
        if (!this.mInitialising && minute == this.getCurrentMinute()) {
            return;
        }

        this.mMinuteSpinner.setValue(minute);
        this.mDate.set(Calendar.MINUTE, minute);
        onDateTimeChanged();
    }

    public void set24HourView(boolean is24HourView) {
        if (this.mIs24HourView == is24HourView) {
            return;
        }

        this.mIs24HourView = is24HourView;
        this.mAmPmSpinner.setVisibility(is24HourView ? View.GONE : View.VISIBLE);
        int hour = this.getCurrentHourOfDay();
        this.updateHourControl();
        this.setCurrentHour(hour);
        this.updateAmPmControl();
    }

    /**
     * Set the current date
     *
     * @param date The current date in mills
     */
    public void setCurrentDate(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        this.setCurrentDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    /**
     * Set the current date
     *
     * @param year The current year
     * @param month The current month
     * @param dayOfMonth The current day of month
     * @param hourOfDay The current hour of day
     * @param minute The current minute
     */
    public void setCurrentDate(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        this.setCurrentYear(year);
        this.setCurrentMonth(month);
        this.setCurrentDay(dayOfMonth);
        this.setCurrentHour(hourOfDay);
        this.setCurrentMinute(minute);
    }


    public int getCurrentYear() {
        return this.mDate.get(Calendar.YEAR);
    }

    /**
     * Set current year
     *
     * @param year The current year
     */
    public void setCurrentYear(int year) {
        if (!this.mInitialising && year == this.getCurrentYear()) {
            return;
        }

        this.mDate.set(Calendar.YEAR, year);
        this.updateDateControl();
        this.onDateTimeChanged();
    }

    /**
     * Get current month in the year
     *
     * @return The current month in the year
     */
    public int getCurrentMonth() {
        return this.mDate.get(Calendar.MONTH);
    }

    /**
     * Set current month in the year
     *
     * @param month The month in the year
     */
    public void setCurrentMonth(int month) {
        if (!this.mInitialising && month == getCurrentMonth()) {
            return;
        }

        this.mDate.set(Calendar.MONTH, month);
        updateDateControl();
        onDateTimeChanged();
    }

    /**
     * Get current day of the month
     *
     * @return The day of the month
     */
    public int getCurrentDay() {
        return this.mDate.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Set current day of the  month
     *
     * @param dayOfMonth The current day of month
     */
    public void setCurrentDay(int dayOfMonth) {
        if (!this.mInitialising && dayOfMonth == getCurrentDay()) {
            return;
        }

        this.mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateControl();
        onDateTimeChanged();
    }

    /**
     * Get current hour in 24 hour mode, in the range (0~23)
     *
     * @return The current hour in 24 hour mode
     */
    public int getCurrentHourOfDay() {
        return mDate.get(Calendar.HOUR_OF_DAY);
    }

    private int getCurrentHour() {
        if (this.mIs24HourView) {
            return this.getCurrentHourOfDay();
        } else {
            int hour = this.getCurrentHourOfDay();
            if (hour > HOURS_IN_HALF_DAY) {
                return hour - HOURS_IN_HALF_DAY;
            } else {
                return hour == 0 ? HOURS_IN_HALF_DAY : hour;
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.mIsEnabled == enabled) {
            return;
        }

        super.setEnabled(enabled);
        this.mDateSpinner.setEnabled(enabled);
        this.mHourSpinner.setEnabled(enabled);
        this.mMinuteSpinner.setEnabled(enabled);
        this.mAmPmSpinner.setEnabled(enabled);
        this.mIsEnabled = enabled;
    }

    /**
     * Set the callback that indicates the 'Set' button has been pressed.
     *
     * @param callback the callback, if null will do nothing
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        this.mOnDateTimeChangedListener = callback;
    }


    private void onDateTimeChanged() {
        if (this.mOnDateTimeChangedListener != null) {
            this.mOnDateTimeChangedListener.onDateTimeChanged(this, this.getCurrentYear(),
                    this.getCurrentMonth(), this.getCurrentDay(), this.getCurrentHourOfDay(), this.getCurrentMinute());
        }
    }
}
