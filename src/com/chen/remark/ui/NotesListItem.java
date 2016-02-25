package com.chen.remark.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chen.remark.R;
import com.chen.remark.model.Remark;
import com.chen.remark.tool.DataUtils;

/**
 * Created by chenfayong on 16/2/22.
 */
public class NotesListItem extends LinearLayout {

    private TextView tvName;

    private TextView tvTitle;

    private TextView tvTime;

    private CheckBox checkbox;

    private Remark remark;

    public NotesListItem(Context context) {
        super(context);
        this.inflate(context, R.layout.note_item, this);
        this.tvTitle = (TextView)this.findViewById(R.id.tv_title);
        this.tvTime = (TextView)this.findViewById(R.id.tv_time);
        this.checkbox = (CheckBox)this.findViewById(android.R.id.checkbox);
    }

    public void bind(Context context, Remark data) {
        this.remark = data;
        this.setBackgroundResource(R.drawable.list_yellow_middle);
        this.tvTitle.setTextAppearance(context, R.style.TextAppearancePrimaryItem);
        this.tvTitle.setText(DataUtils.getFormattedSnippet(data.getRemarkContent()));
        this.tvTime.setText(DateUtils.getRelativeTimeSpanString(data.getModifiedDate()));
    }

    public Remark getRemark() {
        return remark;
    }
}
