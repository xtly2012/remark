package com.chen.remark.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import com.chen.remark.R;
import com.chen.remark.constants.RemarkConstants;

/**
 * Created by chenfayong on 16/3/2.
 */
public class NoteWidgetProvider_4x extends NoteWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.update(context, appWidgetManager, appWidgetIds);
    }

    @Override
    protected int getBgResourceId(int bgId) {
        return R.drawable.widget_4x_yellow;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_4x;
    }

    @Override
    protected int getWidgetType() {
        return RemarkConstants.WIDGET_TYPE_4X;
    }
}
