package com.chen.remark.listener;


import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.chen.remark.R;
import com.chen.remark.ui.DeleteConfirmMenu;
import com.chen.remark.ui.NoteEditActivity;
import com.chen.remark.ui.NotesListActivity;
import com.chen.remark.ui.NotesListItem;

/**
 * Created by chenfayong on 16/1/17.
 */
public class NotesListListener implements View.OnClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, ListView.MultiChoiceModeListener, MenuItem.OnMenuItemClickListener {
    private Activity notesActivity;

    private ListView notesListView;

    private Button addNewNoteButton;

    private DeleteConfirmMenu deleteConfirmMenu;

    private MenuItem menuMove;

    public NotesListListener(Activity activity) {
        this.notesActivity = activity;
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
            this.notesListView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }

        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        this.notesActivity.getMenuInflater().inflate(R.menu.note_list_options, menu);
        menu.findItem(R.id.delete).setOnMenuItemClickListener(this);
        this.menuMove = menu.findItem(R.id.move);
        this.menuMove.setVisible(false);
        menuMove.setOnMenuItemClickListener(this);
        this.addNewNoteButton.setVisibility(View.GONE);

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

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.notesActivity);
        builder.setTitle(this.notesActivity.getString(R.string.alert_title_delete));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(this.notesActivity.getString(R.string.alert_message_delete_notes, 2));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
//        Uri uri = Uri.parse("tel:18651625287");
//        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//        this.notesActivity.startActivity(intent);


//        Uri uri = Uri.parse("tel:18651625287");
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
