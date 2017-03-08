package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.FragmentMoreNews;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityMoreNews extends AppCompatActivity {
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FragmentMoreNews tdywFMN;
    private FragmentMoreNews xwdtFMN;
    private FragmentMoreNews xyshFMN;
    private FragmentMoreNews ztbdFMN;
    private FragmentMoreNews mttdFMN;

    private List<Data3Strings1IntTLTF> tdywList;
    private List<Data3Strings1IntTLTF> xwdtList;
    private List<Data3Strings1IntTLTF> xyshList;
    private List<Data3Strings1IntTLTF> ztbdList;
    private List<Data3Strings1IntTLTF> mttdList;

    private String[] tabTitle = {"通大要闻", "新闻动态", "校园生活", "专题报道", "媒体通大"};

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_news);
        initViews();
        getNewsData();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ActivityMoreNews.this);
        progressDialog.setMessage("正在获取更多新闻......");


        tabLayout = (TabLayout) findViewById(R.id.more_news_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.more_news_view_pager);
    }

    private void getNewsData() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String tdywHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_TDYW);
                final String xwdtHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_XWDT);
                final String xyshHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_XYSH);
                final String ztbdHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_ZTBD);
                final String mttdHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_MTTD);
                ActivityMoreNews.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tdywHtmlStr.length() == 0 || xwdtHtmlStr.length() == 0 || xyshHtmlStr.length() == 0 || ztbdHtmlStr.length() == 0 || mttdHtmlStr.length() == 0) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityMoreNews.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        } else {
                            tdywList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_TDYW, tdywHtmlStr);
                            xwdtList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_XWDT, xwdtHtmlStr);
                            xyshList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_XYSH, xyshHtmlStr);
                            ztbdList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_ZTBD, ztbdHtmlStr);
                            mttdList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_MTTD, mttdHtmlStr);

                            progressDialog.dismiss();

                            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                                @Override
                                public Fragment getItem(int position) {
                                    tdywFMN = new FragmentMoreNews();
                                    tdywFMN.setList(tdywList);

                                    xwdtFMN = new FragmentMoreNews();
                                    xwdtFMN.setList(xwdtList);

                                    xyshFMN = new FragmentMoreNews();
                                    xyshFMN.setList(xyshList);

                                    ztbdFMN = new FragmentMoreNews();
                                    ztbdFMN.setList(ztbdList);

                                    mttdFMN = new FragmentMoreNews();
                                    mttdFMN.setList(mttdList);

                                    if (position == 1) return xwdtFMN;
                                    if (position == 2) return xyshFMN;
                                    if (position == 3) return ztbdFMN;
                                    if (position == 4) return mttdFMN;
                                    return tdywFMN;
                                }

                                @Override
                                public int getCount() {
                                    return tabTitle.length;
                                }

                                @Override
                                public CharSequence getPageTitle(int position) {
                                    return tabTitle[position];
                                }
                            });
                            tabLayout.setupWithViewPager(viewPager);

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    if (tab == tabLayout.getTabAt(0)) viewPager.setCurrentItem(0);
                                    if (tab == tabLayout.getTabAt(1)) viewPager.setCurrentItem(1);
                                    if (tab == tabLayout.getTabAt(2)) viewPager.setCurrentItem(2);
                                    if (tab == tabLayout.getTabAt(3)) viewPager.setCurrentItem(3);
                                    if (tab == tabLayout.getTabAt(4)) viewPager.setCurrentItem(4);
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                }
                            });


                        }
                    }
                });


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
