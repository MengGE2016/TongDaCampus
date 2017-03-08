package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

public class ActivityMoreNewsView extends AppCompatActivity {

    private Toolbar toolbar;

    private String newsLinkStr;

    private TextView contentTv;
    private ProgressDialog progressDialog;

    private FileServer fileServer;
    private String contentStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_news_view);

        initViews();
        getNewsContent();
    }

    private void initViews() {
        Intent intent = getIntent();
        String subTitle = intent.getStringExtra("SubTitle");
        newsLinkStr = intent.getStringExtra("Link");
        int flag = intent.getIntExtra("Flag", 0);
        String title = "通大要闻";
        switch (flag) {
            case FinalStrings.NetStrings.NEWS_XWDT:
                title = "新闻动态";
                break;
            case FinalStrings.NetStrings.NEWS_XYSH:
                title = "校园生活";
                break;
            case FinalStrings.NetStrings.NEWS_ZTBD:
                title = "专题报道";
                break;
            case FinalStrings.NetStrings.NEWS_MTTD:
                title = "媒体通大";
                break;
            case FinalStrings.NetStrings.NEWS_TZGG:
                title = "通知公告";
                break;
            case FinalStrings.NetStrings.NEWS_TDXW:
                title = "通大新闻";
                break;
        }


        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(ActivityMoreNewsView.this);
        progressDialog.setMessage("正在获取新闻内容......");
        fileServer = new FileServer(ActivityMoreNewsView.this);
        contentTv = (TextView) findViewById(R.id.more_news_view_tv);
    }

    //获取新闻内容
    private void getNewsContent() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String contentHtml = NetServer.getHtml(newsLinkStr);

                ActivityMoreNewsView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (contentHtml.length() != 0) {
                            contentStr = StringServer.getNewsContent(contentHtml);
                            progressDialog.dismiss();
                            Spanned spanned = Html.fromHtml(contentStr, new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String source) {
                                    //先检查本地图片缓存文件夹中是否有该图片资源
                                    Bitmap bitmap = fileServer.getBitmapFromSDcard(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR, "news" + source.replace("/", "-") + "mengge");
                                    //若没有则开启异步线程去加载图片
                                    if (bitmap == null) {
                                        new DrawerTask().execute(source);
                                        return null;
                                    } else { //若有则直接显示
                                        Drawable drawable = new BitmapDrawable(bitmap);
                                        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                                        float dWidth = drawable.getIntrinsicWidth();
                                        float dHeight = drawable.getIntrinsicHeight();
                                        int screenHeight = (int) (dHeight * (screenWidth - dWidth / 2.0f) / dWidth);

                                        drawable.setBounds(0, 0, (int) (screenWidth - dWidth / 2.0f), screenHeight);
                                        return drawable;
                                    }
                                }
                            }, null);
                            contentTv.setText(spanned);
                            contentTv.setMovementMethod(LinkMovementMethod.getInstance());
                        } else {
                            Toast.makeText(ActivityMoreNewsView.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
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
            Spanned spanned = Html.fromHtml(contentStr, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    Bitmap bitmap = fileServer.getBitmapFromSDcard(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR, "news" + source.replace("/", "-") + "mengge");
                    if (bitmap == null) return null;

                    Drawable drawable = new BitmapDrawable(bitmap);
                    float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    float dWidth = drawable.getIntrinsicWidth();
                    float dHeight = drawable.getIntrinsicHeight();
                    int screenHeight = (int) (dHeight * (screenWidth - dWidth / 2.0f) / dWidth);

                    drawable.setBounds(0, 0, (int) (screenWidth - dWidth / 2.0f), screenHeight);

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
                URL url = new URL("http://news.ntu.edu.cn" + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
                connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
                connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    Drawable drawable = Drawable.createFromStream(inputStream, strings[0]);
                    String imageNameStr = "news" + strings[0].replace("/", "-");
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

    //ToolBar返回按钮监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
