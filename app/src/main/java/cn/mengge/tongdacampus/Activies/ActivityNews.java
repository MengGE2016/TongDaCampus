package cn.mengge.tongdacampus.Activies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.CustomWidget.RecycleViewAdapterMainNavBroadcast;
import cn.mengge.tongdacampus.CustomWidget.RecycleViewAdapterMainNavNews;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.CustomWidget.AdapterNewsDrawerClasses;
import cn.mengge.tongdacampus.classes.DataClassTableClassInfo;
import cn.mengge.tongdacampus.classes.DataNewsDrawerTodayClassTable;
import cn.mengge.tongdacampus.classes.DataNewsLinkTitle;
import cn.mengge.tongdacampus.server.DrawerServer;
import cn.mengge.tongdacampus.server.FileServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityNews extends AppCompatActivity {

    //主界面控件
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private ImageView imageView;
//    private View dot;
//    private LayoutParams params;
//    private LinearLayout dotGroupLl;
//    private Button schoolJwglBtn;
//    private Button schoolVideoBtn;
//    private Button schoolZbxxBtn;
//    private Button schoolYellowPageBtn;
//    private Button moreNewsBtn;
//    private TextView moreNewsTv;
//    private SpannableString moreNewsStrSpanned;
//    private ListView newsListView;
    private ProgressDialog progressDialog;


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SQLServer sqlServer;


//    private RecyclerView broadcastRecv;
//    private RecycleViewAdapterMainNavBroadcast broadcastAdapter;
//    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityNews.this);
//
//


    private RecyclerView recyclerView;
    private List<DataNewsLinkTitle> newsList = new ArrayList<>();
    private RecycleViewAdapterMainNavNews newsAdapter;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityNews.this,1);
//    private StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

//    private SwipeRefreshLayout swipeRefreshLayout;


    //主界面变量
    private FileServer fileServer; //用于创建文件夹
    private List<ImageView> imageViewContainer;
    private boolean isStop = false;
    private long scrollTimeOffset = 5000;
    private long exitTime = 0;
//    private List<DataNewsLinkTitle> list;
//    private int preDotPosition = 0; //dot初始位置

//    private final String moreNewsStr = "更多>>";


    //抽屉界面控件
//    private Button ntuAbsBtn;
//    private TextView viewClassTableTv;
//    private SpannableString viewClassTableStrSpanned;
//    private Spinner weeksSp;
//    private String[] weeksStrs;
//    private ArrayAdapter<String> weeksSpAdpater;
//    private LinearLayout drawerTodayClassLl;
//    private ListView todayClassesLv;
//    private LinearLayout.LayoutParams todayClassLvLp;
//    private AdapterNewsDrawerClasses todayClassesAdapter;
//    private List<DataNewsDrawerTodayClassTable> todayClassesList;
//    private LinearLayout.LayoutParams noClassTvLp;
//    private TextView noClassTv;
//
//    private ListView settingsLv;
//    private ArrayAdapter<String> settingsAdapter;
//    private String[] settingsStrs;
//    private List<DataClassTableClassInfo> classList;


    //抽屉界面变量
//    private final String viewClassTableStr = "查看课程表>>";
//    private Calendar calendar;
//    private int week;
    private int drawerLayoutFunctionId;

    //抽屉关闭后执行的操作
    //什么也不做
    private final static int NOTHING = 0;
    //按下抽屉界面的图标后
//    private final static int ICON_BUTTON_PRESSED = 1;
    //关闭后打开课表界面
    private final static int START_ACTIVITY_CLASSTABLE = 2;
    //关闭后打开问题反馈界面
    private final static int START_ACTIVITY_REQUESTION = 3;
    //关闭后打开关于我们界面
    private final static int START_ACTIVITY_ABOUT_US = 4;
    //退出程序
    private final static int EXIT_PRO = 5;
    //关闭后打开教务管理界面
    private final static int START_ACTIVITY_JWGL = 6;
    //关闭后打开通大视频界面
    private final static int START_ACTIVITY_TDVIDEOS = 7;
    //关闭后打开光影通大界面
    private final static int START_ACTIVITY_PHOTOTD  = 8;
    //关闭后打开图书馆界面
