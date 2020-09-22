package com.rui.alphaplayer.bean;

import java.io.Serializable;

/**
 * Time: 2020/8/26
 * Author: jianrui
 * Description: 透明视频config model
 */
public class MyAlphaAdConfigModel implements Serializable {
    public Item landscape;

    public Item portrait;

    public Item getLandscapeItem() {
        return landscape;
    }

    public void setLandscapeItem(Item landscapeItem) {
        this.landscape = landscapeItem;
    }

    public Item getPortraitItem() {
        return portrait;
    }

    public void setPortraitItem(Item portraitItem) {
        this.portrait = portraitItem;
    }

    public class Item {
        public String path;
        public int align;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getAlignMode() {
            return align;
        }

        public void setAlignMode(int alignMode) {
            this.align = alignMode;
        }
    }
}
