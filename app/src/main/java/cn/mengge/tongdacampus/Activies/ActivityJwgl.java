package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.CustomDialogImageEditextActivity;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.CustomWidget.FragmentJwglListView;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;
import cn.mengge.tongdacampus.classes.DataNewsLinkTitle;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityJwgl extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView functionLv;

    private Intent intent;
    private ProgressDialog progressDialog;

    private List<Data3Strings1IntTLTF> jwxxList;
    private List<Data3Strings1IntTLTF> jwgdList;
    private String[] functionStrs = {"我的成绩", "全校课表查询", "他人成绩查询", "空教室查询"};
    private String[] tabTitle = {"教务信息", "政策法规"};

    private SharedPreferences preferences;
    private Boolean fisrtLoginBl;

    private int drawerLayoutFunctionId;
    private final static int NOTHING = 0;
    private final static int MY_GRADE = 1;
    private final static int SCHOOL_CLASS_TABLE_QUERY = 2;
    private final static int OTHERS_GRADE_QUERY = 3;
    private final static int EMPTY_CLASSROOM_QUERY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwgl);

        initViews();
        getJwxxAndJwgdList();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        fisrtLoginBl = preferences.getBoolean(FinalStrings.PrefServerStrings.STUDENT_FIRST_LOGIN, true);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.custom_drawer_layout_jwgl_id);

        tabLayout = (TabLayout) findViewById(R.id.jwgl_message_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.jwgl_message_view_pager);

        functionLv = (ListView) findViewById(R.id.jwgl_function_lv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(FinalStrings.CommonStrings.GET_CONTENT_ING);

        functionLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, functionStrs));
        functionLv.setOnItemClickListener(new FunctionLvOnItemtClickLinstener());
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
                    case MY_GRADE:
                        drawerLayoutFunctionId = NOTHING;
                        intent = new Intent(ActivityJwgl.this, ActivityGradeQuery.class);
                        startActivity(intent);
                        break;
                    case SCHOOL_CLASS_TABLE_QUERY:
                        drawerLayoutFunctionId = NOTHING;
                        intent = new Intent(ActivityJwgl.this, CustomDialogImageEditextActivity.class);
                        intent.putExtra("function", 0); //0表示课表查询
                        startActivity(intent);
                        break;
                    case OTHERS_GRADE_QUERY:
                        drawerLayoutFunctionId = NOTHING;
                        intent = new Intent(ActivityJwgl.this, ActivityLoginJwgl.class);
                        startActivity(intent);
                        break;
                    case EMPTY_CLASSROOM_QUERY:
                        drawerLayoutFunctionId = NOTHING;
                        intent = new Intent(ActivityJwgl.this, CustomDialogImageEditextActivity.class);
                        intent.putExtra("function", 1); //1表示空教室查询
                        startActivity(intent);
                        break;
                }
            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    //获取教务信息和教务规定的List
    private void getJwxxAndJwgdList() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String html = "";
                for (int i = 0; i <= 60; i += 20) {
                    html += NetServer.getHtml(FinalStrings.NetStrings.URL_JWXX + "?s=" + i + "&t=1");
                }

                final String jwxxHtml = html;
                final String jwgdHtml = NetServer.getHtml(FinalStrings.NetStrings.URL_JWGD);

                ActivityJwgl.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (jwxxHtml.length() != 0 && jwgdHtml.length() != 0) {
                            jwgdList = StringServer.getJwxxJwgdData(jwgdHtml);
                            jwxxList = StringServer.getJwxxJwgdData(jwxxHtml);

                            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                                @Override
                                public Fragment getItem(int position) {
                                    FragmentJwglListView jwglJwxxFragment = new FragmentJwglListView();
                                    jwglJwxxFragment.setNewsLinkTitles(jwxxList);
                                    jwglJwxxFragment.setContext(ActivityJwgl.this);

                                    FragmentJwglListView jwglJwgdFragment = new FragmentJwglListView();
                                    jwglJwgdFragment.setNewsLinkTitles(jwgdList);
                                    jwglJwgdFragment.setContext(ActivityJwgl.this);
                                    if (position == 1) return jwglJwgdFragment;
                                    return jwglJwxxFragment;
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
                            Toast.makeText(ActivityJwgl.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //ListView监听事件
    private class FunctionLvOnItemtClickLinstener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (fisrtLoginBl){
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityJwgl.this);
                dialog.setTitle("温馨提示：").setMessage("本功能暂时不对非本校学生开放！\n如果你是本校学生，请输入登录信息即可使用此功能！").setCancelable(false).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("我要去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ActivityJwgl.this,ActivityFirstLogin.class);
                        startActivity(intent);
                        ActivityJwgl.this.finish();
//                        System.exit(0);
                    }
                }).show();
            }
            else {
                switch (position) {
                    //我的成绩查询
                    case 0:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayoutFunctionId = MY_GRADE;
                        break;
                    //学校课表查询
                    case 1:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayoutFunctionId = SCHOOL_CLASS_TABLE_QUERY;
                        break;
                    //他人成绩查询
                    case 2:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayoutFunctionId = OTHERS_GRADE_QUERY;
                        break;
                    //空教室查询
                    case 3:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayoutFunctionId = EMPTY_CLASSROOM_QUERY;
                        break;
                }
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

    //重启应用
    private void restartApplication(){
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //按返回按钮监听事件--先关闭抽屉
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

//    @Override
//    protected void onDestroy() {
////        super.onDestroy();
//        restartApplication();
//    }
}
