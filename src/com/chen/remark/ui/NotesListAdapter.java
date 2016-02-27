package com.chen.remark.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chen.remark.R;
import com.chen.remark.model.Remark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfayong on 16/1/14.
 */
public class NotesListAdapter extends ArrayAdapter<Remark> {

    private int resource;

    private Context context;

    private boolean mChoiceMode;

    private Map<Integer, Boolean> mSelectedIndex;

    public NotesListAdapter(Context context, int resource, List<Remark> itemList) {
        super(context, resource, itemList);
        this.mSelectedIndex = new HashMap<>();
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

        todoView.bind(this.context, remark, this.mChoiceMode, this.ifSelectedItem(position));
        return todoView;
    }

    public boolean ifInChoiceMode() {
        return this.mChoiceMode;
    }

    public void setChoiceMode(boolean mode) {
        this.mSelectedIndex.clear();
        this.mChoiceMode = mode;
    }

    public void setCheckedItem(final int position, final boolean checked) {
        this.mSelectedIndex.put(position, checked);
        this.notifyDataSetChanged();
    }

    public boolean ifSelectedItem(final int position) {
        if (null == this.mSelectedIndex.get(position)) {
            return false;
        }

        return this.mSelectedIndex.get(position);
    }
}
