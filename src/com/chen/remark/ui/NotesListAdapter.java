package com.chen.remark.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chen.remark.R;
import com.chen.remark.model.Remark;

import java.util.List;

/**
 * Created by chenfayong on 16/1/14.
 */
public class NotesListAdapter extends ArrayAdapter<Remark> {

    private int resource;

    private Context context;

    public NotesListAdapter(Context context, int resource, List<Remark> itemList) {
        super(context, resource, itemList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotesListItem todoView;
        Remark remark = getItem(position);

        if (convertView == null) {
            todoView = new NotesListItem(this.context);
        } else {
            todoView = (NotesListItem) convertView;
        }

        todoView.bind(this.context, remark);
        return todoView;
    }
}