//    private final static int START_ACTIVITY_LIBRARY = 9;
    //关闭后打开我的成绩界面
    private final static int START_ACTIVITY_MYGRADE = 10;
    //关闭后打开校园黄页界面
    private final static int START_ACTIVITY_YELLOPAGE = 11;
    //关闭后打开新闻界面
    private final static int START_ACTIVITY_MORENEWS = 12;
    //关闭后打开招标信息
    private final static int START_ACTIVITY_ZBXX = 13;


    private NavigationView navigationView;
    private TextView classNavTv;
    private TextView numberNavTv;
    private TextView nameNavTv;

    private String classNavStr;
    private String numberNavStr;
    private String nameNavStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initMainViews();
        initDrawerViews();
        createDir();
        progressDialog.show();
        getBannarImage();
        startBannerScrollThread();
        getNotification();
//        getNews(false);

    }

    /*初始化主界面控件并添加监听事件*/
    private void initMainViews() {

        //文件相关的操作
        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        editor = preferences.edit();
        NetServer netServer = new NetServer();
        netServer.getMainHtml(editor,FinalStrings.NetStrings.URL_STARTPAGE_IMAGE);

        classNavStr = preferences.getString("class_text"," ");
        numberNavStr = preferences.getString("student_num"," ");
        nameNavStr = preferences.getString("yuan_xi_text"," ");
        String sqlName = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_SQL_NAME, FinalStrings.PrefServerStrings.STUDENT_SQL_NAME_DEFAULT);
        sqlServer = new SQLServer(ActivityNews.this, sqlName, FinalStrings.SQLServerStrings.SQL_VERSION);

        applyLimit();

        fileServer = new FileServer(this);

        String sysVersion = getVersionName();
        String versionStr = preferences.getString("app_version","0.0.0");
        editor.putString("app_version",sysVersion);
        if (!versionStr.equals(sysVersion)){
            AlertDialog.Builder whatsNewDialog = new AlertDialog.Builder(ActivityNews.this);
            whatsNewDialog.setTitle("更新内容").setMessage(R.string.what_is_new).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(FinalStrings.CommonStrings.GET_CONTENT_ING);


        drawerLayout = (DrawerLayout) findViewById(R.id.custom_drawer_layout_new_s);
        viewPager = (ViewPager) findViewById(R.id.drawer_layout_relative_viewpager);
//        dotGroupLl = (LinearLayout) findViewById(R.id.drawer_layout_relative_linear_banner_dot);

        recyclerView = (RecyclerView)findViewById(R.id.main_nav_news_recv);
//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.main_nav_swipe_refresh);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getNews(true);
//            }
//        });
//        broadcastRecv = (RecyclerView)findViewById(R.id.main_nav_broadcast_recv);

//        schoolJwglBtn = (Button) findViewById(R.id.news_drawer_layout_main_school_jwgl_btn);
//        schoolVideoBtn = (Button) findViewById(R.id.news_drawer_layout_main_school_video_btn);
//        schoolZbxxBtn = (Button) findViewById(R.id.news_drawer_layout_main_school_zbxx_btn);
//        schoolYellowPageBtn = (Button) findViewById(R.id.news_drawer_layout_main_school_yellow_page_btn);
//        moreNewsBtn = (Button) findViewById(R.id.news_drawer_layout_main_more_news_btn);

//        moreNewsTv = (TextView) findViewById(R.id.news_drawer_layout_main_more_news_tv);
//        moreNewsStrSpanned = new SpannableString(moreNewsStr);
//        moreNewsStrSpanned.setSpan(new TextViewOnClickListener(), 0, moreNewsStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        moreNewsTv.setText(moreNewsStrSpanned);
//        moreNewsTv.setMovementMethod(LinkMovementMethod.getInstance());


//        list = new ArrayList<>();
//        newsListView = (ListView) findViewById(R.id.news_drawer_layout_main_school_news_lv);


//        newsListView.setOnItemClickListener(new ListViewOnItemClickListener());
//        schoolJwglBtn.setOnClickListener(new ButtonOnClickListener());
//        schoolVideoBtn.setOnClickListener(new ButtonOnClickListener());
//        schoolZbxxBtn.setOnClickListener(new ButtonOnClickListener());
//        schoolYellowPageBtn.setOnClickListener(new ButtonOnClickListener());
//        moreNewsBtn.setOnClickListener(new ButtonOnClickListener());

        imageViewContainer = new ArrayList<>();
//        Bitmap[] bitmaps = new Bitmap[]{
//               /* DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_a),*/
//               /* DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_b),*/
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_c),
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_d),
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_e),
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_f),
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_g),
//                DrawerServer.getBitmapFromResourceId(ActivityNews.this, R.drawable.news_banner_h),
//        };



