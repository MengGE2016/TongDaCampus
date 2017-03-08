package cn.mengge.tongdacampus.CustomWidget;

import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by MengGE on 2016/10/16.
 */
public class ApplicationSetTypefaceFont extends android.app.Application {

    public static Typeface typeface;

    @Override
    public void onCreate() {
        super.onCreate();
        setTypeface();
    }

    public void setTypeface() {
//        typeface = Typeface.createFromAsset(getAssets(), "fonts/YouYuan.ttf");
        try {
//            Field serifField = Typeface.class.getDeclaredField("SERIF");
//            serifField.setAccessible(true);
//            serifField.set(null, typeface);
//
//            Field defaultField = Typeface.class.getDeclaredField("DEFAULT");
//            defaultField.setAccessible(true);
//            defaultField.set(null, typeface);
//
//            Field monosapceField = Typeface.class.getDeclaredField("MONOSPACE");
//            monosapceField.setAccessible(true);
//            monosapceField.set(null, typeface);
//
            Field sansSerifField = Typeface.class.getDeclaredField("SANS_SERIF");
            sansSerifField.setAccessible(true);
            sansSerifField.set(null, typeface);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
