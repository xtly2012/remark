package com.chen.remark.tool;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import com.chen.remark.R;
import com.chen.remark.model.Remark;

import java.io.*;
import java.util.List;

/**
 * Created by chenfayong on 16/3/10.
 */
public class BackupUtils {

    private static final String TAG = "BackupUtils";

    public void exportToText(Context context, List<Remark> remarkList) {
        if (!externalStorageAvailable()) {
            Log.d(TAG, "Media was not mounted");
            return;
        }

        PrintStream printStream = getExportToTextPrintStream(context);
        if (printStream == null) {
            Log.e(TAG, "get print stream error");
            return;
        }



    }

    private static boolean externalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static PrintStream getExportToTextPrintStream(Context context) {
        File file = generateFileMountedOnSDcard(context, R.string.file_path, R.string.file_name_txt_format);
        if (file == null) {
            Log.e(TAG, "create file to exported failed");
            return null;
        }

        String fileName = file.getName();
        String fileDirectory = context.getString(R.string.file_path);
        PrintStream printStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return null;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return null;
        }

        return  printStream;
    }

    private static File generateFileMountedOnSDcard(Context context, int filePathResId, int fileNameFormatResId) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(Environment.getExternalStorageDirectory());
        strBuf.append(context.getString(filePathResId));
        File fileDir = new File(strBuf.toString());
        String dateStr = DateFormat.format("yyyyMMdd", System.currentTimeMillis()).toString();
        strBuf.append(context.getString(fileNameFormatResId, dateStr));
        File file = new File(strBuf.toString());

        try {
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            return file;
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }
}
