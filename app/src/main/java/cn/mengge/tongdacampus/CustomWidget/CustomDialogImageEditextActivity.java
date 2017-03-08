package cn.mengge.tongdacampus.CustomWidget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.mengge.tongdacampus.Activies.ActivityGradeQuery;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataCookieAndCheckCodeBm;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.server.JsonServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class CustomDialogImageEditextActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private AutoCompleteTextView textView;
    private Button yesBtn;
    private Button noBtn;
    private Button freshBtn;

    private SharedPreferences preferences;

    private String cookie;
    private ProgressDialog progressDialog;

    private int functionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_image_editext);
        initViews();
        getCookieAndCheckCode();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);

        functionId = getIntent().getIntExtra("function", 0);
        switch (functionId) {
            case 0: //课表查询
                break;
            case 1://空教室查询
                break;
            case 2://成绩查询
                break;
        }

        progressDialog = new ProgressDialog(CustomDialogImageEditextActivity.this);
        progressDialog.setMessage("正在刷新......");

        imageView = (ImageView) findViewById(R.id.custom_dialog_checkcode_iv);
        textView = (AutoCompleteTextView) findViewById(R.id.custom_dialog_checkcode_actv);
        yesBtn = (Button) findViewById(R.id.custom_dialog_make_sure_bt);
        noBtn = (Button) findViewById(R.id.custom_dialog_cancel_bt);
        freshBtn = (Button) findViewById(R.id.custom_dialog_refresh_bt);

        yesBtn.setOnClickListener(CustomDialogImageEditextActivity.this);
        noBtn.setOnClickListener(CustomDialogImageEditextActivity.this);
        freshBtn.setOnClickListener(CustomDialogImageEditextActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_dialog_make_sure_bt:
                if (textView.getText().length() == 0) {
                    Toast.makeText(CustomDialogImageEditextActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String number = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_NUM, "");
                String sfzh = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_SFZH, "");
                String password = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_JWGL_PASSWORD, "");
                String checkCode = textView.getText().toString();
                loginJwgl(number, sfzh, password, checkCode);
                break;

            case R.id.custom_dialog_cancel_bt:
                finish();
                break;


            case R.id.custom_dialog_refresh_bt:
                getCookieAndCheckCode();
                break;

        }
    }

    //登录教务管理系统
    private void loginJwgl(final String number, final String sfzh, final String password, final String checkCode) {

        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isLogin = NetServer.isLoginJwglSuccess(CustomDialogImageEditextActivity.this, FinalStrings.NetStrings.URL_JWGL_LOGIN, cookie, number, sfzh, password, checkCode);
                CustomDialogImageEditextActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isLogin) {
                            //成绩查询
                            if (functionId == 2) {
                                getGradeJsonDataAndStroreDB();
                            } else {
                                progressDialog.dismiss();
                                Intent intent = new Intent(CustomDialogImageEditextActivity.this, CustomDialog2SpinnersActivity.class);
                                intent.putExtra("cookie", cookie);
                                intent.putExtra("function", functionId); //0表示课表查询,1表示空教室查询
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(CustomDialogImageEditextActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        }).start();
    }


    //获取验证码和Cookie
    private void getCookieAndCheckCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataCookieAndCheckCodeBm dataCookieAndCheckCodeBm = NetServer.getCookieAndCheckCodeBitmap(CustomDialogImageEditextActivity.this, FinalStrings.NetStrings.URL_CHECK_CODE_COOKIE);

                CustomDialogImageEditextActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataCookieAndCheckCodeBm != null) {
                            imageView.setImageBitmap(dataCookieAndCheckCodeBm.getBitmap());
                            cookie = dataCookieAndCheckCodeBm.getCookie();
                        } else
                            Toast.makeText(CustomDialogImageEditextActivity.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }


    //从教务管理系统获取成绩
    private void getGradeJsonDataAndStroreDB() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String jsonDataStr = NetServer.getGradeJsonData(cookie);

                CustomDialogImageEditextActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (jsonDataStr.length() != 0) {
                            SQLServer sqlServer = new SQLServer(CustomDialogImageEditextActivity.this, preferences.getString("student_personal_db", "0"), FinalStrings.SQLServerStrings.SQL_VERSION);

                            List<DataGrade> list = JsonServer.parseGradeTableData(sqlServer.getSQLTotalCount(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME), jsonDataStr);
                            if (list.size() != 0) {
                                sqlServer.addGrade2GradeTable(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME, list);
                                Toast.makeText(CustomDialogImageEditextActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomDialogImageEditextActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                            }
//                            Intent intent = new Intent(CustomDialogImageEditextActivity.this, ActivityGradeQuery.class);
                            Intent intent = new Intent();
                            setResult(RESULT_OK,intent);
//                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CustomDialogImageEditextActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        }).start();


    }

}
