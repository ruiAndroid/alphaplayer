package com.rui.alphaplayer.callback;

/**
 * Time: 2020/9/2
 * Author: jianrui
 * Description:
 */
public interface IMyPlayerAction {
    void onVideoSizeChanged(int videoWidth, int videoHeight);

    void startAction();

    void endAction();
}