//        for (Bitmap bitmap : bitmaps) {
//            imageView = new ImageView(ActivityNews.this);
//            imageView.setImageBitmap(bitmap);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageViewContainer.add(imageView);
//
//            // 每循环一次添加一个点到线行布局中
//            dot = new View(ActivityNews.this);
//            dot.setBackgroundResource(R.drawable.banner_dot_selector);
//            params = new LayoutParams(20, 20);
//            params.leftMargin = 10;
//            dot.setEnabled(false);
//            dot.setLayoutParams(params);
//            dotGroupLl.addView(dot); // 向线性布局中添加"点"
//        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerLayoutFunctionId != NOTHING){
                    Intent intent = null;
                    switch (drawerLayoutFunctionId) {
                        //只是关闭抽屉
//                        case NOTHING:
//                            break;
                        //图标按下的对话框
//                    case ICON_BUTTON_PRESSED:
//                        drawerLayoutFunctionId = NOTHING;
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityNews.this);
//                        dialog.setTitle("学校简介:").setMessage(R.string.nantong_university_abstrct).setPositiveButton(FinalStrings.CommonStrings.MAKE_SURE, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).show();
//                        break;
                        //打开教务管理界面
                        case START_ACTIVITY_JWGL:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityJwgl.class);
                            break;
                        //打开通大视频界面
                        case START_ACTIVITY_TDVIDEOS:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivitySchoolVideo.class);
                            break;
                        //打开光影通大界面
                        case START_ACTIVITY_PHOTOTD:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityPhoto.class);
                            break;
                        //打开图书馆界面
