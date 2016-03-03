package com.chen.remark.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import com.chen.remark.R;
import com.chen.remark.constants.IntentConstants;
import com.chen.remark.dao.RemarkDAO;
import com.chen.remark.data.NotesProvider;
import com.chen.remark.data.TableRemark;
import com.chen.remark.model.Remark;
import com.chen.remark.ui.NoteEditActivity;
import com.chen.remark.ui.NotesListActivity;

/**
 * Created by chenfayong on 16/3/1.
 */
public abstract class NoteWidgetProvider extends AppWidgetProvider {

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_BG_COLOR_ID = 1;
    public static final int COLUMN_SNIPPET = 2;

    private static final String TAG = "NoteWidgetProvider";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        String where = TableRemark.RemarkColumns.WIDGET_ID +" = ?";
        ContentValues values = new ContentValues();
        values.put(TableRemark.RemarkColumns.WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        for (int i = 0; i < appWidgetIds.length; i++) {
            context.getContentResolver().update(NotesProvider.NotesResolver.AUTHORITY_REMARK, values,
                    where, new String[]{String.valueOf(appWidgetIds[i])});
        }
    }

    protected void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        update(context, appWidgetManager, appWidgetIds, false);
    }

    private void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                        boolean privacyMode) {
        RemarkDAO remarkDAO = new RemarkDAO(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            if (appWidgetIds[i] != AppWidgetManager.INVALID_APPWIDGET_ID) {

                Intent intent = new Intent(context, NoteEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(IntentConstants.INTENT_EXTRA_WIDGET_ID, appWidgetIds[i]);
                intent.putExtra(IntentConstants.INTENT_EXTRA_WIDGET_TYPE, getWidgetType());

                int bgId = 1;
                String snippet = "";
                Remark remark = remarkDAO.findByWidgetId(appWidgetIds[i]);
                if (remark != null) {
                    bgId =  R.drawable.widget_2x_yellow;
                    snippet = remark.getRemarkContent();
                    intent.putExtra(Intent.EXTRA_UID, remark.getRemarkId());
                    intent.setAction(Intent.ACTION_VIEW);
                } else {
                    snippet = context.getResources().getString(R.string.widget_have_not_content);
                    intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
                }

                RemoteViews rv = new RemoteViews(context.getPackageName(), getLayoutId());
                rv.setImageViewResource(R.id.widget_bg_image, this.getBgResourceId(bgId));
                intent.putExtra(IntentConstants.INTENT_EXTRA_BACKGROUND_ID, bgId);

                PendingIntent pendingIntent = null;
                if (privacyMode) {
                    rv.setTextViewText(R.id.widget_text, context.getString( R.string.widget_under_visit_mode));
                    pendingIntent = PendingIntent.getActivity(context, appWidgetIds[i],
                            new Intent(context, NotesListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                } else {
                    rv.setTextViewText(R.id.widget_text, snippet);
                    pendingIntent = PendingIntent.getActivity(context, appWidgetIds[i], intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }

                rv.setOnClickPendingIntent(R.id.widget_text, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            }
        }
    }


    protected abstract int getBgResourceId(int bgId);

    protected abstract int getLayoutId();

    protected abstract int getWidgetType();
}
