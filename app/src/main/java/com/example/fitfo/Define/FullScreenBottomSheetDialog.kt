package com.example.fitfo.Define

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fitfo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FullScreenBottomSheetDialog(context: Context) : BottomSheetDialog(context, R.style.FullScreen) {

    fun createAndShowLoadingDialog(layoutResId: Int, dismissDelayMillis: Long) {
        val viewLoading = LayoutInflater.from(context).inflate(layoutResId, null)
        viewLoading?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        viewLoading?.minimumHeight = screenHeight

        val behavior = behavior
        behavior.peekHeight = screenHeight
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        setContentView(viewLoading)
        show()

        // Giả mạo một công việc không đồng bộ (ví dụ: call API) bằng cách sử dụng coroutine
        CoroutineScope(Dispatchers.Main).launch {
            // Thực hiện call API hoặc bất kỳ công việc không đồng bộ nào tại đây
            delay(dismissDelayMillis)
            dismiss() // Đóng dialog khi công việc hoàn thành
        }
    }

    fun showLoading(layoutResId: Int) {
        createAndShowLoadingDialog(layoutResId, 5000)
    }

    fun dismissLoading() {
        dismiss()
    }
}

