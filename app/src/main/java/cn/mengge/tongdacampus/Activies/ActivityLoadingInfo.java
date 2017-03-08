package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataClassTableInfo;
import cn.mengge.tongdacampus.classes.DataValAndText;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.JsonServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityLoadingInfo extends AppCompatActivity {

    private Spinner yuanXiSp;
    private Spinner classSp;
    private String xq;
    private List<DataValAndText> yuanXiList;
    private List<DataValAndText> classList;
    private String cookie;
    private String depId;
    private String banJiId;
    private String yuanXiStr;
    private String classStr;
    private String sqlNameStr;
    private Button completeBtn;

    private SQLServer sqlServer;

    private ProgressDialog progressDialog;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_info);
        initViews();
        getClassData();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int nextYear = thisYear + 1;
        int month = calendar.get(Calendar.MONTH) + 1;

        //计算学期
        xq = (thisYear-1) + "-" + thisYear + "-2";
        if (month >= 9 || month <=2) xq = thisYear + "-" + nextYear + "-1";

        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        editor = preferences.edit();

        cookie = preferences.getString(FinalStrings.PrefServerStrings.COOKIE, "");
        sqlNameStr = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_SQL_NAME, FinalStrings.PrefServerStrings.STUDENT_SQL_NAME_DEFAULT);
        sqlServer = new SQLServer(ActivityLoadingInfo.this, sqlNameStr, FinalStrings.SQLServerStrings.SQL_VERSION);

        progressDialog = new ProgressDialog(ActivityLoadingInfo.this);
        progressDialog.setMessage(FinalStrings.CommonStrings.GET_CONTENT_ING);

        completeBtn = (Button) findViewById(R.id.loading_info_completed_bt);
        yuanXiSp = (Spinner) findViewById(R.id.loading_info_choice_xueyuan_sp);
        classSp = (Spinner) findViewById(R.id.loading_info_choice_class_sp);

        yuanXiSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        classSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());

        completeBtn.setOnClickListener(new ButtonOnClickListener());
    }

    //获取所有院系并装载适配器
    private void getClassData() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String jsonStr = NetServer.getClassTableYuanXi(cookie, xq);
                Log.d("jsonStr", jsonStr);
                ActivityLoadingInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (jsonStr.length() != 0) {
                            yuanXiList = JsonServer.parseClassValAndTextJsonData(jsonStr);
                            if (yuanXiList.size() != 0) {
                                String[] textStrs = new String[yuanXiList.size()];
                                for (int i = 0; i < yuanXiList.size(); i++) {
                                    textStrs[i] = yuanXiList.get(i).getText();
                                }
                                yuanXiSp.setAdapter(new ArrayAdapter<>(ActivityLoadingInfo.this, android.R.layout.simple_spinner_dropdown_item, textStrs));

                            }
                        } else
                            Toast.makeText(ActivityLoadingInfo.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    //获取院系中所有班级并装载适配器
    private void getTableData() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String jsonData = NetServer.getClassTable(cookie, depId);
                ActivityLoadingInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (jsonData.length() != 0) {
                            classList = JsonServer.parseClassValAndTextJsonData(jsonData);
                            if (classList.size() != 0) {
                                String[] textStrs = new String[classList.size()];
                                for (int i = 0; i < classList.size(); i++) {
                                    textStrs[i] = classList.get(i).getText();
                                }
                                classSp.setAdapter(new ArrayAdapter<>(ActivityLoadingInfo.this, android.R.layout.simple_spinner_dropdown_item, textStrs));
                            }
                        } else
                            Toast.makeText(ActivityLoadingInfo.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //获取课表信息并保存到数据库中
    private void getClassTable() {
        progressDialog.setMessage("正在获取课表信息......");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String jsonDataStr = NetServer.getClassTable(cookie, xq, banJiId);
                ActivityLoadingInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jsonDataStr.length() != 0) {
                            List<DataClassTableInfo> list = JsonServer.parseClassTableData(jsonDataStr);
                            if (list.size() != 0) {
                                sqlServer.saveClassTable2DB(FinalStrings.SQLServerStrings.CLASS_TABLE_TABLE_NAME,list);
                            }

                        }
                    }
                });
            }
        }).start();
    }

    //获取成绩并保存到数据库
    private void getGrade() {
        progressDialog.setMessage("正在获取成绩信息......");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String gradeJsonStr = NetServer.getGradeJsonData(cookie);

                ActivityLoadingInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (gradeJsonStr.length() != 0) {
                            List<DataGrade> list = JsonServer.parseGradeTableData(0, gradeJsonStr);
                            if (list.size() != 0) {
                                sqlServer.addGrade2GradeTable(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME, list);

                                Intent intent = new Intent(ActivityLoadingInfo.this, ActivityNews.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityLoadingInfo.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }).start();
    }

    //加载信息按钮监听事件
    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loading_info_completed_bt:
                    editor.putBoolean("first_login", false);
                    editor.putString("yuan_xi_val", depId);
                    editor.putString("yuan_xi_text", yuanXiStr);
                    editor.putString("class_val", banJiId);
                    editor.putString("class_text", classStr);
                    editor.commit();
                    getClassTable();
                    getGrade();
                    break;
            }
        }
    }

    //院系和班级选择spinner监听事件
    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            switch (adapterView.getId()) {
                case R.id.loading_info_choice_xueyuan_sp:
                    depId = yuanXiList.get(position).getVal();
                    yuanXiStr = yuanXiList.get(position).getText();
                    getTableData();
                    break;

                case R.id.loading_info_choice_class_sp:
                    banJiId = classList.get(position).getVal();
                    classStr = classList.get(position).getText();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

}
