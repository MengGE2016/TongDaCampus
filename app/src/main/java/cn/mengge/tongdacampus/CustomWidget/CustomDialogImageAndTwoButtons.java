package cn.mengge.tongdacampus.CustomWidget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.mengge.tongdacampus.R;

/**
 * Created by MengGE on 2016/11/3.
 */
public class CustomDialogImageAndTwoButtons extends Dialog {

    public CustomDialogImageAndTwoButtons(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private String title;
        private ImageView imageView;
        private Bitmap bitmap;
        private View contentView;
        private Button storeBt;
        private Button cancelBt;
        private DialogInterface.OnClickListener storeBtonClickListener;
        private DialogInterface.OnClickListener cancelBtonClickListener;
        private String storeBtStr;
        private String cancelBtStr;


        public Builder(Context context){
            this.context = context;
        }

        /**
         * 从资源文件中获取字符串标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap){
            this.bitmap = bitmap;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置保存按钮文本并设置监听事件
         *
         * @param storeButtonText
         * @return
         */
        public Builder setStoreButton(int storeButtonText,
                                      DialogInterface.OnClickListener listener) {
            this.storeBtStr = (String) context
                    .getText(storeButtonText);
            this.storeBtonClickListener = listener;
            return this;
        }

        public Builder setStoreButton(String storeButtonText,
                                      DialogInterface.OnClickListener listener) {
            this.storeBtStr = storeButtonText;
            this.storeBtonClickListener = listener;
            return this;
        }

        /**
         * 设置取消按钮文本并设置监听事件
         *
         * @param cancelBtText
         * @return
         */

        public Builder setCancelButton(int cancelBtText,
                                       DialogInterface.OnClickListener listener) {
            this.cancelBtStr = (String) context
                    .getText(cancelBtText);
            this.cancelBtonClickListener = listener;
            return this;
        }

        public Builder setCancelButton(String cancelBtText,
                                       DialogInterface.OnClickListener listener) {
            this.cancelBtStr = cancelBtText;
            this.cancelBtonClickListener = listener;
            return this;
        }


        public CustomDialogImageAndTwoButtons create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 用自定义主题实例化对话框
            final CustomDialogImageAndTwoButtons dialog = new CustomDialogImageAndTwoButtons(context, R.style.ImageDialog);
            View layout = inflater.inflate(R.layout.custom_dialog_image_two_buttons, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // 设置对话框标题
            ((TextView) layout.findViewById(R.id.about_us_dialog_title_tv)).setText(title);
            //设置对话框图片
            ((ImageView)layout.findViewById(R.id.about_us_dialog_er_coder_iv)).setImageBitmap(bitmap);
            // 设置保存按钮
            if (storeBtStr != null) {
                ((Button) layout.findViewById(R.id.about_us_dialog_store_bt))
                        .setText(storeBtStr);
                if (storeBtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.about_us_dialog_store_bt))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    storeBtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {

                layout.findViewById(R.id.about_us_dialog_store_bt).setVisibility(
                        View.GONE);
            }
            // 设置取消按钮
            if (cancelBtStr != null) {
                ((Button) layout.findViewById(R.id.about_us_dialog_cancel_bt))
                        .setText(cancelBtStr);
                if (cancelBtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.about_us_dialog_cancel_bt))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    cancelBtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {

                layout.findViewById(R.id.about_us_dialog_cancel_bt).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
