package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.RecyclerViewAdapterVideoContent;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.CustomWidget.AdapterVideoContent;
import cn.mengge.tongdacampus.classes.DataUriVideo;
import cn.mengge.tongdacampus.server.DrawerServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivitySchoolVideo extends AppCompatActivity {

    private List<DataUriVideo> list = new ArrayList<>();
    private RecyclerViewAdapterVideoContent adapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager = new GridLayoutManager(ActivitySchoolVideo.this,2);

    private final static String URL = "http://news.ntu.edu.cn/xww/tdsp/tdsp.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_video);

        initViews();
        getAllVideosData(URL);
    }

    //初始化控件
    private void initViews() {

        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ListView listView = (ListView) findViewById(R.id.school_video_content_lv);
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.zhaosheng2016), "2016南通大学招生宣传片", "http://210.29.65.46:8001//video/ca882d11-df7b-4b23-a2ad-00b75cd20ce7.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.find1895), "央视纪录《探索发现》——寻找1895", "http://210.29.65.46:8001//video/cf29f7f5-4122-4dfb-86fe-9456fea3c978.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.happynewyear), "最美通大之我与通大一起跨年", "http://210.29.65.46:8001//video/e9b35075-8ed3-4c30-8fb4-d316f6a22c18.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.mingji_chuancheng), "铭记.传承", "http://210.29.65.46:8001//video/9bb9d823-0a12-42e1-865b-b8b286bc231a.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.best_ntu), "最美通大", "http://210.29.65.46:8001//video/9fc0f0d9-bc26-49c3-8f36-d84565290442.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.shuiyumo), "最美通大之挥彩泼墨绘人生", "http://210.29.65.46:8001//video/877a9205-0e80-4e29-b572-aa15ca1ce295.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.plane), "最美通大之苍穹战鹰", "http://210.29.65.46:8001//video/3e1aacde-d9f5-4bf4-a37f-a14e6ba278d3.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.model), "最美通大之巧设精制秀人才", "http://210.29.65.46:8001//video/6a74a954-a304-4c38-bf16-8d5425f2b5ce.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.mother_iloveyou), "最美通大之妈妈我爱您", "http://210.29.65.46:8001//video/aafab9d3-0b3f-4eed-8792-1ccdeaac6294.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.chuangke), "最美通大之通大创客", "http://210.29.65.46:8001//video/95e4e025-75ac-4ce9-8bcb-23d618c6f396.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.jianke), "最美通大之通大剑客", "http://210.29.65.46:8001//video/d23f1f19-a09a-499d-90df-a15e0a9c56eb.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.zhiyuanzhe), "最美通大之我身边的志愿者", "http://210.29.65.46:8001//video/a44b55b3-0b61-4bb1-9bc5-828187214977.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.jiqiren), "最美通大之机器人创新团队", "http://210.29.65.46:8001//video/4104fed2-1165-410a-a0f7-f2b5ac07e602.mp4"));
//        list.add(new DataUriVideo(DrawerServer.getBitmapFromResourceId(ActivitySchoolVideo.this, R.mipmap.chushuilian), "出水莲", "http://210.29.65.46:8001//video/c4b8158c-a7f4-4194-84df-eb95281d6ce7.mp4"));
//        AdapterVideoContent adapter = new AdapterVideoContent(this, R.layout.custom_list_view_school_video, list);
        progressDialog = new ProgressDialog(ActivitySchoolVideo.this);
        progressDialog.setMessage("正在获取视频......");


        recyclerView = (RecyclerView)findViewById(R.id.school_video_content_lv);

        recyclerView.setLayoutManager(layoutManager);


//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(ActivitySchoolVideo.this, ActivitySchoolVideoShow.class);
//                intent.putExtra("title", list.get(position).getTitleStr());
//                intent.putExtra("URI", list.get(position).getUriStr());
//                startActivity(intent);
//            }
//        });

    }

    private String getAllVideosData(final String urlStr){
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
                        list = getAllVideosDataFormHtml(response.toString());
//                        adapter.notifyDataSetChanged();
                        ActivitySchoolVideo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (list.size()!=0){
                                    adapter = new RecyclerViewAdapterVideoContent(list);
                                    recyclerView.setAdapter(adapter);
                                    progressDialog.dismiss();
                                }

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
        return "";
    }

    private List<DataUriVideo> getAllVideosDataFormHtml(String html){
        List<DataUriVideo> videosList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("tw-col-3").get(0).select("li");
        for(int i=0; i<elements.size(); i++){
            videosList.add(new DataUriVideo(elements.get(i).text(),elements.get(i).select("img").attr("src"), elements.get(i).select("a").attr("href")));
        }
        return videosList;
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
