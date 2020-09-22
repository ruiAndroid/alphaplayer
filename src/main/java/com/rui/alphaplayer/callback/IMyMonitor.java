package com.rui.alphaplayer.callback;

/**
 * Time: 2020/9/2
 * Author: jianrui
 * Description:
 */
public interface IMyMonitor {
    void monitor(boolean result, String playType,int what,int extra,String errorInfo);
}
