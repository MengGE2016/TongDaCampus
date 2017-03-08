package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

import cn.mengge.tongdacampus.CustomWidget.FragmentSchoolYellowPage;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.DrawerServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivitySchoolYellowPage extends AppCompatActivity {

    private final static String URL_SCHOOL_YP_SCHOOL_CAR_TIME_TB = "http://www.ntu.edu.cn/zzy/ggxx/content/29778a91-882d-4627-a026-21e2e0071372.html";
    private final static String URL_SCHOOL_YP_TIME_TABLE = "http://www.ntu.edu.cn/zzy/ggxx/content/96c22092-8f28-4156-b5db-11c752a082cd.html";
    private final static String URL_SCHOOL_YP_MAIN_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/5a83364a-0732-44d1-b2e8-1251f3fb7e77.html";
    private final static String URL_SCHOOL_YP_QI_XIU_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/8381f276-aeca-4979-91a6-5c5474ce9be2.html";
    private final static String URL_SCHOOL_YP_ZHONG_XIU_TEL_TABLE = "http://www.ntu.edu.cn/zzy/dhcx/content/e9961d3d-ced0-4206-8c77-442dbf7b6f07.html";

    private StringBuilder timeTableHtmlStrBud;
    private StringBuilder schoolCarTimeTbStrBud;
    private StringBuilder mainTelTbStrBud;
    private StringBuilder qiXiuTelTbStrBud;
    private StringBuilder zhongXiuTelTbStrBud;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final static String[] tabTitle = {"教学作息时间表", "校车班次表", "主校区电话表", "启秀校区电话表", "钟秀校区电话表"};

//    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_yellow_page);
        initViews();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        progressDialog = new ProgressDialog(ActivitySchoolYellowPage.this);
//        progressDialog.setMessage("正在获取内容......");
//        progressDialog.show();

//        final Bitmap restTimeBm = DrawerServer.getBitmapFromResourceId(ActivitySchoolYellowPage.this, R.mipmap.school_yp_rest_time_table);
//        final Bitmap carTimeBm = DrawerServer.getBitmapFromResourceId(ActivitySchoolYellowPage.this, R.mipmap.school_yp_car_time_table);
//        final Bitmap mainSchoolPhoneBm = DrawerServer.getBitmapFromResourceId(ActivitySchoolYellowPage.this, R.mipmap.school_yp_main_school_phone);
//        final Bitmap qiXiuSchoolPhoneBm = DrawerServer.getBitmapFromResourceId(ActivitySchoolYellowPage.this, R.mipmap.school_yp_qixiu_school_phone);
//        final Bitmap zhongXiuSchoolPhoneBm = DrawerServer.getBitmapFromResourceId(ActivitySchoolYellowPage.this, R.mipmap.school_yp_zhongxiu_school_phone);

        //TabLayout与Viewpager
        tabLayout = (TabLayout) findViewById(R.id.school_yellow_page_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.school_yello_page_view_pager);

//        getContentFromURL();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                FragmentSchoolYellowPage restTimeFragment = new FragmentSchoolYellowPage();
//                                restTimeFragment.setBitmap(restTimeBm);
                restTimeFragment.setHtmlStr(URL_SCHOOL_YP_TIME_TABLE);

                FragmentSchoolYellowPage carTimeFragment = new FragmentSchoolYellowPage();
//                                carTimeFragment.setBitmap(carTimeBm);
                carTimeFragment.setHtmlStr(URL_SCHOOL_YP_SCHOOL_CAR_TIME_TB);

                FragmentSchoolYellowPage mainSchoolPhoneFragment = new FragmentSchoolYellowPage();
//                                mainSchoolPhoneFragment.setBitmap(mainSchoolPhoneBm);
                mainSchoolPhoneFragment.setHtmlStr(URL_SCHOOL_YP_MAIN_TEL_TABLE);


                FragmentSchoolYellowPage qiXiuPhoneFragment = new FragmentSchoolYellowPage();
//                                qiXiuPhoneFragment.setBitmap(qiXiuSchoolPhoneBm);
                qiXiuPhoneFragment.setHtmlStr(URL_SCHOOL_YP_QI_XIU_TEL_TABLE);

                FragmentSchoolYellowPage zhongXiuPhoneFragment = new FragmentSchoolYellowPage();
//                                zhongXiuPhoneFragment.setBitmap(zhongXiuSchoolPhoneBm);
                zhongXiuPhoneFragment.setHtmlStr(URL_SCHOOL_YP_ZHONG_XIU_TEL_TABLE);

                if (position == 1) return carTimeFragment;
                if (position == 2) return mainSchoolPhoneFragment;
                if (position == 3) return qiXiuPhoneFragment;
                if (position == 4) return zhongXiuPhoneFragment;
                return restTimeFragment;
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

//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                FragmentSchoolYellowPage restTimeFragment = new FragmentSchoolYellowPage();
//                restTimeFragment.setBitmap(restTimeBm);
//
//                FragmentSchoolYellowPage carTimeFragment = new FragmentSchoolYellowPage();
//                carTimeFragment.setBitmap(carTimeBm);
//
//                FragmentSchoolYellowPage mainSchoolPhoneFragment = new FragmentSchoolYellowPage();
//                mainSchoolPhoneFragment.setBitmap(mainSchoolPhoneBm);
//
//
//                FragmentSchoolYellowPage qiXiuPhoneFragment = new FragmentSchoolYellowPage();
//                qiXiuPhoneFragment.setBitmap(qiXiuSchoolPhoneBm);
//
//                FragmentSchoolYellowPage zhongXiuPhoneFragment = new FragmentSchoolYellowPage();
//                zhongXiuPhoneFragment.setBitmap(zhongXiuSchoolPhoneBm);
//
//                if (position == 1) return carTimeFragment;
//                if (position == 2) return mainSchoolPhoneFragment;
//                if (position == 3) return qiXiuPhoneFragment;
//                if (position == 4) return zhongXiuPhoneFragment;
//                return restTimeFragment;
//            }
//
//            @Override
//            public int getCount() {
//                return tabTitle.length;
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return tabTitle[position];
//            }
//        });

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



//    private void getContentFromURL(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                String timeTableHtmlStr = NetServer.getHtml(URL_SCHOOL_YP_TIME_TABLE);
//                String schoolCarTimeTbStr = NetServer.getHtml(URL_SCHOOL_YP_SCHOOL_CAR_TIME_TB);
//                String mainTelTbStr = NetServer.getHtml(URL_SCHOOL_YP_MAIN_TEL_TABLE);
//                String qiXiuTelTbStr = NetServer.getHtml(URL_SCHOOL_YP_QI_XIU_TEL_TABLE);
//                String zhongXiuTelTbStr = NetServer.getHtml(URL_SCHOOL_YP_ZHONG_XIU_TEL_TABLE);
//                Document document = null;
//                Element element = null;
//
//                if (timeTableHtmlStr.length()!=0){
//                    document = Jsoup.parse(timeTableHtmlStr);
//                    element = document.getElementById("zoom");
//                    timeTableHtmlStrBud = new StringBuilder(element.toString());
//                }
//                if (schoolCarTimeTbStr.length()!=0){
//                    document = Jsoup.parse(schoolCarTimeTbStr);
//                    element = document.getElementById("zoom");
//                    schoolCarTimeTbStrBud = new StringBuilder(element.toString());
//                }
//                if (mainTelTbStr.length()!=0){
//                    document = Jsoup.parse(mainTelTbStr);
//                    element = document.getElementById("zoom");
//                    mainTelTbStrBud = new StringBuilder(element.toString());
//                }
//                if (qiXiuTelTbStr.length()!=0){
//                    document = Jsoup.parse(qiXiuTelTbStr);
//                    element = document.getElementById("zoom");
//                    qiXiuTelTbStrBud = new StringBuilder(element.toString());
//                }
//                if (zhongXiuTelTbStr.length()!=0){
//                    document = Jsoup.parse(zhongXiuTelTbStr);
//                    element = document.getElementById("zoom");
//                    zhongXiuTelTbStrBud= new StringBuilder(element.toString());
//                }
//
//                ActivitySchoolYellowPage.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//                            @Override
//                            public Fragment getItem(int position) {
//                                FragmentSchoolYellowPage restTimeFragment = new FragmentSchoolYellowPage();
////                                restTimeFragment.setBitmap(restTimeBm);
//                                restTimeFragment.setHtmlStr(timeTableHtmlStrBud.toString());
//
//                                FragmentSchoolYellowPage carTimeFragment = new FragmentSchoolYellowPage();
////                                carTimeFragment.setBitmap(carTimeBm);
//                                carTimeFragment.setHtmlStr(schoolCarTimeTbStrBud.toString());
//
//                                FragmentSchoolYellowPage mainSchoolPhoneFragment = new FragmentSchoolYellowPage();
////                                mainSchoolPhoneFragment.setBitmap(mainSchoolPhoneBm);
//                                mainSchoolPhoneFragment.setHtmlStr(mainTelTbStrBud.toString());
//
//
//                                FragmentSchoolYellowPage qiXiuPhoneFragment = new FragmentSchoolYellowPage();
////                                qiXiuPhoneFragment.setBitmap(qiXiuSchoolPhoneBm);
//                                qiXiuPhoneFragment.setHtmlStr(qiXiuTelTbStrBud.toString());
//
//                                FragmentSchoolYellowPage zhongXiuPhoneFragment = new FragmentSchoolYellowPage();
////                                zhongXiuPhoneFragment.setBitmap(zhongXiuSchoolPhoneBm);
//                                zhongXiuPhoneFragment.setHtmlStr(zhongXiuTelTbStrBud.toString());
//
//                                if (position == 1) return carTimeFragment;
//                                if (position == 2) return mainSchoolPhoneFragment;
//                                if (position == 3) return qiXiuPhoneFragment;
//                                if (position == 4) return zhongXiuPhoneFragment;
//                                return restTimeFragment;
//                            }
//
//                            @Override
//                            public int getCount() {
//                                return tabTitle.length;
//                            }
//
//                            @Override
//                            public CharSequence getPageTitle(int position) {
//                                return tabTitle[position];
//                            }
//                        });
//
//                        progressDialog.dismiss();
//
//                    }
//                });
//
//            }
//
//
//        }).start();
//    }

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
