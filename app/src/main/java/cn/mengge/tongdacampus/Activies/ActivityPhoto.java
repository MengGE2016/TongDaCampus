package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.RecyclerViewAdapterPhtotsContent;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataUriVideo;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityPhoto extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterPhtotsContent adapterPhtotsContent;
    private List<DataUriVideo> list;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityPhoto.this,2);

    private ProgressDialog progressDialog;

    private final static String PHOTO_URL = "http://news.ntu.edu.cn/xww/gytd/gytd.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ActivityPhoto.this);
        progressDialog.setMessage("正在获取内容......");


        recyclerView = (RecyclerView)findViewById(R.id.pthotos_act_recy);
        list = new ArrayList<>();
//        adapterPhtotsContent = new RecyclerViewAdapterPhtotsContent(list);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(adapterPhtotsContent);

        getPhotos();

    }

    private void getPhotos(){
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String html = NetServer.getHtml(PHOTO_URL);
                ActivityPhoto.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (html != null){
                            list = getAllPhotoDataFormHtml(html);
                            adapterPhtotsContent = new RecyclerViewAdapterPhtotsContent(list);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(adapterPhtotsContent);
//                            adapterPhtotsContent.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }).start();
    }

    private List<DataUriVideo> getAllPhotoDataFormHtml(String html){
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
