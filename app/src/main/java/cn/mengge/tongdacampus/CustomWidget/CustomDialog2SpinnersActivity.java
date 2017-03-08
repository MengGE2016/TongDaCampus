package cn.mengge.tongdacampus.CustomWidget;

import android.app.ProgressDialog;
import android.content.Intent;
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

import cn.mengge.tongdacampus.Activies.ActivityClassTable;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataClassTableInfo;
import cn.mengge.tongdacampus.classes.DataValAndText;
import cn.mengge.tongdacampus.server.JsonServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class CustomDialog2SpinnersActivity extends AppCompatActivity {

    private Spinner mainSp;
    private Spinner subSp;
    private Button yesBtn;
    private Button noBtn;
    private String cookie;
    private String xq;
    private String mainID;
    private ProgressDialog progressDialog;

    private List<DataValAndText> subList;
    private List<DataValAndText> mainList;

    private String mainText;
    private String subText;
    private String subId;

    private SQLServer sqlServer;

    private int functionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_2_spinners);
        initViews();
        getClassData();
    }

    //初始化控件并添加监听事件
    private void initViews() {

        cookie = getIntent().getStringExtra("cookie");
        functionId = getIntent().getIntExtra("function", 0);

        progressDialog = new ProgressDialog(CustomDialog2SpinnersActivity.this);
        progressDialog.setMessage("正在获取信息......");

        sqlServer = new SQLServer(CustomDialog2SpinnersActivity.this);

        if (sqlServer.getSQLTotalCount(FinalStrings.SQLServerStrings.TEMP_CLASS_TABLE_NAME) != 0) {
            sqlServer.deleteTableData(FinalStrings.SQLServerStrings.TEMP_CLASS_TABLE_NAME);
        }
        if (sqlServer.getSQLTotalCount(FinalStrings.SQLServerStrings.EMPTY_CLASSROOM_TABLE_NAME) != 0) {
            sqlServer.deleteTableData(FinalStrings.SQLServerStrings.EMPTY_CLASSROOM_TABLE_NAME);
        }

        mainSp = (Spinner) findViewById(R.id.custom_dialog_class_yuanxi_sp);
        subSp = (Spinner) findViewById(R.id.custom_dialog_class_banji_sp);
        mainSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        subSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());


        yesBtn = (Button) findViewById(R.id.custom_dialog_class_makesure_btn);
        noBtn = (Button) findViewById(R.id.custom_dialog_class_cancel_btn);
        yesBtn.setOnClickListener(new ButtonOnClickListener());
        noBtn.setOnClickListener(new ButtonOnClickListener());
    }

    //获取所有院系或校区并装载适配器
    private void getClassData() {
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int nextYear = thisYear + 1;
        int month = calendar.get(Calendar.MONTH) + 1;

        //计算学期
        xq = (thisYear-1) + "-" + thisYear + "-2";
        if (month >= 9 || month <=2) xq = thisYear + "-" + nextYear + "-1";

        if (functionId == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String jsonStr = NetServer.getClassTableYuanXi(cookie, xq);
                    Log.d("jsonStr", jsonStr);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonStr.length() != 0) {
                                mainList = JsonServer.parseClassValAndTextJsonData(jsonStr);
                                if (mainList.size() != 0) {
                                    String[] textStrs = new String[mainList.size()];
                                    for (int i = 0; i < mainList.size(); i++) {
                                        textStrs[i] = mainList.get(i).getText();
                                    }
                                    mainSp.setAdapter(new ArrayAdapter<>(CustomDialog2SpinnersActivity.this, android.R.layout.simple_spinner_dropdown_item, textStrs));

                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

        if (functionId == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String jsonStr = NetServer.getXiaoQu(cookie, xq);
                    Log.d("jsonStr", jsonStr);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonStr.length() != 0) {
                                mainList = JsonServer.parseClassValAndTextJsonData(jsonStr);
                                if (mainList.size() != 0) {
                                    String[] textStrs = new String[mainList.size()];
                                    for (int i = 0; i < mainList.size(); i++) {
                                        textStrs[i] = mainList.get(i).getText();
                                    }
                                    mainSp.setAdapter(new ArrayAdapter<>(CustomDialog2SpinnersActivity.this, android.R.layout.simple_spinner_dropdown_item, textStrs));

                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

    }

    //获取院系中所有班级或校区教室并装载适配器
    private void getTableData() {

        if (functionId == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String jsonData = NetServer.getClassTable(cookie, mainID);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonData.length() != 0) {
                                subList = JsonServer.parseClassValAndTextJsonData(jsonData);
                                if (subList.size() != 0) {
                                    String[] textStrs = new String[subList.size()];
                                    for (int i = 0; i < subList.size(); i++) {
                                        textStrs[i] = subList.get(i).getText();
                                    }
                                    subSp.setAdapter(new ArrayAdapter<>(CustomDialog2SpinnersActivity.this, android.R.layout.simple_spinner_dropdown_item, textStrs));
                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

        if (functionId == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String jsonData = NetServer.getClassRoom(cookie, mainID);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonData.length() != 0) {
                                subList = JsonServer.parseClassValAndTextJsonData(jsonData);
                                if (subList.size() != 0) {
                                    String[] textStrs = new String[subList.size()];
                                    for (int i = 0; i < subList.size(); i++) {
                                        textStrs[i] = subList.get(i).getText();
                                    }
                                    subSp.setAdapter(new ArrayAdapter<>(CustomDialog2SpinnersActivity.this, android.R.layout.simple_spinner_dropdown_item, textStrs));
                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

    }

    //获取课表信息并保存到数据库中
    private void getClassTable() {
        progressDialog.show();
        //课表查询
        if (functionId == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    final String jsonDataStr = NetServer.getClassTable(cookie, xq, subId);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if (jsonDataStr.length() != 0) {
                                List<DataClassTableInfo> list = JsonServer.parseClassTableData(jsonDataStr);
                                if (list.size() != 0) {
                                    sqlServer.saveClassTable2DB(FinalStrings.SQLServerStrings.TEMP_CLASS_TABLE_NAME, list);

                                    Intent intent = new Intent(CustomDialog2SpinnersActivity.this, ActivityClassTable.class);
                                    intent.putExtra("account", 1);
                                    intent.putExtra("function", functionId);
                                    intent.putExtra("toolbar_subtitle", mainText + "\t\t" + subText);
                                    startActivity(intent);
                                    finish();
                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

        //空教室查询
        if (functionId == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    final String jsonDataStr = NetServer.getUseClassRoom(cookie, xq, subId);
                    CustomDialog2SpinnersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if (jsonDataStr.length() != 0) {
                                LogUtil.d("classroomjson", jsonDataStr);
                                List<DataClassTableInfo> list = JsonServer.parseClassTableData(jsonDataStr);
                                if (list.size() != 0) {
                                    sqlServer.saveClassTable2DB(FinalStrings.SQLServerStrings.EMPTY_CLASSROOM_TABLE_NAME, list);

                                    Intent intent = new Intent(CustomDialog2SpinnersActivity.this, ActivityClassTable.class);
                                    intent.putExtra("account", 1);
                                    intent.putExtra("function", functionId);
                                    intent.putExtra("toolbar_subtitle", mainText + "\t\t" + subText);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CustomDialog2SpinnersActivity.this, "该教室暂时无人使用", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else
                                Toast.makeText(CustomDialog2SpinnersActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }

    }


    //Spinner的监听事件
    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            switch (adapterView.getId()) {
                case R.id.custom_dialog_class_yuanxi_sp:

                    mainID = mainList.get(position).getVal();
                    LogUtil.d("depID", mainID);
                    mainText = mainList.get(position).getText();
                    getTableData();
                    break;

                case R.id.custom_dialog_class_banji_sp:
                    subId = subList.get(position).getVal();
                    LogUtil.d("banjiID", subId);
                    subText = subList.get(position).getText();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    //Button的监听事件
    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.custom_dialog_class_makesure_btn:
                    getClassTable();
                    break;

                case R.id.custom_dialog_class_cancel_btn:
                    finish();
                    break;
            }
        }
    }
}
