package com.bytedance.tiktok.fragment

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.bytedance.tiktok.R
import com.bytedance.tiktok.activity.MainActivity
import com.bytedance.tiktok.activity.PlayListActivity
import com.bytedance.tiktok.adapter.VideoAdapter
import com.bytedance.tiktok.base.BaseFragment
import com.bytedance.tiktok.bean.*
import com.bytedance.tiktok.utils.OnVideoControllerListener
import com.bytedance.tiktok.utils.RxBus
import com.bytedance.tiktok.view.*
import com.bytedance.tiktok.view.viewpagerlayoutmanager.OnViewPagerListener
import com.bytedance.tiktok.view.viewpagerlayoutmanager.ViewPagerLayoutManager
import kotlinx.android.synthetic.main.fragment_recommend.*
import rx.functions.Action1
import java.util.*


/**
 * create by libo
 * create on 2020-05-19
 * description 推荐播放页
 */
class RecommendFragment : BaseFragment() {
    private var adapter: VideoAdapter? = null
    private var viewPagerLayoutManager: ViewPagerLayoutManager? = null

    /** 当前播放视频位置  */
    private var curPlayPos = -1
    private var videoView: FullScreenVideoView? = null

    private var ivCurCover: ImageView? = null

    //    var seekBar: SeekBar? = null
    var llInformation: LinearLayout? = null

