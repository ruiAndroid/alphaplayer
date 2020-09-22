package com.rui.alphaplayer;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.rui.alphaplayer.bean.MyAlphaAdConfigModel;
import com.rui.alphaplayer.callback.IMyMonitor;
import com.rui.alphaplayer.callback.IMyPlayerAction;
import com.rui.alphaplayer.utils.MyAlphaAdJsonUtils;
import com.ss.ugc.android.alpha_player.controller.PlayerController;
import com.ss.ugc.android.alpha_player.model.AlphaVideoViewType;
import com.ss.ugc.android.alpha_player.model.Configuration;
import com.ss.ugc.android.alpha_player.model.DataSource;
import com.ss.ugc.android.alpha_player.model.ScaleType;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Time: 2020/9/1
 * Author: jianrui
 * Description: 透明视频播放器
 */
public class AlphaPlayerView extends FrameLayout {
    private static final String TAG = "AlphaPlayerView";
    private Context mContext;

    //播放器
    private RelativeLayout mVideoContainer;

    //视频播放器类型:GL_SURFACE_VIEW/GL_SURFACE_VIEW
    private AlphaVideoViewType mViewType=AlphaVideoViewType.GL_SURFACE_VIEW;

    //播放控制器
    private PlayerController mPlayerController;

    private Configuration mPlayConfiguration;

    //解码类型，默认软解
    private boolean mEnableMediaCodec=false;

    //上层播放器,ijkPlayer
    private AlphaPlayer mAlphaPlayer;

    private IMyPlayerAction mIPlayerAction;

    private IMyMonitor mIMonitor;



    public AlphaPlayerView(@NonNull Context context) {
        super(context);
        this.mContext=context;
        initView();
    }

    public AlphaPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initView();
    }

    public AlphaPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    /**
     * 初始化
     */
    public void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.view_player,this);
        mVideoContainer = findViewById(R.id.alpha_play_view);
        mAlphaPlayer=new AlphaPlayer(mContext);
    }

    /**
     * 初始化播放控制器
     * @param lifecycleOwner
     */
    public void initController(LifecycleOwner lifecycleOwner){
        if(mPlayConfiguration==null){
            mPlayConfiguration=new Configuration(mContext, lifecycleOwner);
            mPlayConfiguration.setAlphaVideoViewType(mViewType);
        }

        if(mPlayerController==null && mAlphaPlayer!=null){
            //默认设置软解类型
            mAlphaPlayer.setEnableMediaCodec(mEnableMediaCodec);
            mPlayerController = new PlayerController(mContext,lifecycleOwner,mPlayConfiguration.getAlphaVideoViewType(),mAlphaPlayer);

            if(mIMonitor!=null){
                mPlayerController.setMonitor(monitor);
            }

            if(mIPlayerAction!=null){
                mPlayerController.setPlayerAction(playerAction);
            }
        }
        attachPlayView();
    }


    /**
     * 播放视频
     * playContainer:用于播放的container
     * videoPath:视频源文件路径
     */
    public void play(String videoPath){
        if(TextUtils.isEmpty(videoPath)){
            Log.e(TAG,"videopath is null");
            return;
        }
        File dirFile=new File(videoPath);

        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        MyAlphaAdConfigModel configModel = MyAlphaAdJsonUtils.parseConfigModel(dirFile.getAbsolutePath());
        if(configModel!=null){
            DataSource mDataSource=new DataSource();
            mDataSource.baseDir=videoPath;
            mDataSource.setPortraitPath(configModel.getPortraitItem().getPath(),configModel.getPortraitItem().getAlignMode());
            mDataSource.setLandscapePath(configModel.getLandscapeItem().getPath(),configModel.getLandscapeItem().getAlignMode());
            if(mDataSource.isValid()){
                mVideoContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerController.start(mDataSource);
                    }
                });
            }
        }

    }


    public void attachPlayView(){
        if(mPlayerController!=null){
            mPlayerController.attachAlphaView(mVideoContainer);
        }
    }


    public void detachPlayView(){
        if(mPlayerController!=null){
            mPlayerController.detachAlphaView(mVideoContainer);
        }
    }

    /**
     * 释放
     */
    public void releasePlayController(){
        if(mPlayerController!=null){
            this.detachPlayView();
            mPlayerController.release();
        }
    }

    public AlphaVideoViewType getViewType() {
        return mViewType;
    }

    public void setViewType(AlphaVideoViewType mViewType) {
        this.mViewType = mViewType;
    }

    public boolean isEnableMediaCodec() {
        return mEnableMediaCodec;
    }

    public void setEnableMediaCodec(boolean mEnableMediaCodec) {
        this.mEnableMediaCodec = mEnableMediaCodec;
    }

    public void setPlayerAction(IMyPlayerAction mIPlayerAction) {
        this.mIPlayerAction = mIPlayerAction;
    }

    public void setMonitor(IMyMonitor mIMonitor) {
        this.mIMonitor = mIMonitor;
    }

    private com.ss.ugc.android.alpha_player.IPlayerAction playerAction =new com.ss.ugc.android.alpha_player.IPlayerAction() {
        @Override
        public void onVideoSizeChanged(int i, int i1, @NotNull ScaleType scaleType) {
            if(mIPlayerAction!=null){
                mIPlayerAction.onVideoSizeChanged(i,i1);
            }
        }

        @Override
        public void startAction() {
            if(mIPlayerAction!=null){
                mIPlayerAction.startAction();
            }
        }

        @Override
        public void endAction() {
            if(mIPlayerAction!=null){
                mIPlayerAction.endAction();
            }
        }
    };

    private com.ss.ugc.android.alpha_player.IMonitor monitor=new com.ss.ugc.android.alpha_player.IMonitor() {
        @Override
        public void monitor(boolean b, @NotNull String s, int i, int i1, @NotNull String s1) {
            if(mIMonitor!=null){
                mIMonitor.monitor(b,s,i,i1,s1);
            }
        }
    };
}

