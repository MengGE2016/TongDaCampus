package cn.mengge.tongdacampus.Activies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.CustomDialogAddLessonIfoActivity;
import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataClassTableClassInfo;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityClassTable extends AppCompatActivity {

    private TextView emptyTv;       // 第一个无内容的格子
    private TextView monColumTv;    // 星期一的格子
    private TextView tueColumTv;    // 星期二的格子
    private TextView wedColumTv;    // 星期三的格子
    private TextView thrusColumTv;  // 星期四的格子
    private TextView friColumTv;    // 星期五的格子
    private TextView satColumTv;    // 星期六的格子
    private TextView sunColumTv;    // 星期日的格子
    private RelativeLayout courseTableRl;   // 课程表body部分布局
    private int screenWidth;                // 屏幕宽度 
    private int aveWidth;                   // 课程格子平均宽度

    private DisplayMetrics displayMetrics;
    private int width;
    private int height;
    private int gridHeight;

    private Toolbar toolbar;
    private List<DataClassTableClassInfo> classList;
    private SQLServer sqlServer;
    private SharedPreferences preferences;

    private String tableName;

    private FloatingActionButton addClassFab;

    private boolean isGuest;
    private int functionId;
    private int accountId;
//    private boolean deleteLesson = false;
//    private boolean addLesson = false;

    // 五种颜色的背景
    private int[] background = {
            R.drawable.course_info_blue, R.drawable.course_info_deep_teal,
            R.drawable.course_info_green, R.drawable.course_info_red,
            R.drawable.course_info_oreagen, R.drawable.course_info_yellow,
            R.drawable.course_info_black, R.drawable.course_info_big_red,
            R.drawable.course_info_purple, R.drawable.course_info_gray
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_table);

        initViews();
        addClassContent();
    }

    //初始化界面控件
    private void initViews() {

        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitle("课程表");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //准备从数据库获取课程表
        preferences = getSharedPreferences("PERSONAL_PREF", MODE_PRIVATE);
        LogUtil.d("account", String.valueOf(getIntent().getIntExtra("account", 0)));

        String toolBarSubTitle = getIntent().getStringExtra("toolbar_subtitle");
        accountId = getIntent().getIntExtra("account", 0);

        switch (accountId) {
            case 0:
                //个人数据库
                String sqlName = preferences.getString("student_personal_db", "0");
                sqlServer = new SQLServer(ActivityClassTable.this, sqlName, FinalStrings.SQLServerStrings.SQL_VERSION);
                tableName = FinalStrings.SQLServerStrings.CLASS_TABLE_TABLE_NAME;
                isGuest = false;
                toolbar.setSubtitle("我的课程");
                break;
            case 1:
                //游客数据库
                sqlServer = new SQLServer(ActivityClassTable.this);
                tableName = FinalStrings.SQLServerStrings.TEMP_CLASS_TABLE_NAME;
                isGuest = true;
                toolbar.setSubtitle(toolBarSubTitle);
                break;
        }
        LogUtil.d("functionId", String.valueOf(getIntent().getIntExtra("function", 0)));
        functionId = getIntent().getIntExtra("function", 0);
        if (functionId == 1) {
            tableName = FinalStrings.SQLServerStrings.EMPTY_CLASSROOM_TABLE_NAME;
            toolbar.setTitle("教室使用情况");
            toolbar.setSubtitle(toolBarSubTitle);
        }

        // 获得列头的控件
        emptyTv = (TextView) findViewById(R.id.test_empty);
        monColumTv = (TextView) findViewById(R.id.test_monday_course);
        tueColumTv = (TextView) findViewById(R.id.test_tuesday_course);
        wedColumTv = (TextView) findViewById(R.id.test_wednesday_course);
        thrusColumTv = (TextView) findViewById(R.id.test_thursday_course);
        friColumTv = (TextView) findViewById(R.id.test_friday_course);
        satColumTv = (TextView) findViewById(R.id.test_saturday_course);
        sunColumTv = (TextView) findViewById(R.id.test_sunday_course);
        courseTableRl = (RelativeLayout) findViewById(R.id.test_course_rl);

        addClassFab = (FloatingActionButton) findViewById(R.id.class_table_add_class_fab);
        addClassFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGuest) {
                    Toast.makeText(ActivityClassTable.this, "当前状态不能添加课程", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ActivityClassTable.this, CustomDialogAddLessonIfoActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 屏幕宽度
        width = displayMetrics.widthPixels;
        // 平均宽度
        int aveWidth = width / 8;
        // 第一个空白格子设置为25宽
        emptyTv.setWidth(aveWidth * 3 / 4);
        monColumTv.setWidth(aveWidth * 33 / 32 + 1);
        tueColumTv.setWidth(aveWidth * 33 / 32 + 1);
        wedColumTv.setWidth(aveWidth * 33 / 32 + 1);
        thrusColumTv.setWidth(aveWidth * 33 / 32 + 1);
        friColumTv.setWidth(aveWidth * 33 / 32 + 1);
        satColumTv.setWidth(aveWidth * 33 / 32 + 1);
        sunColumTv.setWidth(aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        height = displayMetrics.heightPixels;
        gridHeight = height / 12;


        // 设置课表界面 动态生成12 * maxCourseNum 个 textview
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 8; j++) {
                TextView textView = new TextView(ActivityClassTable.this);
                textView.setId((i - 1) * 8 + j);
                // 除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j < 8)
                    textView.setBackgroundResource(R.drawable.course_text_view_bg);
                else
                    textView.setBackgroundResource(R.drawable.course_table_last_colum);

                // 相对布局参数
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1, gridHeight);
                // 文字对齐方式
                textView.setGravity(Gravity.CENTER);
                // 字体样式
                textView.setPadding(2, 2, 2, 2);
                textView.setTextSize(12.0f);
                textView.setTextColor(R.color.course_table_text_view_color);
                // tx.setTextAppearance(ActivityClassTable.this, R.style.courseTableText);
                // 如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    textView.setText(String.valueOf(i));
                    layoutParams.width = aveWidth * 3 / 4;
                    // 设置他们的相对位置
                    if (i == 1)
                        layoutParams.addRule(RelativeLayout.BELOW, emptyTv.getId());
                    else
                        layoutParams.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    layoutParams.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    textView.setText("");
                }

                textView.setLayoutParams(layoutParams);
                courseTableRl.addView(textView);
            }
        }

