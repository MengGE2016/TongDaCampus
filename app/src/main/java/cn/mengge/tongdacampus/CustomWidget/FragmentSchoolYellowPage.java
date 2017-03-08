package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.mengge.tongdacampus.Activies.ActivitySchoolYellowPage;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.NetServer;

/**
 * Created by MengGE on 2016/10/16.
 */
public class FragmentSchoolYellowPage extends Fragment {

    private WebView webView;
//    private Bitmap bitmap;
    private String htmlStr;
    private View view;
    private String contentHtml;

    private ProgressBar progressBar;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what ==0){
                progressBar.setVisibility(View.GONE);
                webView.loadDataWithBaseURL(null,contentHtml,"text/html","utf-8", null);
            }
        }
    };
//    public void setBitmap(Bitmap bitmap) {
//        this.bitmap = bitmap;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.from(getActivity()).inflate(R.layout.fragment_school_yellow_page, null);
        progressBar = (ProgressBar)view.findViewById(R.id.fragment_school_yp_prob);


        webView = (WebView) view.findViewById(R.id.fragment_school_yp_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);


//        webView.loadDataWithBaseURL(null,htmlStr,"text/html","utf-8", null);
//        imageView.setImageBitmap(bitmap);

        getContentFromURL();
        return view;
    }


    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public String getHtmlStr() {
        return htmlStr;
    }

    public void setHtmlStr(String htmlStr) {
        this.htmlStr = htmlStr;
    }


    private void getContentFromURL(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String html = NetServer.getHtml(htmlStr);

                Document document = null;
                Element element = null;

                if (html.length()!=0){
                    document = Jsoup.parse(html);
                    element = document.getElementById("zoom");
                    contentHtml = element.toString();


                    Message message = new Message();
                    message.what =0 ;
                    handler.sendEmptyMessage(0);
                }
            }


        }).start();
    }


}


