package dev.luanramos.thelightningnodesrank.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast

fun Context.copyToClipboard(label: String, text: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(this, label, Toast.LENGTH_LONG).show()
    }
}