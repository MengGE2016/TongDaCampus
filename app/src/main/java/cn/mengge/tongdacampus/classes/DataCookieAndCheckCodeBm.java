package cn.mengge.tongdacampus.classes;

import android.graphics.Bitmap;

/**
 * Created by MengGE on 2016/10/15.
 */
public class DataCookieAndCheckCodeBm {
    private String cookie;
    private Bitmap bitmap;

    public DataCookieAndCheckCodeBm() {
    }

    public DataCookieAndCheckCodeBm(String cookie, Bitmap bitmap) {
        this.cookie = cookie;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
