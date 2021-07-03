package com.bytedance.tiktok.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bytedance.tiktok.R

/**
 * create by libo
 * create on 2020-05-24
 * description 评论弹框
 */
class InformationDialog : BaseBottomSheetDialog() {

    var imgId:Int ? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_info, container)
        var ivPopImg= view!!.findViewById<ImageView>(R.id.iv_popImg)
        ivPopImg.setImageResource(imgId!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    protected override val height: Int
        protected get() = resources.displayMetrics.heightPixels - 600

    fun setImgId(resId: Int) {
        this.imgId = resId
    }
}