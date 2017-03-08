package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.VideoView;
import android.widget.MediaController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.mengge.tongdacampus.R;

public class ActivitySchoolVideoShow extends AppCompatActivity {
    private VideoView videoView;
    private MediaController controller;
    private ProgressDialog progressDialog;
    private boolean isBegin = false;
    private ActionBar actionBar;

    private String mp4Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_school_video_show);
        Intent intent = getIntent();
        String uriStr = intent.getStringExtra("URI");
        String titleStr = intent.getStringExtra("title");

        actionBar = getSupportActionBar();
        actionBar.setTitle(titleStr);
        actionBar.setHomeButtonEnabled(true); //设置返回键可用
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("视频正在加载,请稍候......");

        actionBar.show();

        videoView = (VideoView) findViewById(R.id.school_video_show_vv);
        controller = new MediaController(this);

        //从网页中获取视频的地址http://xxxxx.mp4
        getMp4Url(uriStr);
        //视频初始化完毕
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                actionBar.hide();
                videoView.start();
                isBegin = true;
            }
        });

        //视频播放结束退出Activity
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
//                actionBar.show();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivitySchoolVideoShow.this);
//                dialog.setMessage("视频播放结束,是否退出?")
//                        .setPositiveButton("是",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        finish();
//                                    }
//                                })
//                        .setNegativeButton("否",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialogInterface,
//                                            int which) {
//                                        dialogInterface.dismiss();
//                                    }
//                                });
//                dialog.show();
                finish();
            }
        });

        //Android版本5.0以上才可以使用此方法

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (isBegin && what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        actionBar.show();
                        progressDialog.setMessage("正在缓冲......");
                        progressDialog.show();
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        progressDialog.dismiss();
                        actionBar.hide();
                        isBegin = true;
                    }
                    return false;
                }
            });
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);
        videoView.requestFocus();
    }

    private void getMp4Url(final String urlStr){
        progressDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream inputStream = null;
                URL url = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(urlStr);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);

                        }

                        mp4Url = getMp4UrlFormHtml(response.toString());
                        ActivitySchoolVideoShow.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                videoView.setVideoURI(Uri.parse(mp4Url));
                            }
                        });

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    protected String getMp4UrlFormHtml(String htmlStr) {
        Document document = Jsoup.parse(htmlStr);
        Element element = document.getElementById("zoom");
        return element.select("embed").attr("src").toString();
    }


    //Toolbar返回键监听--退出activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
