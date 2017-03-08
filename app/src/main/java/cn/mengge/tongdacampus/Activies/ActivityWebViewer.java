package cn.mengge.tongdacampus.Activies;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityWebViewer extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);
        initViews();
    }

    private void initViews() {

        Intent intent = getIntent();
        int titleId = intent.getIntExtra("CONTENT", 0);
        String subTitle = intent.getStringExtra("SubTitle");
        String link = intent.getStringExtra("LINK");

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);

        if (titleId == 0) {
            toolbar.setTitle("教务信息");
        }
        if (titleId == 1) {
            toolbar.setTitle("政策法规");
        }
        toolbar.setSubtitle(subTitle);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.web_viewer_vw);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(link);
    }

    //ToolBar上的返回按钮监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
