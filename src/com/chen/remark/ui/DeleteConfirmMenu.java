package com.chen.remark.ui;

import android.content.Context;
import android.view.Menu;
import android.widget.Button;
import android.widget.PopupMenu;
import com.chen.remark.R;

/**
 * Created by chenfayong on 16/1/30.
 */
public class DeleteConfirmMenu {

    private Button button;

    private PopupMenu popupMenu;

    private Menu menu;

    public DeleteConfirmMenu(Context context, Button button, int menuId) {
        this.button = button;
        this.button.setBackgroundResource(R.drawable.dropdown_icon);
        this.popupMenu = new PopupMenu(context, button);
    }
}
