package com.example.fitfo.Define

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class Dialog {
    fun showConfirmDialog(context: Context, title: String, message: String, onConfirm: () -> Unit = {}, onCancel: () -> Unit = {}) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("Có") { dialog, which ->
            onConfirm.invoke()
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Không") { dialog, which ->
            onCancel.invoke()
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showMessageDialog(context: Context, title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val titleColor: Int = when (title.toLowerCase()) {
            "success" -> Color.GREEN
            "error" -> Color.RED
            "warning" -> Color.YELLOW
            else -> return
        }
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        val alertDialog = alertDialogBuilder.create()
        val titleView = alertDialog.findViewById<TextView>(android.R.id.title)
        titleView?.setTextColor(titleColor)
        alertDialog.show()

        // Đóng dialog sau 5 giây
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alertDialog.dismiss() }, 3000)
    }
}
