package cn.mengge.tongdacampus.CustomWidget;

import android.util.Log;

/**
 * Created by MengGE on 2016/11/2.
 */
public class LogUtil {

    private final static int VERBOSE = 1;
    private final static int DEBUG = 2;
    private final static int INFO = 3;
    private final static int WARN = 4;
    private final static int ERROR = 5;
    private final static int NOTHING = 6;
    private final static int LEVEL = VERBOSE;

    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v("log" + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.d("log" + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.i("log" + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.w("log" + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.e("log" + tag, msg);
        }
    }
}
