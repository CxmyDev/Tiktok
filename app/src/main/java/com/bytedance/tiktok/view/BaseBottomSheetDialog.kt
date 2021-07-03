package com.bytedance.tiktok.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bytedance.tiktok.R
import com.bytedance.tiktok.bean.PauseVideoEvent
import com.bytedance.tiktok.utils.RxBus
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * create by libo
 * create on 2020-05-27
 * description
 */
open class BaseBottomSheetDialog : BottomSheetDialogFragment() {
    private var bottomSheet: FrameLayout? = null
    var behavior: BottomSheetBehavior<FrameLayout>? = null
    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        bottomSheet = dialog.delegate.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet!!.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            bottomSheet!!.layoutParams = layoutParams
            behavior = BottomSheetBehavior.from(bottomSheet)
            behavior?.peekHeight = height
            // 初始为展开状态
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED

            behavior?.setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        RxBus.getDefault().post(PauseVideoEvent(true))
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialog)
        isCancelable = true
    }

    protected open val height: Int
        protected get() = resources.displayMetrics.heightPixels

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    protected fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
//    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val d = super.onCreateDialog(savedInstanceState)
//        d.setOnShowListener { dialog ->
//            val d = dialog as BottomSheetDialog
//            val bottomSheet = d.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
//            val behaviour: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//            behaviour.setBottomSheetCallback(object : BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                        handleUserExit()
//                        dismiss()
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//            })
//        }
//        return d
//    }
}