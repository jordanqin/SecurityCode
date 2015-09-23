package com.jordanqin.securitycode.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by qjd on 2015/9/3.
 * desc:
 */

public class ClipboardUtils {
    public static void putTextIntoClipboard(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy text", text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
