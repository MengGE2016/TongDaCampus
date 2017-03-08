package cn.mengge.tongdacampus.Activies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityStart extends AppCompatActivity {
//    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private AlphaAnimation alphaAnimation;
    private boolean fisrtLoginBl;
    private SharedPreferences preferences;

    private ImageView imageView;

//    private Thread imageThread;
//
//
//    private String imageUrlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        fisrtLoginBl = preferences.getBoolean(FinalStrings.PrefServerStrings.STUDENT_FIRST_LOGIN, true);
        String imageUrl = preferences.getString("start_page_image_url"," ").toString();
        //        linearLayout = (LinearLayout) findViewById(R.id.start_activity_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.start_activity_layout);
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
//        linearLayout.startAnimation(alphaAnimation);
        relativeLayout.startAnimation(alphaAnimation);

        View view = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
        imageView = (ImageView) findViewById(R.id.start_iv);
        if(fisrtLoginBl) Glide.with(ActivityStart.this).load(R.mipmap.start_page).into(imageView);
        if (!(imageUrl.length()<=5) && NetServer.isNetworkConnected(ActivityStart.this)){
            Glide.with(ActivityStart.this).load(imageUrl).into(imageView);
//            Toast.makeText(ActivityStart.this,"COnn",Toast.LENGTH_SHORT).show();
        }
        else {
            Glide.with(ActivityStart.this).load(R.mipmap.start_page).into(imageView);
//            Toast.makeText(ActivityStart.this,"UN",Toast.LENGTH_SHORT).show();
        }


//        getMainHtml(MAIN_URL);







        //开启线程让Activity停留2秒
        new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    ActivityStart.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent;
                            if (fisrtLoginBl)
                                intent = new Intent(ActivityStart.this, ActivityFirstLogin.class);
                            else intent = new Intent(ActivityStart.this, ActivityNews.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


//    private void getMainHtml(final String mainUrl) {
//        // TODO Auto-generated method stub
//        imageThread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                InputStream inputStream = null;
//                URL url = null;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    url = new URL(mainUrl);
//
//                    httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.setReadTimeout(5000);
//                    httpURLConnection.setReadTimeout(5000);
//
//                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//
//                        inputStream = httpURLConnection.getInputStream();
//
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//                        String line;
//                        StringBuilder response = new StringBuilder();
//
//                        while ((line = reader.readLine()) != null) {
//                            response.append(line);
//
//                        }
//                        // String newsHtml = response.toString();
//                        // System.out.println(response.toString());
//                        // getDataFromHtml(newsHtml);
//
////						System.out.println(response.toString());
//
//
//                        String secondUrl = getSubUrlFormHtml(response.toString());
//                        getHtmlFromSecondUrl(secondUrl);
//
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (httpURLConnection != null) {
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//
//
//        });
//
//        imageThread.start();
//    }
//
//    private void getHtmlFromSecondUrl(final String secondUrl){
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                InputStream inputStream = null;
//                URL url = null;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    url = new URL(secondUrl);
//
//                    httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.setReadTimeout(5000);
//                    httpURLConnection.setReadTimeout(5000);
//
//                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//
//                        inputStream = httpURLConnection.getInputStream();
//
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//                        String line;
//                        StringBuilder response = new StringBuilder();
//
//                        while ((line = reader.readLine()) != null) {
//                            response.append(line);
//
//                        }
//                        // String newsHtml = response.toString();
//                        // System.out.println(response.toString());
//                        // getDataFromHtml(newsHtml);
//
////						System.out.println(response.toString());
//
//
//                        String secondUrl = getSecondUrlFromHtml(response.toString());
////                        System.out.println(response.toString());
////                        System.out.println(secondUrl);
////						getSecondUrlFromHtml(secondUrl);
//
//                        getImageHtml(secondUrl);
//
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (httpURLConnection != null) {
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//
//        }).start();
//    }
//
//    private void getImageHtml(final String secondUrl) {
//        // TODO Auto-generated method stub
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                InputStream inputStream = null;
//                URL url = null;
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    url = new URL(secondUrl);
//
//                    httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.setReadTimeout(5000);
//                    httpURLConnection.setReadTimeout(5000);
//
//                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//
//                        inputStream = httpURLConnection.getInputStream();
//
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//                        String line;
//                        StringBuilder response = new StringBuilder();
//
//                        while ((line = reader.readLine()) != null) {
//                            response.append(line);
//
//                        }
//                        // String newsHtml = response.toString();
//                        // System.out.println(response.toString());
//                        // getDataFromHtml(newsHtml);
//
////						System.out.println(response.toString());
//
//                        System.out.println(response.toString());
//                        final String imageUrl = getImageUrlFromHtml(response.toString());
//
//                        ActivityStart.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                imageUrlStr=imageUrl;
//                            }
//                        });
//
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (httpURLConnection != null) {
//                        httpURLConnection.disconnect();
//                    }
//                }
//            }
//
//        }).start();
//    }
//
//    private String getImageUrlFromHtml(String html){
//        Document document = Jsoup.parse(html);
//        Element element = document.getElementById("bigImg");
//        String imageUrl = element.attr("src");
////        System.err.println(imageUrl);
//        String image1920=imageUrl.replace("320x510", "1080x1920");
////        System.err.println(image1920);
////        return imageUrl;
//        return image1920;
//    }
//
//    private String getSecondUrlFromHtml(String html){
//        Document document = Jsoup.parse(html);
//        Elements elements = document.getElementById("img1").getElementsByTag("a");
//
//
//        Random random = new Random();
//
//        int select = random.nextInt(elements.size())%(elements.size()-0+1)+0;
//
//
//        return "http://sj.zol.com.cn"+elements.get(select).attr("href");
//
//    }
//
//    private String getSubUrlFormHtml(String string) {
//        // TODO Auto-generated method stub
//        Document document = Jsoup.parse(string);
//        Elements elements = document.getElementsByClass("pic");
//        Random random = new Random();
//
//        int select = random.nextInt(elements.size())%(elements.size()-0+1)+0;
////		System.err.println(select);
//        return "http://sj.zol.com.cn"+elements.get(select).select("a").attr("href");
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (imageThread!=null){
//            imageThread.stop();
//        }
    }
}
