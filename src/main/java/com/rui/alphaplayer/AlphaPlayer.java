package com.rui.alphaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.ss.ugc.android.alpha_player.model.VideoInfo;
import com.ss.ugc.android.alpha_player.player.AbsPlayer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Time: 2020/8/26
 * Author: jianrui
 * Description: alpha player
 */
public class AlphaPlayer extends AbsPlayer {

    private static final String TAG = "AlphaPlayer";
    private IjkMediaPlayer ijkMediaPlayer;

    private int currVideoWidth;
    private int currVideoHeight;

    private Context mContext;
    //解码类型
    private boolean mEnableMediaCodec=false;

    public AlphaPlayer(Context context){
        this.mContext=context;
    }

    public boolean isEnableMediaCodec() {
        return mEnableMediaCodec;
    }

    public void setEnableMediaCodec(boolean mEnableMediaCodec) {
        this.mEnableMediaCodec = mEnableMediaCodec;
    }

    @NotNull
    @Override
    public String getPlayerType() {
        return "AlphaPlayer";

    }

    @NotNull
    @Override
    public VideoInfo getVideoInfo() throws Exception {
        return new VideoInfo(currVideoWidth, currVideoHeight);

    }

    @Override
    public void initMediaPlayer() throws Exception {

        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        ijkMediaPlayer.setVolume(1.0f, 1.0f);
        //默认使用软解
        setEnableMediaCodec(ijkMediaPlayer,mEnableMediaCodec);
        ijkMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                //MEDIA_INFO_VIDEO_RENDERING_START
                Log.i(TAG,"onInfo:"+i+",i1:"+i1);
                if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    firstFrameListener.onFirstFrame();
                }
                return false;
            }
        });
        ijkMediaPlayer.setOnPreparedListener(onPreparedListener);
        ijkMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        ijkMediaPlayer.setOnErrorListener(onErrorListener);
        ijkMediaPlayer.setLogEnabled(true);
        ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                completionListener.onCompletion();

            }
        });
        ijkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                Log.i(TAG,"onBufferingUpdate i:"+i);
            }
        });
//        exoPlayer.addVideoListener(exoVideoListener);
    }


    private IMediaPlayer.OnErrorListener onErrorListener=new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            errorListener.onError(0, 0, "ExoPlayer on error i:"+i+",i1:"+i1);
            return false;
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener=new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            currVideoWidth = i;
            currVideoHeight = i1;
        }
    };

    private IMediaPlayer.OnPreparedListener onPreparedListener=new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            Log.i(TAG,"onPrepared");
            preparedListener.onPrepared();
        }
    };

    //设置是否开启硬解码
    private void setEnableMediaCodec(IjkMediaPlayer ijkMediaPlayer, boolean isEnable) {
        int value = isEnable ? 1 : 0;
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);//开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);
    }
    @Override
    public void pause() {
        ijkMediaPlayer.pause();

    }

    @Override
    public void prepareAsync() {
        ijkMediaPlayer.prepareAsync();
    }

    @Override
    public void release() {
        ijkMediaPlayer.release();

    }

    @Override
    public void reset() {
        ijkMediaPlayer.stop();

    }

    @Override
    public void setDataSource(@NotNull String dataPath) throws IOException {
        Log.i(TAG,"dataPath:"+dataPath);
        ijkMediaPlayer.setDataSource(mContext, Uri.parse(dataPath),null);
    }

    @Override
    public void setLooping(boolean looping) {

    }

    @Override
    public void setScreenOnWhilePlaying(boolean b) {

    }

    @Override
    public void setSurface(@NotNull Surface surface) {
        ijkMediaPlayer.setSurface(surface);

    }

    @Override
    public void start() {
        ijkMediaPlayer.start();

    }

    @Override
    public void stop() {
        ijkMediaPlayer.stop();

    }
}
