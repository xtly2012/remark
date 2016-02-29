package com.chen.remark.listener;


import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.chen.remark.R;
import com.chen.remark.dao.RemarkDAO;
import com.chen.remark.model.Remark;
import com.chen.remark.tool.DataUtils;
import com.chen.remark.ui.*;

import java.util.List;

/**
 * Created by chenfayong on 16/1/17.
 */
public class NotesListListener implements View.OnClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, ListView.MultiChoiceModeListener, MenuItem.OnMenuItemClickListener {
    private Activity notesActivity;

    private ListView notesListView;

    private NotesListAdapter notesListAdapter;

    private Button addNewNoteButton;

    private MenuItem menuMove;

    private ActionMode mActionMode;

    public NotesListListener(Activity activity, NotesListAdapter notesListAdapter) {
        this.notesActivity = activity;
        this.notesListAdapter = notesListAdapter;
        this.notesListView = (ListView)this.notesActivity.findViewById(R.id.notes_list);
        this.addNewNoteButton = (Button)this.notesActivity.findViewById(R.id.btn_new_note);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_note:
                createNewNote();
                break;

            default:
                break;
        }
    }

    private void createNewNote() {
        Intent intent = new Intent(notesActivity, NoteEditActivity.class);
        intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
        notesActivity.startActivityForResult(intent, NotesListActivity.REQUEST_CODE_NEW_NOTE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.notesListView.startActionMode(this) != null) {
            this.onItemCheckedStateChanged(null, position, id, true);
            this.notesListView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }

        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        this.notesListAdapter.setCheckedItem(position, checked);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        this.notesActivity.getMenuInflater().inflate(R.menu.note_list_options, menu);
        menu.findItem(R.id.delete).setOnMenuItemClickListener(this);
        this.menuMove = menu.findItem(R.id.move);
        this.menuMove.setVisible(false);
        menuMove.setOnMenuItemClickListener(this);
        this.addNewNoteButton.setVisibility(View.GONE);

        this.notesListAdapter.setChoiceMode(true);
        this.notesListView.setLongClickable(false);
        this.mActionMode = mode;

        View customView = LayoutInflater.from(this.notesActivity).inflate(R.layout.note_list_dropdown_menu, null);
        mode.setCustomView(customView);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.notesListAdapter.setChoiceMode(false);
        this.notesListView.setLongClickable(true);
        this.addNewNoteButton.setVisibility(View.VISIBLE);
    }

    public void finishActionMode() {
        this.mActionMode.finish();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (notesListAdapter.getSelectedCount() == 0) {
            Toast.makeText(this.notesActivity, this.notesActivity.getString(R.string.menu_select_none),
                    Toast.LENGTH_SHORT).show();
            return true;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this.notesActivity);
        builder.setTitle(this.notesActivity.getString(R.string.alert_title_delete));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(this.notesActivity.getString(R.string.alert_message_delete_notes, this.notesListAdapter.getSelectedCount()));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemarkDAO remarkDAO = new RemarkDAO(notesActivity);
                List<Long> itemIdList = notesListAdapter.getCheckedItemId();
                for (Long itemId : itemIdList) {
                    remarkDAO.removeByRemarkId(itemId);
                }

                notesListAdapter.removeCheckedPosition();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view instanceof NotesListItem) {
            if (this.notesListAdapter.ifInChoiceMode()) {
                position = position - this.notesListView.getHeaderViewsCount();
                this.onItemCheckedStateChanged(null, position, id, !this.notesListAdapter.ifSelectedItem(position));
                return;
            }
        }

        Log.i("android list view item has selected: position = " + position, "ListView");

        NotesListItem listItem = (NotesListItem)view;
        Intent intent = new Intent(this.notesActivity, NoteEditActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_UID, listItem.getRemark().getRemarkId());
        this.notesActivity.startActivityForResult(intent, NotesListActivity.REQUEST_CODE_OPEN_NODE);


//        联系人
//        Uri uri = Uri.parse("content://contacts/people");
//        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//        this.notesActivity.startActivityForResult(intent, 2);

//        打电话
//        Uri uri = Uri.parse("tel:12345678911");
//        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//        this.notesActivity.startActivity(intent);


//        Uri uri = Uri.parse("tel:12345678911");
//        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//        this.notesActivity.startActivity(intent);

/**
        NotificationManager manager = (NotificationManager)this.notesActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = R.drawable.icon_app;
        String tickerText = "Remark notification";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);

        Context context = this.notesActivity.getApplicationContext();      // application Context
        CharSequence contentTitle = "My notification";  // message title
        CharSequence contentText = "Hello World!";      // message text
        Intent notificationIntent = new Intent(this.notesActivity, this.notesActivity.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(this.notesActivity, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        manager.notify(1, notification);
**/
    }
}
