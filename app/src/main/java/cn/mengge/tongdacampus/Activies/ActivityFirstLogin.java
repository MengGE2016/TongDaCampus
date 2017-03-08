package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataCookieAndCheckCodeBm;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityFirstLogin extends AppCompatActivity {

    private Button loginBtn;
//    private Button guestBtn;
    private Button refreshBtn;
    private AutoCompleteTextView studentNumEd;
    private AutoCompleteTextView studentSFZHEd;
    private EditText studentPasswdEd;
    private AutoCompleteTextView studentCheckCodeEd;
    private ImageView checkCodeIv;
    private ProgressDialog progressDialog;

    private String studentNumStr;
    private String studentSFZHStr;
    private String studentPasswdStr;
    private String studentCheckCodeStr;
    private String COOKIE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        initViews();
        getCheckCodeAndCookie();
    }

    private void initViews() {

        loginBtn = (Button) findViewById(R.id.first_login_login_btn);
//        guestBtn = (Button) findViewById(R.id.first_login_guest_btn);
        refreshBtn = (Button) findViewById(R.id.first_login_student_refresh_check_code_btn);
        studentNumEd = (AutoCompleteTextView) findViewById(R.id.first_login_student_num_ed);
        studentSFZHEd = (AutoCompleteTextView) findViewById(R.id.first_login_student_sfzh_ed);
        studentPasswdEd = (EditText) findViewById(R.id.first_login_student_password_ed);
        studentCheckCodeEd = (AutoCompleteTextView) findViewById(R.id.first_login_student_check_code_ed);
        checkCodeIv = (ImageView) findViewById(R.id.first_login_student_check_code_iv);


        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        editor = preferences.edit();
        studentNumEd.setText(preferences.getString(FinalStrings.PrefServerStrings.STUDENT_NUM, ""));
        studentSFZHEd.setText(preferences.getString(FinalStrings.PrefServerStrings.STUDENT_SFZH, ""));
        studentPasswdEd.setText(preferences.getString(FinalStrings.PrefServerStrings.STUDENT_JWGL_PASSWORD, ""));


        progressDialog = new ProgressDialog(ActivityFirstLogin.this);
        progressDialog.setMessage(FinalStrings.CommonStrings.LOGIN_LOGINING);
        progressDialog.setCancelable(true);

        loginBtn.setOnClickListener(new ButtonOnClickListener());
//        guestBtn.setOnClickListener(new ButtonOnClickListener());
        refreshBtn.setOnClickListener(new ButtonOnClickListener());
    }

    //Button的监听事件
    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //帐号登录
                case R.id.first_login_login_btn:
                    login();
                    break;
                //游客登录
//                case R.id.first_login_guest_btn:
//                    Intent intent = new Intent(ActivityFirstLogin.this, ActivityNews.class);
//                    startActivity(intent);
//                    finish();
//                    break;
                //刷新验证码
                case R.id.first_login_student_refresh_check_code_btn:
                    getCheckCodeAndCookie();
                    break;
            }
        }
    }

    //获取验证码和cookie
    private void getCheckCodeAndCookie() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataCookieAndCheckCodeBm cookieAndCheckCodeBm = NetServer.getCookieAndCheckCodeBitmap(ActivityFirstLogin.this, FinalStrings.NetStrings.URL_CHECK_CODE_COOKIE);
                ActivityFirstLogin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cookieAndCheckCodeBm == null) {
                            Toast.makeText(ActivityFirstLogin.this, R.string.login_network_not_connect_text,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            COOKIE = cookieAndCheckCodeBm.getCookie();
                            checkCodeIv.setImageBitmap(cookieAndCheckCodeBm.getBitmap());
                        }
                    }
                });
            }
        }).start();
    }

    //登录操作
    private void login() {
        studentNumStr = studentNumEd.getText().toString();
        studentSFZHStr = studentSFZHEd.getText().toString();
        studentPasswdStr = studentPasswdEd.getText().toString();
        studentCheckCodeStr = studentCheckCodeEd.getText().toString();

        if (studentNumStr.length() == 0) {
            Toast.makeText(ActivityFirstLogin.this, R.string.login_student_number_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentSFZHStr.length() == 0) {
            Toast.makeText(ActivityFirstLogin.this, R.string.login_student_sfzh_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentPasswdStr.length() == 0) {
            Toast.makeText(ActivityFirstLogin.this, R.string.login_student_password_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentCheckCodeStr.length() == 0) {
            Toast.makeText(ActivityFirstLogin.this, R.string.login_student_check_code_not_null_text, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean flag = NetServer.isLoginJwglSuccess(ActivityFirstLogin.this, FinalStrings.NetStrings.URL_JWGL_LOGIN, COOKIE, studentNumStr, studentSFZHStr, studentPasswdStr, studentCheckCodeStr);
                    ActivityFirstLogin.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                editor.putString(FinalStrings.PrefServerStrings.COOKIE, COOKIE);
                                editor.putString(FinalStrings.PrefServerStrings.STUDENT_NUM, studentNumStr);
                                editor.putString(FinalStrings.PrefServerStrings.STUDENT_SFZH, studentSFZHStr);
                                editor.putString(FinalStrings.PrefServerStrings.STUDENT_JWGL_PASSWORD, studentPasswdStr);
                                editor.putString(FinalStrings.PrefServerStrings.STUDENT_SQL_NAME, studentNumStr + ".DB");
                                editor.commit();
                                Toast.makeText(ActivityFirstLogin.this, FinalStrings.CommonStrings.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityFirstLogin.this, ActivityLoadingInfo.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                ActivityFirstLogin.this.finish();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityFirstLogin.this);
        dialog.setMessage(FinalStrings.CommonStrings.IS_EXIT).setPositiveButton(FinalStrings.CommonStrings.MAKE_SURE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityFirstLogin.this.finish();
                System.exit(0);
            }
        }).setNegativeButton(FinalStrings.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
