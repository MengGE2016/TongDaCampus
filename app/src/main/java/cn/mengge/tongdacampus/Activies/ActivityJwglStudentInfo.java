package cn.mengge.tongdacampus.Activies;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.utils.FinalStrings;


public class ActivityJwglStudentInfo extends AppCompatActivity implements View.OnClickListener {

    private TextView studentInfoTv;
    private Button gradeQueryBt;
    private Toolbar toolbar;
    private String COOKIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        COOKIE = getIntent().getStringExtra("COOKIE");
        init();
        getHtmlContent();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentInfoTv = (TextView) findViewById(R.id.main_student_info_tv);
        gradeQueryBt = (Button) findViewById(R.id.main_student_grade_query_bt);

        gradeQueryBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_student_grade_query_bt:
                Intent gradeIntent = new Intent(ActivityJwglStudentInfo.this, ActivityGradeQuery.class);
                gradeIntent.putExtra("COOKIE",COOKIE);
                startActivity(gradeIntent);
                break;
        }
    }

    private String getStudentInfo(String html) {
        String studentInfoStr = " ";
        Document document = Jsoup.parse(html);

        org.jsoup.nodes.Element element = document.getElementById("stuInfo");
        char[] stuInfoNotEnter = element.text().substring(4).toCharArray();

        for (int i = 1; i < stuInfoNotEnter.length; i++) {

            if (stuInfoNotEnter[i] == ' ') {
                studentInfoStr += "\n";
            }
            studentInfoStr += stuInfoNotEnter[i];
        }
        return studentInfoStr;
    }

    private void getHtmlContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream inputStream = null;
                HttpURLConnection connection = null;
                try {
                    url = new URL(FinalStrings.NetStrings.URL_STUDENT_MAIN);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
                    connection.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                    connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/");
                    connection.setRequestProperty("Cookie", COOKIE);
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                    connection.setRequestProperty("Cache-Control", "max-age=0");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) response.append(line);
                        final String studentInfoStr = getStudentInfo(response.toString());
                        ActivityJwglStudentInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                studentInfoTv.setText(studentInfoStr);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
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
