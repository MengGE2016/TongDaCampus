package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.AdapterGrade;
import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.classes.DataGradeNameAndOthers;
import cn.mengge.tongdacampus.server.JsonServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.server.StringServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityOtherStudentGrade extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView studentInfoTv;
    private ListView gradeLv;
    private SharedPreferences preferences;
    private String cookie;

    private List<DataGrade> gradeList;
    private List<DataGradeNameAndOthers> gradeInfoList;
    private SQLServer sqlServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_student_grade);

        initViews(); //初始化控件
        getStudentInfo(); //获取学生信息
        getStudentGrade(); //获取学生成绩
    }

    //初始化控件并添加监听事件
    private void initViews() {
        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sqlServer = new SQLServer(ActivityOtherStudentGrade.this);
        //清空数据库--为重新写入做好准备
        if (sqlServer.getSQLTotalCount(FinalStrings.SQLServerStrings.TEMP_GRADE_TABLE_NAME) != 0) {
            sqlServer.deleteTableData(FinalStrings.SQLServerStrings.TEMP_GRADE_TABLE_NAME);
        }

        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.GUEST_PREF, MODE_PRIVATE);
        cookie = preferences.getString(FinalStrings.PrefServerStrings.GUEST_COOKIE, "");
        studentInfoTv = (TextView) findViewById(R.id.student_info_show_info_tv);
        gradeLv = (ListView) findViewById(R.id.student_info_grade_lv);

    }

    //从网页中获取学生信息
    private void getStudentInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityOtherStudentGrade.this);
        progressDialog.setMessage("正在获取信息......");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String html = NetServer.getStudentInfo(cookie, FinalStrings.NetStrings.URL_STUDENT_MAIN);
                LogUtil.d("StudentInfoHtml", html);
                ActivityOtherStudentGrade.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (html.length() != 0) {
                            Spanned spanned = Html.fromHtml(StringServer.getStudentInfoFromHtml(html));
                            studentInfoTv.setText(spanned);
                        } else {
                            Toast.makeText(ActivityOtherStudentGrade.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    //获取学生的成绩
    private void getStudentGrade() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String gradeJson = NetServer.getGradeJsonData(cookie);

                ActivityOtherStudentGrade.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (gradeJson.length() != 0) {
                            gradeList = JsonServer.parseGradeTableData(0, gradeJson);//解析成绩的JSON数据
                            //保存成绩到游客数据库中
                            sqlServer.addGrade2GradeTable(FinalStrings.SQLServerStrings.TEMP_GRADE_TABLE_NAME, gradeList);
                            //从数据库中获取成绩
                            List<DataGradeNameAndOthers> gradesList = sqlServer.getGradeNameFromSQL(FinalStrings.SQLServerStrings.TEMP_GRADE_TABLE_NAME);
                            gradeInfoList = new ArrayList<>();
                            for (int i = gradesList.size() - 1; i >= 0; i--) {
                                gradeInfoList.add(gradesList.get(i));
                            }
                            gradeLv.setAdapter(new AdapterGrade(ActivityOtherStudentGrade.this, R.layout.custom_listview_grade, gradeInfoList));
                            gradeLv.setOnItemClickListener(new ListViewOnItemClickListener());
                        } else
                            Toast.makeText(ActivityOtherStudentGrade.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).start();
    }

    //ListView 的监听事件
    protected class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            String gradeInfo = sqlServer.getGradeDetails(FinalStrings.SQLServerStrings.TEMP_GRADE_TABLE_NAME, gradeInfoList.get(position).getCjid());
            final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityOtherStudentGrade.this);
            dialog.setTitle(gradeInfoList.get(position).getKcmc()).setMessage(gradeInfo).setPositiveButton(FinalStrings.CommonStrings.MAKE_SURE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    //Toolbar返回键监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}