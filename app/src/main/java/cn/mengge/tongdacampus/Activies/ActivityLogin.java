package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataCookieAndCheckCodeBm;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText studentNumEd;
    private EditText studentSFZHEd;
    private EditText studentPasswordEd;
    private EditText studentCheckCodeEd;
    private ImageView checkCodeIv;
    private Button freshBt;
    private Button loginBt;
    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    private String COOKIE;

    private String studentNumStr;
    private String studentSFZHStr;
    private String studentPasswdStr;
    private String studentCheckCodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        getCheckCodeAndCookie();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        studentNumEd = (EditText) findViewById(R.id.login_student_number_ed);
        studentSFZHEd = (EditText) findViewById(R.id.login_student_id_ed);
        studentPasswordEd = (EditText) findViewById(R.id.login_student_password_ed);
        studentCheckCodeEd = (EditText) findViewById(R.id.login_student_check_code_ed);
        checkCodeIv = (ImageView) findViewById(R.id.login_check_code_iv);
        freshBt = (Button) findViewById(R.id.login_fresh_check_code_bt);
        loginBt = (Button) findViewById(R.id.login_student_login_bt);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(FinalStrings.CommonStrings.LOGIN_LOGINING);

        freshBt.setOnClickListener(this);
        loginBt.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_fresh_check_code_bt:
                getCheckCodeAndCookie();
                break;
            case R.id.login_student_login_bt:
                login();
                break;
        }
    }

    private void getCheckCodeAndCookie() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataCookieAndCheckCodeBm cookieAndCheckCodeBm = NetServer.getCookieAndCheckCodeBitmap(ActivityLogin.this, FinalStrings.NetStrings.URL_CHECK_CODE_COOKIE);
                ActivityLogin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cookieAndCheckCodeBm == null) {
                            Toast.makeText(ActivityLogin.this, R.string.login_network_not_connect_text,
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

    private void login() {
        studentNumStr = studentNumEd.getText().toString();
        studentSFZHStr = studentSFZHEd.getText().toString();
        studentPasswdStr = studentPasswordEd.getText().toString();
        studentCheckCodeStr = studentCheckCodeEd.getText().toString();

        if (studentNumStr.length() == 0) {
            Toast.makeText(ActivityLogin.this, R.string.login_student_number_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentSFZHStr.length() == 0) {
            Toast.makeText(ActivityLogin.this, R.string.login_student_sfzh_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentPasswdStr.length() == 0) {
            Toast.makeText(ActivityLogin.this, R.string.login_student_password_not_null_text, Toast.LENGTH_SHORT).show();
        } else if (studentCheckCodeStr.length() == 0) {
            Toast.makeText(ActivityLogin.this, R.string.login_student_check_code_not_null_text, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean flag = NetServer.isLoginJwglSuccess(ActivityLogin.this, FinalStrings.NetStrings.URL_JWGL_LOGIN, COOKIE, studentNumStr, studentSFZHStr, studentPasswdStr, studentCheckCodeStr);
                    ActivityLogin.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                Toast.makeText(ActivityLogin.this, FinalStrings.CommonStrings.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityLogin.this, ActivityJwglStudentInfo.class);
                                intent.putExtra("COOKIE", COOKIE);
                                startActivity(intent);
                                progressDialog.dismiss();
                                ActivityLogin.this.finish();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