//        addClassContent();
    }

    //给课表中添加内容--各色Button并设置监听处理事件
    private void addClassContent() {
        for (int i = 0; i < 7; i++) {
            final int week = i + 1;
            classList = sqlServer.getSingleDayClassInfo(tableName, week);
            LogUtil.d("classList", classList.toString());
            for (int j = 0; j < classList.size(); j++) {
                final String kcmcInfo = classList.get(j).getKcInfo();
                final int begin = classList.get(j).getBeginJieCi();
                final int end = classList.get(j).getEndJieCi();

                // 添加课程信息
                final Button courseInfoBt = new Button(ActivityClassTable.this);
                if (functionId != 1 && (accountId == 1 || accountId == 0)) {
                    // 设置一种背景
                    courseInfoBt.setText(kcmcInfo);
//                    int backgroundRandom = (int) (Math.random() * 10 - 1);
//                    courseInfoBt.setBackgroundResource(background[backgroundRandom]);
                    courseInfoBt.setBackgroundResource(R.drawable.course_info_black);

                } else {
                    addClassFab.hide();
                    courseInfoBt.setText("被占用");
                    courseInfoBt.setBackgroundResource(background[7]);
                }

                courseInfoBt.setTextSize(13.0f);
                // 该textview的高度根据其节数的跨度来设置
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        aveWidth * 31 / 32, (gridHeight - 5) * (end - begin + 1)/* 一共上几节课 */);
                // textview的位置由课程开始节数和上课的时间（day of week）确定
                layoutParams.topMargin = 5 + (begin/* 开始上课节次 */ - 1) * gridHeight;
                layoutParams.leftMargin = 2;
                // 偏移由这节课是星期几决定
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i + 1/* 周几上课 */);
                // 字体居中
                courseInfoBt.setGravity(Gravity.CENTER_HORIZONTAL);


                courseInfoBt.setPadding(5, 5, 5, 5);
                courseInfoBt.setLayoutParams(layoutParams);
                courseInfoBt.setTextColor(Color.WHITE);
                // 设置不透明度
                courseInfoBt.getBackground().setAlpha(222);

                if (!isGuest) {
                    courseInfoBt.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            CharSequence[] deleteFunctions = {/*"修改课程信息", */"删除该课程"};
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityClassTable.this);
                            dialog.setItems(deleteFunctions, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
//                                    if (position == 0) {
//                                        Toast.makeText(ActivityClassTable.this, "修改课程信息", Toast.LENGTH_SHORT).show();
//                                    }
                                    if (position == 0) {
                                        sqlServer.deleteLessonInfoFromSQL(FinalStrings.SQLServerStrings.CLASS_TABLE_NAME, week, begin, end);
                                        Toast.makeText(ActivityClassTable.this, "删除成功", Toast.LENGTH_SHORT).show();
//                                        deleteLesson = true;
                                        courseInfoBt.setText("");
                                        courseInfoBt.setBackgroundColor(Color.parseColor("#00ffffff"));

                                    }
                                }
                            });
                            dialog.show();
                            return false;
                        }
                    });
                }


                courseInfoBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String jieci = begin + "-" + end + "节";
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityClassTable.this);

                        if (functionId != 1 && (accountId == 1 || accountId == 0)) {
                            DataClassTableClassInfo.ClassInfo classInfo = StringServer.getClassInfo(kcmcInfo);

                            StringBuilder kcInfoStr = new StringBuilder();
                            kcInfoStr.append("任课教师:\t" + classInfo.getTeacherName() + "\n\n");
                            kcInfoStr.append("上课进程:\t" + classInfo.getWeeksStr() + "\n\n");
                            kcInfoStr.append("上课教室:\t" + classInfo.getClassRoom() + "\n\n");
                            kcInfoStr.append("上课节次:\t" + jieci);
                            dialog.setTitle(classInfo.getKcmcStr());
                            dialog.setMessage(kcInfoStr);
                        } else {
                            dialog.setTitle("节次:" + jieci);
                            char[] chars = kcmcInfo.toCharArray();
                            StringBuilder info = new StringBuilder(" ");
                            for (char c : chars) {
                                info.append(c);
                                if (c == ']') {
                                    info.append("\n");
                                }
                            }

                            dialog.setMessage(info);
                        }

                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                courseTableRl.addView(courseInfoBt);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    boolean isAddSuccess = data.getBooleanExtra("IsSuccess", false);
                    if (isAddSuccess) {
//                        addLesson = true;
                        ActivityClassTable.this.recreate();
                    }
                }
        }
    }

    //Toolbar返回键监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                LogUtil.d("destory", String.valueOf(deleteLesson));
//                LogUtil.d("destory1", "yes");
//                Intent intent = new Intent();
//                LogUtil.d("destory", String.valueOf(deleteLesson));
//                LogUtil.d("destory", String.valueOf(addLesson));
//                intent.putExtra("deleteLesson", deleteLesson);
//                intent.putExtra("addLesson", addLesson);
//                setResult(RESULT_OK, intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        LogUtil.d("destory", "yes");
//        Intent intent = new Intent();
//        LogUtil.d("destory", String.valueOf(deleteLesson));
//        LogUtil.d("destory", String.valueOf(addLesson));
//        intent.putExtra("deleteLesson", deleteLesson);
//        intent.putExtra("addLesson", addLesson);
//        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
