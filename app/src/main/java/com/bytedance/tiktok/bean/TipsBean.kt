package com.bytedance.tiktok.bean

/**
 * Created by admin on 2021/7/3.
 */
class TipsBean {

    companion object{
        /**
         * 购物
         */
        const val TIPS_TYPE_SHOPPING = "shopping"

        /**
         * 百科
         */
        const val TIPS_TYPE_WIKI = "wiki"

        /**
         * 新闻
         */
        const val TIPS_TYPE_NEWS = "news"

        /**
         * 定位
         */
        const val TIPS_TYPE_LOCATION = "location"
    }


    constructor(startTime: Long, endTime: Long, tipsType: String,  tipsText: String, popupImg: Int, locationPic:Int = 0) {
        this.startTime = startTime
        this.endTime = endTime
        this.tipsType = tipsType
        this.tipsText = tipsText
        this.popupImg = popupImg
        this.locationPic = locationPic
    }

    var startTime = 0L

    var endTime = 0L

    var tipsType = TIPS_TYPE_SHOPPING


    var tipsText = ""

    var popupImg = 0

    var popupImgFile = ""

    var locationPic = 0

    var locationPicFile = ""
}