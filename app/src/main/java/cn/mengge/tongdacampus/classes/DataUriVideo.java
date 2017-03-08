package cn.mengge.tongdacampus.classes;

import android.graphics.Bitmap;

/**
 * Created by MengGE on 2016/10/22.
 */
public class DataUriVideo {
    private String imageUrl;
    private String titleStr;
    private String videoUrl;

    public DataUriVideo( String titleStr, String imageUrl, String videoUrl) {
        this.titleStr = titleStr;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
