package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataZbxx;
import cn.mengge.tongdacampus.CustomWidget.FragmentZbxx;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityZBXX extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView functionLv;

    private FragmentZbxx zbxxFragment;
    private FragmentZbxx zbggFragment;
    private ProgressDialog progressDialog;

    private List<DataZbxx> zbxxList;
    private List<DataZbxx> zbggList;
    private String[] functionStrs = {"部门简介", "联系我们"};
    private String[] tabTitle = {"招标信息", "中标公示"};

    private int drawerLayoutFunctionId;
    private static final int NOTHING = 0;
    private static final int PARTMENT_INTRODUCE = 1;
    private static final int CONNECT_US = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbxx);
        initViews();
        getZbxxData();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(FinalStrings.CommonStrings.REFRESHING);

        drawerLayout = (DrawerLayout) findViewById(R.id.custom_drawer_layout_zbxx_id);
        tabLayout = (TabLayout) findViewById(R.id.zbxx_message_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.zbxx_message_view_pager);
        //抽屉中的ListView
        functionLv = (ListView) findViewById(R.id.zbxx_function_lv);
        functionLv.setOnItemClickListener(new functionLvOnItemtClickLinstener());
        functionLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, functionStrs));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (drawerLayoutFunctionId) {
                    case NOTHING:
                        break;
                    //弹出部门简介对话框
                    case PARTMENT_INTRODUCE:
                        drawerLayoutFunctionId = NOTHING;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityZBXX.this);
                        dialog.setTitle("部门简介").setMessage(R.string.zbxx_partment_jianjie_text).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                        break;
                    //弹出联系我们对话框
                    case CONNECT_US:
                        drawerLayoutFunctionId = NOTHING;
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(ActivityZBXX.this);
                        dialog1.setTitle("联系我们").setMessage(R.string.zbxx_partment_address_text).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                        break;
                }
            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    //获取招标信息的标题,网址和发布时间
    private void getZbxxData() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String zbxxHtml = NetServer.getHtml(FinalStrings.NetStrings.URL_ZBXX);
                final String zbggHtml = NetServer.getHtml(FinalStrings.NetStrings.URL_ZBGG);

                ActivityZBXX.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (zbxxHtml.length() != 0 && zbggHtml.length() != 0) {
                            zbxxList = StringServer.getZbxxDataList(zbxxHtml, FinalStrings.NetStrings.FLAG_ZBXX);
                            zbggList = StringServer.getZbxxDataList(zbggHtml, FinalStrings.NetStrings.FLAG_ZBGG);
                            progressDialog.dismiss();
                            //设置ViewPager的适配器装载Fragment
                            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                                @Override
                                public Fragment getItem(int position) {
                                    zbxxFragment = new FragmentZbxx();

                                    zbxxFragment.setLists(zbxxList);
                                    zbxxFragment.setContext(ActivityZBXX.this);

                                    zbggFragment = new FragmentZbxx();
                                    zbggFragment.setLists(zbggList);
                                    zbggFragment.setContext(ActivityZBXX.this);
                                    if (position == 1) return zbggFragment;
                                    return zbxxFragment;
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
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                }
                            });

                        } else
                            Toast.makeText(ActivityZBXX.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //侧滑抽屉中的ListView的监听事件
    private class functionLvOnItemtClickLinstener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            drawerLayout.closeDrawer(Gravity.START); //关闭抽屉
            switch (position) {
                //弹出部门简介对话框
                case 0:
                    drawerLayoutFunctionId = PARTMENT_INTRODUCE;
                    break;
                //弹出联系我们对话框
                case 1:
                    drawerLayoutFunctionId = CONNECT_US;
                    break;
            }
        }
    }

    //手机按键监听--监听菜单键实现抽屉的打开与关闭
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onKeyDown(keyCode, event);
    }

    //按返回按钮监听事件--先关闭抽屉
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

}
