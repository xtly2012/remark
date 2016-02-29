package com.chen.remark.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chen.remark.R;
import com.chen.remark.model.Remark;

import java.util.*;

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

    public int getSelectedCount() {
        Collection<Boolean> values = this.mSelectedIndex.values();
        if (values == null) {
            return 0;
        }

        int count = 0;
        Iterator<Boolean> iter = values.iterator();
        while (iter.hasNext()) {
            if (true == iter.next()) {
                count++;
            }
        }

        return count;
    }

    public long getItemId(int position) {
        Remark remark = this.getItem(position);
        if (remark == null) {
            return 0;
        }

        return remark.getRemarkId();
    }

    public List<Long> getCheckedItemId() {
        List<Long> itemIdList = new ArrayList<>();
        Set<Map.Entry<Integer, Boolean>> entrySet = this.mSelectedIndex.entrySet();
        Iterator<Map.Entry<Integer, Boolean>> entryIter = entrySet.iterator();
        while (entryIter.hasNext()) {
            Map.Entry<Integer, Boolean> entry = entryIter.next();
            if (true == entry.getValue()) {
                itemIdList.add(this.getItemId(entry.getKey()));
            }
        }

        return itemIdList;
    }

    public void removeCheckedPosition() {
        Set<Map.Entry<Integer, Boolean>> entrySet = this.mSelectedIndex.entrySet();
        Iterator<Map.Entry<Integer, Boolean>> entryIter = entrySet.iterator();
        while (entryIter.hasNext()) {
            Map.Entry<Integer, Boolean> entry = entryIter.next();
            if (true == entry.getValue()) {
                Remark remark = this.getItem(entry.getKey());
                this.remove(remark);
            }
        }

    }
}