//                        case START_ACTIVITY_LIBRARY:
//                            drawerLayoutFunctionId = NOTHING;
//                            intent = new Intent(ActivityNews.this, ActivityLibrary.class);
//                            break;
                        //打开我的课表界面
                        case START_ACTIVITY_CLASSTABLE:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityClassTable.class);
                            break;
                        //打开我的成绩界面
                        case START_ACTIVITY_MYGRADE:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityGradeQuery.class);
                            break;

                        case START_ACTIVITY_ZBXX:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityZBXX.class);
                            break;

                        //打开校园黄页界面
                        case START_ACTIVITY_YELLOPAGE:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivitySchoolYellowPage.class);
                            break;


                        //打开问题反馈界面
                        case START_ACTIVITY_REQUESTION:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityReQuestion.class);
                            break;
                        //打开关于我们界面
                        case START_ACTIVITY_ABOUT_US:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityAboutUS.class);

                            break;
                        case START_ACTIVITY_MORENEWS:
                            drawerLayoutFunctionId = NOTHING;
                            intent = new Intent(ActivityNews.this, ActivityMoreNews.class);
                            break;

                        case EXIT_PRO:
                            drawerLayoutFunctionId = NOTHING;
                            editor.commit();
                            System.exit(FinalStrings.ApplicationStrings.PROGRAM_NORMAL_EXIT_CODE);

                    }

                    if (intent != null) startActivity(intent);
                }


            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

    }

    private void getBannarImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String html = NetServer.getHtml(FinalStrings.NetStrings.URL_OFFICIAL_URL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (html!=null){
                            String[] urls = StringServer.getURLformHtml(html);

                            for (int i=0; i<urls.length; i++) {
                                imageView = new ImageView(ActivityNews.this);
//                                imageView.setImageBitmap(bitmap);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(ActivityNews.this).load(urls[i]).into(imageView);
                                imageViewContainer.add(imageView);

                                // 每循环一次添加一个点到线行布局中
//                                dot = new View(ActivityNews.this);
//                                dot.setBackgroundResource(R.drawable.banner_dot_selector);
//                                params = new LayoutParams(20, 20);
//                                params.leftMargin = 10;
//                                dot.setEnabled(false);
//                                dot.setLayoutParams(params);
//                                dotGroupLl.addView(dot); // 向线性布局中添加"点"

                                viewPager.setAdapter(new BannerAdapter());
                                viewPager.addOnPageChangeListener(new BannerPageChangeListener());

                                // 选中第一个图片、文字描述
                                // tvBannerTextDesc.setText(bannerTextDescArray[0]);
//                                dotGroupLl.getChildAt(0).setEnabled(true);
                                viewPager.setCurrentItem(0);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    //检查申请权限
    private void applyLimit(){
//        if (ContextCompat.checkSelfPermission(ActivityNews.this, Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(ActivityNews.this, new String[]{Manifest.permission.INTERNET},1);
//        }
        if (ContextCompat.checkSelfPermission(ActivityNews.this, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ActivityNews.this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 1:
//                if (grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) Toast.makeText(ActivityNews.this,"You denied the internet permission",Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                if (grantResults.length>0 && grantResults[1] != PackageManager.PERMISSION_GRANTED) Toast.makeText(ActivityNews.this,"You denied the send sms permission",Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                if (grantResults.length>0 && grantResults[2] != PackageManager.PERMISSION_GRANTED) Toast.makeText(ActivityNews.this,"You denied the read external permission",Toast.LENGTH_SHORT).show();
//                break;
//            case 4:
//                if (grantResults.length>0 && grantResults[3] != PackageManager.PERMISSION_GRANTED) Toast.makeText(ActivityNews.this,"You denied the write external permission",Toast.LENGTH_SHORT).show();
//                break;
//            default:
//        }
//    }

    /*初始化抽屉界面控件并添加监听事件*/
    private void initDrawerViews() {
//        ntuAbsBtn = (Button) findViewById(R.id.news_drawer_layout_drawer_ntu_btn);
//        ntuAbsBtn.setOnClickListener(new ButtonOnClickListener());
//
//        weeksSp = (Spinner) findViewById(R.id.news_drawer_layout_drawer_weeks_spinner);
//        weeksStrs = new String[20];
//        for (int i = 0; i < 20; i++) {
//            weeksStrs[i] = "第 " + (i + 1) + " 周";
//        }
//
//        weeksSpAdpater = new ArrayAdapter<>(ActivityNews.this, android.R.layout.simple_spinner_dropdown_item, weeksStrs);
//        weeksSp.setAdapter(weeksSpAdpater);
//        weeksSp.setSelection(preferences.getInt("which_week_today", 0), true);
//        weeksSp.setOnItemSelectedListener(new SpinnerOnItemSelectedLinstener());
//
//        viewClassTableTv = (TextView) findViewById(R.id.news_drawer_layout_drawer_view_class_table_tv);
//        viewClassTableStrSpanned = new SpannableString(viewClassTableStr);
//        viewClassTableStrSpanned.setSpan(new TextViewOnClickListener(), 0, viewClassTableStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        viewClassTableTv.setText(viewClassTableStrSpanned);
//        viewClassTableTv.setMovementMethod(LinkMovementMethod.getInstance());
//
//        calendar = Calendar.getInstance();
//        week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        if (week == 0) week = 7;
//
//
//        //从数据库中获取当天的课程信息并放入list中
//        todayClassesList = new ArrayList<>();
//        classList = sqlServer.getSingleDayClassInfo(FinalStrings.SQLServerStrings.CLASS_TABLE_NAME, week);
//        for (DataClassTableClassInfo info : classList) {
//
//            String jieci = info.getBeginJieCi() + "-" + info.getEndJieCi() + "节";
//            if (info.getEndJieCi() == 0) {
//                jieci = "第" + info.getBeginJieCi() + "节";
//            }
//            DataClassTableClassInfo.ClassInfo classInfo = StringServer.getClassInfo(info.getKcInfo());
//            String kcmcStr = classInfo.getKcmcStr();
//            String classRoomStr = classInfo.getClassRoom();
//            todayClassesList.add(new DataNewsDrawerTodayClassTable(jieci, kcmcStr, classRoomStr));
//        }
//        //动态加载抽屉界面布局--list中没有数据则加载TextView--list中有数据则加载ListView显示当天的课程
//        drawerTodayClassLl = (LinearLayout) findViewById(R.id.news_drawer_layout_drawer_today_classes_ll_lv);
//        if (todayClassesList != null && todayClassesList.size() != 0) {
//            todayClassLvLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
//            todayClassesLv = new ListView(ActivityNews.this);
//            todayClassesLv.setId(FinalStrings.CustomWidgetIdStrings.drawerLlForClassLvId);
//            todayClassesLv.setLayoutParams(todayClassLvLp);
//            todayClassesAdapter = new AdapterNewsDrawerClasses(ActivityNews.this, R.layout.custom_listview_news_drawer_today_classes, todayClassesList);
//            todayClassesLv.setAdapter(todayClassesAdapter);
//            todayClassesLv.setOnItemClickListener(new ListViewOnItemClickListener());
//            drawerTodayClassLl.addView(todayClassesLv);
//            todayClassesAdapter.notifyDataSetChanged();
//        } else {
//            noClassTvLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//            noClassTvLp.gravity = Gravity.CENTER;
//            noClassTv = new TextView(ActivityNews.this);
//            noClassTv.setLayoutParams(noClassTvLp);
//            noClassTv.setGravity(Gravity.CENTER);
//            noClassTv.setPadding(10, 10, 10, 10);
//            noClassTv.setTextColor(Color.parseColor("#00FFFF"));
//            noClassTv.setTextSize(20.0f);
//            //            noClassTv.setBackgroundResource(R.drawable.frame_black_green_transparent);
//            noClassTv.setText("今天没有课哦!");
//            drawerTodayClassLl.addView(noClassTv);
//        }
//
//        settingsLv = (ListView) findViewById(R.id.news_drawer_layout_drawer_settings_about_lv);
//        settingsStrs = new String[]{"问题反馈", "关于我们", "退出"};
//        settingsAdapter = new ArrayAdapter<>(ActivityNews.this, android.R.layout.simple_list_item_1, settingsStrs);
//        settingsLv.setAdapter(settingsAdapter);
//        settingsLv.setOnItemClickListener(new ListViewOnItemClickListener());


        navigationView = (NavigationView)findViewById(R.id.main_nav_view);

        View headerLayout = navigationView.inflateHeaderView(R.layout.main_nav_header);

        classNavTv = (TextView) headerLayout.findViewById(R.id.main_nav_header_class_tv);
        numberNavTv = (TextView) headerLayout.findViewById(R.id.main_nav_header_num_tv);
        nameNavTv = (TextView) headerLayout.findViewById(R.id.main_nav_header_name_tv);

        classNavTv.setText(classNavStr);
        numberNavTv.setText(numberNavStr);
        nameNavTv.setText(nameNavStr);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.jwgl_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_JWGL;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.td_videos_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_TDVIDEOS;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.photos_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_PHOTOTD;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
//                    case R.id.library_main_nav_menu:
//                        drawerLayoutFunctionId = START_ACTIVITY_LIBRARY;
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
                    case R.id.my_lesson_table_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_CLASSTABLE;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.my_grades_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_MYGRADE;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.school_yp_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_YELLOPAGE;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.requestion_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_REQUESTION;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about_us_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_ABOUT_US;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.exit_main_nav_menu:
                        drawerLayoutFunctionId = EXIT_PRO;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.more_news_main_nav_menu:
                        drawerLayoutFunctionId = START_ACTIVITY_MORENEWS;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.zbxx_main_nav_menu:
                     drawerLayoutFunctionId = START_ACTIVITY_ZBXX;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }

                return false;
            }
        });

    }

    /*Banner中ViewPager适配器*/
    private class BannerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewContainer.get(position
                    % imageViewContainer.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = imageViewContainer.get(position
                    % imageViewContainer.size());

            // 为每一个page添加点击事件
//            view.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(ActivityNews.this, R.string.this_function_cannot_be_used_for_now_text, Toast.LENGTH_SHORT).show();
//                }
//
//            });
            container.removeView(imageViewContainer.get(position
                    % imageViewContainer.size()));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    /*ViewPager页面切换监听事件*/
    private class BannerPageChangeListener implements
            ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Nothing to do
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Nothing to do
        }

        @Override
        public void onPageSelected(int position) {
            // 取余后的索引，得到新的page的索引
//            int newPositon = position % imageViewContainer.size();
            // 根据索引设置图片的描述
            // tvBannerTextDesc.setText(bannerTextDescArray[newPositon]);
            // 把上一个点设置为被选中
//            dotGroupLl.getChildAt(preDotPosition).setEnabled(false);
            // 根据索引设置那个点被选中
//            dotGroupLl.getChildAt(newPositon).setEnabled(true);
            // 新索引赋值给上一个索引的位置
//            preDotPosition = newPositon;
        }

    }


//    /*TextView控件的监听事件*/
//    private class TextViewOnClickListener extends ClickableSpan {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
////                case R.id.news_drawer_layout_main_more_news_tv:
////                    Intent moreNewsIntent = new Intent(ActivityNews.this, ActivityMoreNews.class);
////                    startActivity(moreNewsIntent);
////                    break;
//
//                case R.id.news_drawer_layout_drawer_view_class_table_tv:
//                    drawerLayout.closeDrawer(Gravity.START);
//                    drawerLayoutFunctionId = START_ACTIVITY_CLASSTABLE;
//                    break;
//            }
//        }
//    }


    /*Spinner控件的监听事件*/
//    private class SpinnerOnItemSelectedLinstener implements AdapterView.OnItemSelectedListener {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//            switch (adapterView.getId()) {
//                case R.id.news_drawer_layout_drawer_weeks_spinner:
//
//                    editor.putInt("which_week_today", position);
//                    Toast.makeText(ActivityNews.this, weeksStrs[position], Toast.LENGTH_SHORT).show();
//
//                    break;
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//    }

    /*Button控件的监听事件*/
//    private class ButtonOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            Intent intent;
//            switch (view.getId()) {
//                //教务管理按钮
//                case R.id.news_drawer_layout_main_school_jwgl_btn:
//                    intent = new Intent(ActivityNews.this, ActivityJwgl.class);
//                    startActivity(intent);
//                    break;
//                //通大视频按钮
//                case R.id.news_drawer_layout_main_school_video_btn:
//                    intent = new Intent(ActivityNews.this, ActivitySchoolVideo.class);
//                    startActivity(intent);
//                    break;
//                //招标公告按钮
//                case R.id.news_drawer_layout_main_school_zbxx_btn:
//                    intent = new Intent(ActivityNews.this, ActivityZBXX.class);
//                    startActivity(intent);
//                    break;
//                //校园黄页按钮
//                case R.id.news_drawer_layout_main_school_yellow_page_btn:
//                    intent = new Intent(ActivityNews.this, ActivitySchoolYellowPage.class);
//                    startActivity(intent);
//                    break;
//                //更多新闻按钮
//                case R.id.news_drawer_layout_main_more_news_btn:
//                    intent = new Intent(ActivityNews.this, ActivityMoreNews.class);
//                    startActivity(intent);
//                    break;
//                //抽屉界面的app图标按钮
//                case R.id.news_drawer_layout_drawer_ntu_btn:
//                    drawerLayout.closeDrawer(GravityCompat.START);//关闭抽屉
//                    drawerLayoutFunctionId = ICON_BUTTON_PRESSED;//准备在抽屉关闭后弹出对话框
//                    break;
//
//            }
//        }
//    }

    /*ListView控件的监听事件*/
//    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            switch (adapterView.getId()) {
//                //主界面中新闻动态的ListView
//                case R.id.news_drawer_layout_main_school_news_lv:
//
//                    String link = list.get(position).getLink();
//                    String title = list.get(position).getTitle();
//                    Intent intent = new Intent(ActivityNews.this, ActivityMoreNewsView.class);
//                    if (position >= 6) {
//                        intent.putExtra("Link", "http://www.ntu.edu.cn" + link);
//                        intent.putExtra("SubTitle", title);
//                        intent.putExtra("Flag", FinalStrings.NetStrings.NEWS_TZGG);
//                        startActivity(intent);
//                    } else {
//
//                        intent.putExtra("Link", link);
//                        intent.putExtra("SubTitle", title);
//                        intent.putExtra("Flag", FinalStrings.NetStrings.NEWS_TDXW);
//                        startActivity(intent);
//                    }
//                    break;
//
//                抽屉界面的课程的Lv
//                case FinalStrings.CustomWidgetIdStrings.drawerLlForClassLvId:
//
//                    DataClassTableClassInfo kcInfoDCTCI = classList.get(position);
//
//                    String jieci = kcInfoDCTCI.getBeginJieCi() + "-" + kcInfoDCTCI.getEndJieCi() + "节";
//                    DataClassTableClassInfo.ClassInfo classInfo = StringServer.getClassInfo(kcInfoDCTCI.getKcInfo());
//
//                    StringBuilder kcInfoStr = new StringBuilder();
//                    kcInfoStr.append("课程名称:\t" + classInfo.getKcmcStr() + "\n\n");
//                    kcInfoStr.append("任课教师:\t" + classInfo.getTeacherName() + "\n\n");
//                    kcInfoStr.append("上课进程:\t" + classInfo.getWeeksStr() + "\n\n");
//                    kcInfoStr.append("上课教室:\t" + classInfo.getClassRoom() + "\n\n");
//                    kcInfoStr.append("上课节次:\t" + jieci);
//
//
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityNews.this);
//                    dialog.setTitle("课程信息").setMessage(kcInfoStr).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).show();
//                    break;
//
//                抽屉界面的系统相关Lv
//                case R.id.news_drawer_layout_drawer_settings_about_lv:
//                    switch (position) {
//                        case 0:
//                            drawerLayout.closeDrawer(GravityCompat.START);
//                            drawerLayoutFunctionId = START_ACTIVITY_REQUESTION;
//                            break;
//                        case 1:
//                            drawerLayout.closeDrawer(GravityCompat.START);
//                            drawerLayoutFunctionId = START_ACTIVITY_ABOUT_US;
//                            break;
//                        case 2:
//                            drawerLayout.closeDrawer(GravityCompat.START);
//                            drawerLayoutFunctionId = EXIT_PRO;
//                    }
//                    break;
//            }
//        }
//    }

    //开启Banner滚动线程
    private void startBannerScrollThread() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    // 每个两秒钟发一条消息到主线程，更新viewpager界面
                    SystemClock.sleep(scrollTimeOffset);

                    ActivityNews.this.runOnUiThread(new Runnable() {
                        public void run() {
                            int newindex = viewPager.getCurrentItem() + 1;
                            viewPager.setCurrentItem(newindex);
                        }
                    });
                }
            }
        }).start();
    }

    //开启从网络获取通知的线程
    private void getNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String tzggHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_TZGG);
                LogUtil.d("html",tzggHtmlStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            if (tzggHtmlStr.length() != 0) {
                                newsList = StringServer.getNewsData(tzggHtmlStr);
                                LogUtil.d("oneListLink", newsList.get(0).getLink());
                                LogUtil.d("listSize", String.valueOf(newsList.size()));
                                if (newsList != null) {
                                    LinearLayoutManager linearLayout = new LinearLayoutManager(ActivityNews.this);
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    recyclerView.setLayoutManager(linearLayout);
                                    newsAdapter = new RecycleViewAdapterMainNavNews(newsList);
                                    recyclerView.setAdapter(newsAdapter);
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                            }

                    }
                });
            }
        }).start();
    }


    //开启从网络获取新闻动态线程
    private void getNews(final boolean flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                final String html = NetServer.getHtml(FinalStrings.NetStrings.URL_OFFICIAL_URL);
                final String tdywHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_TDYW);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!flag){
                            if (tdywHtmlStr.length() != 0) {
//                            newsList = StringServer.getDataFromHtmlByClass(html);
                                newsList = StringServer.getNewsData(tdywHtmlStr);
//                            newsList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_TDYW, tdywHtmlStr);
//                            List<String> newsList = new ArrayList<>();
                                if (newsList != null) {
//                                for (int i = 0; i < newsList.size(); i++) {
//                                    newsList.add(newsList.get(i).getTitle());
//                                }
//                                newsAdapter = new ArrayAdapter(ActivityNews.this, android.R.layout.simple_list_item_1, newsList);
                                    recyclerView.setLayoutManager(gridLayoutManager);
                                    newsAdapter = new RecycleViewAdapterMainNavNews(newsList);
                                    recyclerView.setAdapter(newsAdapter);
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                            }
                        }else{
//                            if (tdywHtmlStr.length() != 0) {
////                            newsList = StringServer.getDataFromHtmlByClass(html);
////                                newsList.clear();
//                                newsList = StringServer.getNewsData(tdywHtmlStr);
////                            newsList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_TDYW, tdywHtmlStr);
////                            List<String> newsList = new ArrayList<>();
//                                if (newsList != null) {
////                                for (int i = 0; i < newsList.size(); i++) {
////                                    newsList.add(newsList.get(i).getTitle());
////                                }
////                                newsAdapter = new ArrayAdapter(ActivityNews.this, android.R.layout.simple_list_item_1, newsList);
//                                   newsAdapter.notifyDataSetChanged();
//                                    swipeRefreshLayout.setRefreshing(false);
//                                } else {
//                                    swipeRefreshLayout.setRefreshing(false);
//                                    Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                swipeRefreshLayout.setRefreshing(false);
//                                Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
//                            }
                        }

                    }
                });
            }
        }).start();
    }

    //开启从网络获取通知线程
