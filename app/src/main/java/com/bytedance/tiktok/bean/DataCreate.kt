package com.bytedance.tiktok.bean

import android.text.TextUtils
import android.util.JsonReader
import android.util.Log
import com.bytedance.tiktok.R
import com.bytedance.tiktok.application.App
import com.bytedance.tiktok.bean.VideoBean.UserBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.util.*

/**
 * create by libo
 * create on 2020-06-03
 * description 本地数据创建，代替接口获取数据
 */
class DataCreate {

    fun getJson(): String {
        var builder = StringBuilder()
        try {
            var assetManager = App.context!!.assets;
            var bf = BufferedReader(InputStreamReader(assetManager.open("video_data.json")))
            var line = ""
            while ((bf.readLine().also { line = it }) != null) {
                builder.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.toString().trim()
    }

    fun initData() {
//
//        val videoBeanNew1 = VideoBean()
//        videoBeanNew1.coverRes = R.mipmap.cover1
//        videoBeanNew1.content = "#街坊 #颜值打分 给自己颜值打100分的女生集合"
//        videoBeanNew1.videoRes = R.raw.video1
//        videoBeanNew1.distance = 7.9f
//        videoBeanNew1.isFocused = false
//        videoBeanNew1.isLiked = true
//        videoBeanNew1.likeCount = 226823
//        videoBeanNew1.commentCount = 3480
//        videoBeanNew1.shareCount = 4252
////        videoBeanNew1.tipsList.add(
////                TipsBean(2000, 8000,
////                        TipsBean.TIPS_TYPE_WIKI,
////                        0,
////                        "百科: 小熊猫",
////                        0))
////        videoBeanNew1.tipsList.add(
////                TipsBean(9000, 16000,
////                        TipsBean.TIPS_TYPE_TEXT,
////                        0,
////                        "百科: 小熊猫",
////                        0))
//
//
//        val videoBeanOne = VideoBean()
//        videoBeanOne.videoId = 1
//        videoBeanOne.coverRes = R.mipmap.cover1
//        videoBeanOne.content = "#街坊 #颜值打分 给自己颜值打100分的女生集合"
//        videoBeanOne.videoRes = R.raw.video1
//        videoBeanOne.distance = 7.9f
//        videoBeanOne.isFocused = false
//        videoBeanOne.isLiked = true
//        videoBeanOne.likeCount = 226823
//        videoBeanOne.commentCount = 3480
//        videoBeanOne.shareCount = 4252
//
//
//        val userBeanOne = UserBean()
//        userBeanOne.uid = 1
//        userBeanOne.head = R.mipmap.head1
//        userBeanOne.nickName = "南京街坊"
//        userBeanOne.sign = "你们喜欢的话题，就是我们采访的内容"
//        userBeanOne.subCount = 119323
//        userBeanOne.focusCount = 482
//        userBeanOne.fansCount = 32823
//        userBeanOne.workCount = 42
//        userBeanOne.dynamicCount = 42
//        userBeanOne.likeCount = 821
//        userList.add(userBeanOne)
//        videoBeanOne.userBean = userBeanOne
//
//        val videoBeanTwo = VideoBean()
//        videoBeanTwo.videoId = 2
//        videoBeanTwo.coverRes = R.mipmap.cover2
//        videoBeanTwo.content = "400 户摊主开进济南环联夜市，你们要的烟火气终于来了！"
//        videoBeanTwo.videoRes = R.raw.video2
//        videoBeanTwo.distance = 19.7f
//        videoBeanTwo.isFocused = true
//        videoBeanTwo.isLiked = false
//        videoBeanTwo.likeCount = 1938230
//        videoBeanTwo.commentCount = 8923
//        videoBeanTwo.shareCount = 5892
//
//        val userBeanTwo = UserBean()
//        userBeanTwo.uid = 2
//        userBeanTwo.head = R.mipmap.head2
//        userBeanTwo.nickName = "民生直通车"
//        userBeanTwo.sign = "直通现场新闻，直击社会热点，深入事件背后，探寻事实真相"
//        userBeanTwo.subCount = 20323234
//        userBeanTwo.focusCount = 244
//        userBeanTwo.fansCount = 1938232
//        userBeanTwo.workCount = 123
//        userBeanTwo.dynamicCount = 123
//        userBeanTwo.likeCount = 344
//        userList.add(userBeanTwo)
//        videoBeanTwo.userBean = userBeanTwo
//
//        val videoBeanThree = VideoBean()
//        videoBeanThree.videoId = 3
//        videoBeanThree.coverRes = R.mipmap.cover3
//        videoBeanThree.content = "科比生涯霸气庆祝动作，最后动作诠释了一生荣耀 #科比 @路人王篮球 "
//        videoBeanThree.videoRes = R.raw.video3
//        videoBeanThree.distance = 15.9f
//        videoBeanThree.isFocused = false
//        videoBeanThree.isLiked = false
//        videoBeanThree.likeCount = 592032
//        videoBeanThree.commentCount = 9221
//        videoBeanThree.shareCount = 982
//
//        val userBeanThree = UserBean()
//        userBeanThree.uid = 3
//        userBeanThree.head = R.mipmap.head3
//        userBeanThree.nickName = "七叶篮球"
//        userBeanThree.sign = "老科的视频会一直保留，想他了就回来看看"
//        userBeanThree.subCount = 1039232
//        userBeanThree.focusCount = 159
//        userBeanThree.fansCount = 29232323
//        userBeanThree.workCount = 171
//        userBeanThree.dynamicCount = 173
//        userBeanThree.likeCount = 1724
//        userList.add(userBeanThree)
//        videoBeanThree.userBean = userBeanThree
//
//        val videoBeanFour = VideoBean()
//        videoBeanFour.videoId = 4
//        videoBeanFour.coverRes = R.mipmap.cover4
//        videoBeanFour.content = "美好的一天，从发现美开始 #莉莉柯林斯 "
//        videoBeanFour.videoRes = R.raw.video4
//        videoBeanFour.distance = 25.2f
//        videoBeanFour.isFocused = false
//        videoBeanFour.isLiked = false
//        videoBeanFour.likeCount = 887232
//        videoBeanFour.commentCount = 2731
//        videoBeanFour.shareCount = 8924
//
//        val userBeanFour = UserBean()
//        userBeanFour.uid = 4
//        userBeanFour.head = R.mipmap.head4
//        userBeanFour.nickName = "一只爱修图的剪辑师"
//        userBeanFour.sign = "接剪辑，活动拍摄，修图单\n 合作私信"
//        userBeanFour.subCount = 2689424
//        userBeanFour.focusCount = 399
//        userBeanFour.fansCount = 360829
//        userBeanFour.workCount = 562
//        userBeanFour.dynamicCount = 570
//        userBeanFour.likeCount = 4310
//        userList.add(userBeanFour)
//        videoBeanFour.userBean = userBeanFour
//
//        val videoBeanFive = VideoBean()
//        videoBeanFive.videoId = 5
//        videoBeanFive.coverRes = R.mipmap.cover5
//        videoBeanFive.content = "有梦就去追吧，我说到做到。 #网球  #网球小威 "
//        videoBeanFive.videoRes = R.raw.video5
//        videoBeanFive.distance = 9.2f
//        videoBeanFive.isFocused = false
//        videoBeanFive.isLiked = false
//        videoBeanFive.likeCount = 8293241
//        videoBeanFive.commentCount = 982
//        videoBeanFive.shareCount = 8923
//
//        val userBeanFive = UserBean()
//        userBeanFive.uid = 5
//        userBeanFive.head = R.mipmap.head5
//        userBeanFive.nickName = "国际网球联合会"
//        userBeanFive.sign = "ITF国际网球联合会负责制定统一的网球规则，在世界范围内普及网球运动"
//        userBeanFive.subCount = 1002342
//        userBeanFive.focusCount = 87
//        userBeanFive.fansCount = 520232
//        userBeanFive.workCount = 89
//        userBeanFive.dynamicCount = 122
//        userBeanFive.likeCount = 9
//        userList.add(userBeanFive)
//        videoBeanFive.userBean = userBeanFive
//
//        val videoBeanSix = VideoBean()
//        videoBeanSix.videoId = 6
//        videoBeanSix.coverRes = R.mipmap.cover6
//        videoBeanSix.content = "能力越大，责任越大，英雄可能会迟到，但永远不会缺席  #蜘蛛侠 "
//        videoBeanSix.videoRes = R.raw.video6
//        videoBeanSix.distance = 16.4f
//        videoBeanSix.isFocused = true
//        videoBeanSix.isLiked = true
//        videoBeanSix.likeCount = 2109823
//        videoBeanSix.commentCount = 9723
//        videoBeanFive.shareCount = 424
//
//        val userBeanSix = UserBean()
//        userBeanSix.uid = 6
//        userBeanSix.head = R.mipmap.head6
//        userBeanSix.nickName = "罗鑫颖"
//        userBeanSix.sign = "一个行走在Tr与剪辑之间的人\n 有什么不懂的可以来直播间问我"
//        userBeanSix.subCount = 29342320
//        userBeanSix.focusCount = 67
//        userBeanSix.fansCount = 7028323
//        userBeanSix.workCount = 5133
//        userBeanSix.dynamicCount = 5159
//        userBeanSix.likeCount = 0
//        userList.add(userBeanSix)
//        videoBeanSix.userBean = userBeanSix
//
//        val videoBeanSeven = VideoBean()
//        videoBeanSeven.videoId = 7
//        videoBeanSeven.coverRes = R.mipmap.cover7
//        videoBeanSeven.content = "真的拍不出来你的神颜！现场看大屏帅疯！#陈情令南京演唱会 #王一博 😭"
//        videoBeanSeven.videoRes = R.raw.video7
//        videoBeanSeven.distance = 16.4f
//        videoBeanSeven.isFocused = false
//        videoBeanSeven.isLiked = false
//        videoBeanSeven.likeCount = 185782
//        videoBeanSeven.commentCount = 2452
//        videoBeanSeven.shareCount = 3812
//
//        val userBeanSeven = UserBean()
//        userBeanSeven.uid = 7
//        userBeanSeven.head = R.mipmap.head7
//        userBeanSeven.nickName = "Sean"
//        userBeanSeven.sign = "云深不知处"
//        userBeanSeven.subCount = 471932
//        userBeanSeven.focusCount = 482
//        userBeanSeven.fansCount = 371423
//        userBeanSeven.workCount = 242
//        userBeanSeven.dynamicCount = 245
//        userBeanSeven.likeCount = 839
//        userList.add(userBeanSeven)
//        videoBeanSeven.userBean = userBeanSeven
//
//        val videoBeanEight = VideoBean()
//        videoBeanEight.videoId = 8
//        videoBeanEight.coverRes = R.mipmap.cover8
//        videoBeanEight.content = "逆序只是想告诉大家，学了舞蹈的她气质开了挂！"
//        videoBeanEight.videoRes = R.raw.video8
//        videoBeanEight.distance = 8.4f
//        videoBeanEight.isFocused = false
//        videoBeanEight.isLiked = false
//        videoBeanEight.likeCount = 1708324
//        videoBeanEight.commentCount = 8372
//        videoBeanEight.shareCount = 982
//
//        val userBeanEight = UserBean()
//        userBeanEight.uid = 8
//        userBeanEight.head = R.mipmap.head8
//        userBeanEight.nickName = "曹小宝"
//        userBeanEight.sign = "一个晒娃狂魔麻麻，平日里没啥爱好！喜欢拿着手机记录孩子成长片段，风格不喜勿喷！"
//        userBeanEight.subCount = 1832342
//        userBeanEight.focusCount = 397
//        userBeanEight.fansCount = 1394232
//        userBeanEight.workCount = 164
//        userBeanEight.dynamicCount = 167
//        userBeanEight.likeCount = 0
//        userList.add(userBeanEight)
//        videoBeanEight.userBean = userBeanEight

//        datas.add(videoBeanOne)
//        datas.add(videoBeanTwo)
//        datas.add(videoBeanThree)
//        datas.add(videoBeanFour)
//        datas.add(videoBeanFive)
//        datas.add(videoBeanSix)
//        datas.add(videoBeanSeven)
//        datas.add(videoBeanEight)
//        datas.add(videoBeanOne)
//        datas.add(videoBeanTwo)
//        datas.add(videoBeanThree)
//        datas.add(videoBeanFour)
//        datas.add(videoBeanFive)
//        datas.add(videoBeanSix)
//        datas.add(videoBeanSeven)
//        datas.add(videoBeanEight)

        val typeOfSrc: Type = object : TypeToken<ArrayList<VideoBean>>() {}.type
//        for (value in datas.groupBy {
//            it.videoId % 2
//        }.values) {
//            Log.i("zyang",Gson().toJson(value,  typeOfSrc))
//            Log.i("zyang","===============")
//        }

//        Log.i("zyang",Gson().toJson(datas,  typeOfSrc))

//        val typeOfSrc1: Type = object : TypeToken<ArrayList<UserBean>>() {}.type
//        Log.i("zyang",Gson().toJson(userList,  typeOfSrc1))

        var jsonStr = getJson()

        var list = Gson().fromJson(jsonStr, typeOfSrc) as ArrayList<VideoBean>;
        transfromRes(list)

        datas.clear()
        datas.addAll(list)
        userList.clear()
        for (videoBean in list) {
            userList.add(videoBean.userBean!!)
        }
        for (videoBean in list) {
            userList.add(videoBean.userBean!!)
        }
        for (videoBean in list) {
            userList.add(videoBean.userBean!!)
        }
        Log.i("zyang1", userList.size.toString())
    }

    companion object {
        @JvmField
        var datas = ArrayList<VideoBean>()

        @JvmField
        var userList = ArrayList<UserBean>()
    }


    fun transfromRes(list: ArrayList<VideoBean>) {
        var res = App.context!!.resources
        var packageName = App.context!!.packageName
        for (videoBean in list) {
            videoBean.coverRes = res.getIdentifier(videoBean.coverResFile, "mipmap", packageName)
            videoBean.videoRes = res.getIdentifier(videoBean.videoResFile, "raw", packageName)

            videoBean.userBean!!.head = res.getIdentifier(videoBean.userBean!!.headFile, "mipmap", App.context!!.packageName)

            for (tipsBean in videoBean.tipsList) {
                if (!TextUtils.isEmpty(tipsBean.locationPicFile)) {
                    tipsBean.locationPic = res.getIdentifier(tipsBean.locationPicFile, "mipmap", packageName)
                }
                tipsBean.popupImg = res.getIdentifier(tipsBean.popupImgFile, "mipmap", packageName)
            }
        }

    }

}