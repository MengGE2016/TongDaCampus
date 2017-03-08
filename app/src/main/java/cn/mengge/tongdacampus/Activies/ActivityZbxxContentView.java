package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.FileServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityZbxxContentView extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView contentTv;
    private ProgressDialog progressDialog;
    private Intent intent;

    private FileServer fileServer;
    private String link;
    private String itemMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbxx_content_view);
        initViews();
        getContentAndsetTextView();
    }

    private void initViews() {
        intent = getIntent();
        boolean contentFlag = intent.getBooleanExtra("CONTENT_FLAG", false);
        String subTitle = intent.getStringExtra("TITLE");
        link = intent.getStringExtra("LINK");
        fileServer = new FileServer(ActivityZbxxContentView.this);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        if (contentFlag) {
            toolbar.setTitle("中标公示");
        } else toolbar.setTitle("招标公告");
        toolbar.setSubtitle(subTitle);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contentTv = (TextView) findViewById(R.id.zbxx_content_view_sc_tv);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        progressDialog = new ProgressDialog(ActivityZbxxContentView.this);
        progressDialog.setMessage("正在获取内容......");
    }

    private void getContentAndsetTextView() {
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String htmlStr = NetServer.getHtml(link);
                ActivityZbxxContentView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (htmlStr.length() != 0) {
                            itemMessage = StringServer.getZbxxMessage(htmlStr);
                            Spanned spanned = Html.fromHtml(itemMessage, new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String s) {
                                    //先检查本地图片缓存文件夹中是否有该图片资源
                                    Bitmap bitmap = fileServer.getBitmapFromSDcard(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR, "zbxx" + s.replace("/", "-") + "mengge");
                                    //若没有则开启异步线程去加载图片
                                    if (bitmap == null) {
                                        new DrawerTask().execute(s);
                                        return null;
                                    } else { //若有则直接显示
                                        Drawable drawable = new BitmapDrawable(bitmap);
                                        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                                        float dWidth = drawable.getIntrinsicWidth();
                                        float dHeight = drawable.getIntrinsicHeight();
                                        int screenHeight = (int) (dHeight * (screenWidth - dWidth / 2.0f) / dWidth);

                                        drawable.setBounds(0, 0, (int) dWidth*5, (int) dHeight*5);
                                        return drawable;
                                    }
                                }
                            }, null);
                            contentTv.setText(spanned);
                            progressDialog.dismiss();
                        } else
                            Toast.makeText(ActivityZbxxContentView.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //开启异步线程从网络加载图片
    private class DrawerTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Map<String, Object> map) {
            super.onPostExecute(map);
            fileServer.saveImageFile2SD(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR, map.get("imageName").toString() + "mengge", ((BitmapDrawable) map.get("image")).getBitmap());
            Spanned spanned = Html.fromHtml(itemMessage, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    Bitmap bitmap = fileServer.getBitmapFromSDcard(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR, "zbxx" + source.replace("/", "-") + "mengge");
                    if (bitmap == null) return null;

                    Drawable drawable = new BitmapDrawable(bitmap);
                    float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    float dWidth = drawable.getIntrinsicWidth();
                    float dHeight = drawable.getIntrinsicHeight();
                    int screenHeight = (int) (dHeight * (screenWidth - dWidth / 2.0f) / dWidth);

                    drawable.setBounds(0, 0, (int) dWidth*5, (int) dHeight*5);

                    return drawable;
                }
            }, null);
            contentTv.setText(spanned);

        }

        @Override
        protected Map<String, Object> doInBackground(String... strings) {
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            Map<String, Object> map = new HashMap<>();
            try {
                URL url = new URL("http://ztb.ntu.edu.cn" + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
                connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
                connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    Drawable drawable = Drawable.createFromStream(inputStream, strings[0]);
                    String imageNameStr = "zbxx" + strings[0].replace("/", "-");
                    map.put("imageName", imageNameStr);
                    map.put("image", drawable);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
                try {
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return map;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
