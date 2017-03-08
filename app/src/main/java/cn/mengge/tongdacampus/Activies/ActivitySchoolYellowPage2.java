package cn.mengge.tongdacampus.Activies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.utils.FinalStrings;


public class ActivitySchoolYellowPage2 extends AppCompatActivity {

    private final static String URL_SCHOOL_YP_SCHOOL_CAR_TIME_TB = "http://www.ntu.edu.cn/zzy/ggxx/content/29778a91-882d-4627-a026-21e2e0071372.html";
    private final static String URL_SCHOOL_YP_TIME_TABLE = "http://www.ntu.edu.cn/zzy/ggxx/content/96c22092-8f28-4156-b5db-11c752a082cd.html";
    private final static String URL_SCHOOL_YP_MAIN_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/5a83364a-0732-44d1-b2e8-1251f3fb7e77.html";
    private final static String URL_SCHOOL_YP_QI_XIU_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/8381f276-aeca-4979-91a6-5c5474ce9be2.html";
    private final static String URL_SCHOOL_YP_ZHONG_XIU_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/e9961d3d-ced0-4206-8c77-442dbf7b6f07.html";


    private WebView webView;
    private StringBuilder subString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_yellow_page2);

        webView = (WebView)findViewById(R.id.school_yp2_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(URL_STR);

        getHtmlFromURL(URL_SCHOOL_YP_MAIN_TEL_TABLE);

    }


    private void getHtmlFromURL(final String urlStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                URL url = null;
                try{
                    url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
                    connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
                    connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FinalStrings.NetStrings.NET_DATA_CHARSET_UTF_8));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        String html = response.toString();
                        if (html.length() != 0){
                            Document document = Jsoup.parse(html);
                            Element element = document.getElementById("zoom");
                            subString = new StringBuilder(element.toString());


                            ActivitySchoolYellowPage2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadDataWithBaseURL(null,subString.toString(),"text/html","utf-8", null);
                                    if (subString != null){

                                    }else {

                                    }
                                }
                            });

                        }
                    }




                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