    private val handler: Handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (videoView!!.isPlaying) {
                val current = videoView!!.currentPosition
                seekBar!!.progress = current
                for (i in 0..(childCount!! - 1)) {
                    var tipsBean = videoBean!!.tipsList[i]
                    if (videoBean!!.isTurnOn) {
                        if (tipsBean.startTime <= current && tipsBean.endTime > current) {
                            llInformation!!.getChildAt(i).visibility = View.VISIBLE
                        } else {
                            llInformation!!.getChildAt(i).visibility = View.GONE
                        }
                    }else{
                        llInformation!!.getChildAt(i).visibility = View.GONE
                    }

                }
                seekBar!!.max = videoView!!.duration
            }
            handler.postDelayed(this, 0)
        }
    }


    override fun setLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun init() {
        adapter = VideoAdapter(activity, DataCreate.datas)
        recyclerView!!.adapter = adapter
        videoView = FullScreenVideoView(activity)
        seekBar!!.setOnSeekBarChangeListener(onSeekBarChangeListener)
        setViewPagerLayoutManager()
        setRefreshEvent()

        //监听播放或暂停事件
        val subscribe = RxBus.getDefault().toObservable(PauseVideoEvent::class.java)
                .subscribe(Action1 { event: PauseVideoEvent ->
                    if (event.isPlayOrPause) {
                        videoView!!.start()
                        ivPlay!!.visibility = View.GONE
                    } else {
                        videoView!!.pause()
                    }
                } as Action1<PauseVideoEvent>)
//        subscribe.unsubscribe()
    }

    override fun onResume() {
        super.onResume()

        //返回时，推荐页面可见，则继续播放视频
        if (MainActivity.curMainPage == 0 && MainFragment.Companion.curPage == 1) {
            videoView!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        videoView!!.pause()
    }

    override fun onStop() {
        super.onStop()
        videoView!!.stopPlayback()
    }

    private fun setViewPagerLayoutManager() {
        viewPagerLayoutManager = ViewPagerLayoutManager(activity)
        recyclerView!!.layoutManager = viewPagerLayoutManager
        recyclerView!!.scrollToPosition(PlayListActivity.initPos)
        viewPagerLayoutManager!!.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
                playCurVideo(PlayListActivity.initPos)
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                if (ivCurCover != null) {
                    ivCurCover!!.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
                playCurVideo(position)
            }
        })
    }

    private fun setRefreshEvent() {
        refreshLayout!!.setColorSchemeResources(R.color.color_link)
        refreshLayout!!.setOnRefreshListener {
            object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    refreshLayout!!.isRefreshing = false
                }
            }.start()
        }
    }

    var ivPlay:ImageView? = null
    private fun playCurVideo(position: Int) {
        if (position == curPlayPos) {
            return
        }
        val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
        val rootView = itemView.findViewById<ViewGroup>(R.id.rl_container)
        val likeView: LikeView = rootView.findViewById(R.id.likeview)
        val controllerView: ControllerView = rootView.findViewById(R.id.controller)
        ivPlay = rootView.findViewById<ImageView>(R.id.iv_play)
        val ivCover = rootView.findViewById<ImageView>(R.id.iv_cover)
        ivPlay!!.alpha = 0.4f
        llInformation = controllerView.findViewById(R.id.llInformation)
        //播放暂停事件
        likeView.setOnPlayPauseListener(object : LikeView.OnPlayPauseListener {
            override fun onPlayOrPause() {
                if (videoView!!.isPlaying) {
                    videoView!!.pause()
                    ivPlay!!.visibility = View.VISIBLE
                } else {
                    videoView!!.start()
                    ivPlay!!.visibility = View.GONE
                }
            }

        })

        //评论点赞事件
        likeShareEvent(controllerView)

        //切换播放视频的作者主页数据
        RxBus.getDefault().post(CurUserBean(DataCreate.datas[position].userBean!!))
        curPlayPos = position

        //切换播放器位置
        dettachParentView(rootView)
        autoPlayVideo(curPlayPos, ivCover, seekBar!!)
    }

    /**
     * 移除videoview父view
     */
    private fun dettachParentView(rootView: ViewGroup) {
        //1.添加videoview到当前需要播放的item中,添加进item之前，保证ijkVideoView没有父view
        videoView?.parent?.let {
            (it as ViewGroup).removeView(videoView)
        }
        rootView.addView(videoView, 0)
    }

    var childCount: Int? = null
    var videoBean: VideoBean? = null

    /**
     * 自动播放视频
     */
    private fun autoPlayVideo(position: Int, ivCover: ImageView, seekBar: SeekBar) {
        llInformation!!.removeAllViews()
        val bgVideoPath = "android.resource://" + activity!!.packageName + "/" + DataCreate.datas[position].videoRes
        videoView!!.setVideoPath(bgVideoPath)
        videoView!!.start()
        videoView!!.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = true

            //延迟取消封面，避免加载视频黑屏
            object : CountDownTimer(200, 200) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    ivCover.visibility = View.GONE
                    ivCurCover = ivCover
                }
            }.start()
        }

        videoBean = DataCreate.datas[position]
        val list: ArrayList<TipsBean> = videoBean!!.tipsList
        for (i in list.indices) {
            var tipsBean = videoBean!!.tipsList[i]
            if (tipsBean.tipsType == TipsBean.TIPS_TYPE_WIKI) {
                val view: RelativeLayout = LayoutInflater.from(context).inflate(R.layout.baike_layout, null) as RelativeLayout

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0, 0, 0, 24)
                layoutParams.height = 102
                view.layoutParams = layoutParams

                val tvBaike: TextView = view.findViewById<TextView>(R.id.tv_baike)
                tvBaike.text = tipsBean.tipsText
                view.visibility = View.GONE
                llInformation!!.addView(view)
            } else if (tipsBean.tipsType == TipsBean.TIPS_TYPE_SHOPPING) {
                val view = LayoutInflater.from(context).inflate(R.layout.taobao_layout, null)

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0, 0, 0, 24)
                layoutParams.height = 102
                view.layoutParams = layoutParams

                val tvTaobao: TextView = view.findViewById<TextView>(R.id.tv_taobao)
                tvTaobao.text = tipsBean.tipsText
                view.visibility = View.GONE
                llInformation!!.addView(view)
            } else if (tipsBean.tipsType == TipsBean.TIPS_TYPE_NEWS) {
                val view = LayoutInflater.from(context).inflate(R.layout.zixun_layout, null)

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0, 0, 0, 24)
                layoutParams.height = 102
                view.layoutParams = layoutParams

                val tvZixun: TextView = view.findViewById<TextView>(R.id.tv_zixun)
                tvZixun.text = tipsBean.tipsText
                view.visibility = View.GONE
                llInformation!!.addView(view)
            } else if (tipsBean.tipsType == TipsBean.TIPS_TYPE_LOCATION || tipsBean.tipsType == "info") {
                val view = LayoutInflater.from(context).inflate(R.layout.location_layout, null)

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0, 0, 0, 24)
                layoutParams.height = 130 * 3
                view.layoutParams = layoutParams

                var ivLocation = view.findViewById<ImageView>(R.id.iv_location)
                ivLocation.setBackgroundResource(tipsBean.locationPic)

                val tvLocation: TextView = view.findViewById(R.id.tv_location)
                tvLocation.text = tipsBean.tipsText
                view.visibility = View.GONE
                llInformation!!.addView(view)
            }
        }

        childCount = llInformation!!.childCount

        for (i in 0..(childCount!! - 1)) {
            var view: View = llInformation!!.getChildAt(i)
            view!!.setOnClickListener(View.OnClickListener {
                val informationDialog = InformationDialog()
                informationDialog.setImgId(videoBean!!.tipsList[i].popupImg)
                informationDialog.show(childFragmentManager, "")
                videoView!!.pause()
            })
        }
        handler.postDelayed(runnable, 0)
    }

    private val onSeekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        // 当进度条停止修改的时候触发
        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // 取得当前进度条的刻度
            val progress = seekBar.progress
            if (videoView!!.isPlaying) {
                // 设置当前播放的位置
                videoView!!.seekTo(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                       fromUser: Boolean) {
        }
    }

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: ControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            override fun onHeadClick() {
                RxBus.getDefault().post(MainPageChangeEvent(1))
            }

            override fun onLikeClick() {}
            override fun onCommentClick() {
                val commentDialog = CommentDialog()
                commentDialog.show(childFragmentManager, "")
            }

            override fun onInformationClick() {
                val informationDialog = InformationDialog()
                informationDialog.show(childFragmentManager, "")
                videoView!!.pause()
                ivPlay!!.visibility = View.VISIBLE
            }

            override fun onShareClick() {
                ShareDialog().show(childFragmentManager, "")
            }
        })
    }
}