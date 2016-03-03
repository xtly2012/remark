package com.chen.remark.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import com.chen.remark.R;
import com.chen.remark.constants.RemarkConstants;

/**
 * Created by chenfayong on 16/3/2.
 */
public class NoteWidgetProvider_2x extends NoteWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.update(context, appWidgetManager, appWidgetIds);
    }

    @Override
    protected int getBgResourceId(int bgId) {
        return R.drawable.widget_2x_yellow;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_2x;
    }

    @Override
    protected int getWidgetType() {
        return RemarkConstants.WIDGET_TYPE_2X;
    }
}
