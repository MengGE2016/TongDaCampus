package cn.mengge.tongdacampus.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MengGE on 2016/10/18.
 */
public class DrawerServer {

    public static Bitmap getBitmapFromResourceId(Context context, int resourceId) {
        Bitmap bitmap = null;
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