//    private void getNotification() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final String html = NetServer.getHtml(FinalStrings.NetStrings.URL_OFFICIAL_URL);
////                final String tdywHtmlStr = NetServer.getHtml(FinalStrings.NetStrings.URL_TDYW);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (html.length() != 0) {
//                            newsList = StringServer.getDataFromHtmlByClass(html);
////                            newsList = StringServer.getNewsData(html);
////                            newsList = StringServer.getNewsData(FinalStrings.NetStrings.NEWS_TDYW, tdywHtmlStr);
////                            List<String> newsList = new ArrayList<>();
//                            if (newsList != null) {
////                                for (int i = 0; i < newsList.size(); i++) {
////                                    newsList.add(newsList.get(i).getTitle());
////                                }
////                                newsAdapter = new ArrayAdapter(ActivityNews.this, android.R.layout.simple_list_item_1, newsList);
//                                linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
//                                broadcastRecv.setLayoutManager(linearLayoutManager);
//                                broadcastAdapter = new RecycleViewAdapterMainNavBroadcast(newsList);
//                                broadcastRecv.setAdapter(broadcastAdapter);
//                                progressDialog.dismiss();
//                            } else {
//                                progressDialog.dismiss();
//                                Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            progressDialog.dismiss();
//                            Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        }).start();
//    }

    //创建文件夹
    private void createDir() {
        fileServer.makeDir(FinalStrings.FileSreverStrings.MAIN_DIR);
        fileServer.makeDir(FinalStrings.FileSreverStrings.CACHE_DIR);
        fileServer.makeDir(FinalStrings.FileSreverStrings.IMAGE_DIR);
        fileServer.makeDir(FinalStrings.FileSreverStrings.IMAGE_CACHE_DIR);
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

    //按返回键关闭抽屉与退出应用
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            if (System.currentTimeMillis() - exitTime < FinalStrings.ApplicationStrings.PROGRAM_EXIT_BACK_PRESSED_TIME) {
                editor.commit();
                super.onBackPressed();
                System.exit(FinalStrings.ApplicationStrings.PROGRAM_NORMAL_EXIT_CODE);
            } else {
                Toast.makeText(ActivityNews.this, FinalStrings.CommonStrings.PRESS_AGAIN_TO_EXIT_PRO, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
        }
    }

    //关闭线程
    @Override
    protected void onDestroy() {
        // 销毁线程
        isStop = true;
        editor.commit();
        super.onDestroy();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LogUtil.d("RESULT", String.valueOf(requestCode));
//        if (requestCode == RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    if (data.getBooleanExtra("deleteLesson", false) || data.getBooleanExtra("addLesson", false)) {
//
//                        classList = sqlServer.getSingleDayClassInfo(FinalStrings.SQLServerStrings.CLASS_TABLE_NAME, week);
//                        LogUtil.d("classLst", classList.toString());
//                        todayClassesList.clear();
//                        LogUtil.d("classLst", todayClassesList.toString());
//                        for (DataClassTableClassInfo info : classList) {
//
//                            String jieci = info.getBeginJieCi() + "-" + info.getEndJieCi() + "节";
//                            if (info.getEndJieCi() == 0) {
//                                jieci = "第" + info.getBeginJieCi() + "节";
//                            }
//                            DataClassTableClassInfo.ClassInfo classInfo = StringServer.getClassInfo(info.getKcInfo());
//                            String kcmcStr = classInfo.getKcmcStr();
//                            String classRoomStr = classInfo.getClassRoom();
//                            todayClassesList.add(new DataNewsDrawerTodayClassTable(jieci, kcmcStr, classRoomStr));
//                        }
//
//                    }
//            }
//        }
//    }

    //获取软件的当前版本号
    private String getVersionName() {
        PackageManager packageManager = ActivityNews.this.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(
                    ActivityNews.this.getPackageName(), 0);
            String version = info.versionName;

            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
