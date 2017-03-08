package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataCookieAndCheckCodeBm;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityLoginJwgl extends AppCompatActivity {

    private UserLoginTask authTask = null;
    // UI references.
    private AutoCompleteTextView studentNumACTV;
    private AutoCompleteTextView studentSFZHACTV;
    private AutoCompleteTextView studentCheckCodeACTV;
    private EditText studentPasswordEd;
    private ImageView checkCodeIv;
    private Button refreshBtn;
    //    private Toolbar toolbar;
    private String cookie;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_jwgl);
        initViews();
        getCheckCodeAndCookie();
    }


    private void initViews() {
        //ToolBar
//        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
//        toolbar.setTitleTextColor(R.color.colorWhite); //设置标题颜色
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(FinalStrings.PrefServerStrings.GUEST_PREF, MODE_PRIVATE);
        editor = preferences.edit();


        //验证码图片
        checkCodeIv = (ImageView) findViewById(R.id.login_jwgl_refresh_iv);

        //刷新验证码按钮
        refreshBtn = (Button) findViewById(R.id.login_jwgl_student_refresh_check_code_btn);
        refreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckCodeAndCookie();
            }
        });

        // Set up the login form.
        studentNumACTV = (AutoCompleteTextView) findViewById(R.id.login_jwgl_student_num_actv);
        studentSFZHACTV = (AutoCompleteTextView) findViewById(R.id.login_jwgl_student_sfzh_actv);
        studentCheckCodeACTV = (AutoCompleteTextView) findViewById(R.id.login_jwgl_student_checkcode_actv);

        studentPasswordEd = (EditText) findViewById(R.id.login_jwgl_student_password_ed);
        studentPasswordEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_jwgl_student_password_visiable_btn || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

//        studentNumACTV.setText(preferences.getString(FinalStrings.PrefServerStrings.GUEST_NUM, ""));
//        studentSFZHACTV.setText(preferences.getString(FinalStrings.PrefServerStrings.GUEST_SFZH, ""));
//        studentPasswordEd.setText(preferences.getString(FinalStrings.PrefServerStrings.GUEST_PASSORD, ""));


        //登录按钮
        Button signInButton = (Button) findViewById(R.id.login_jwgl_student_sign_in_btn);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //取消按钮
        Button cancelBtn = (Button) findViewById(R.id.login_jwgl_student_cancel_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //获取Cookie和验证码
    private void getCheckCodeAndCookie() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataCookieAndCheckCodeBm dataCookieAndCheckCodeBm = NetServer.getCookieAndCheckCodeBitmap(ActivityLoginJwgl.this, FinalStrings.NetStrings.URL_CHECK_CODE_COOKIE);
                ActivityLoginJwgl.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataCookieAndCheckCodeBm == null) {
                            Toast.makeText(ActivityLoginJwgl.this, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                        } else {
                            cookie = dataCookieAndCheckCodeBm.getCookie();
                            checkCodeIv.setImageBitmap(dataCookieAndCheckCodeBm.getBitmap());
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (authTask != null) {
            return;
        }

        // Reset errors.
        studentNumACTV.setError(null);
        studentSFZHACTV.setError(null);
        studentCheckCodeACTV.setError(null);
        studentPasswordEd.setError(null);

        // Store values at the time of the login attempt.
        String number = studentNumACTV.getText().toString();
        String sfzh = studentSFZHACTV.getText().toString();
        String checkCode = studentCheckCodeACTV.getText().toString();
        String password = studentSFZHACTV.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码
        if (TextUtils.isEmpty(password)) {
            studentPasswordEd.setError("密码不能为空");
            focusView = studentPasswordEd;
            focusView.requestFocus();
            cancel = true;
        } else if (!isPasswordValid(password)) {
            studentPasswordEd.setError("输入的密码无效,请重新输入");
            focusView = studentPasswordEd;
            focusView.requestFocus();
            cancel = true;
        }


        // 检查学号
        if (TextUtils.isEmpty(number)) {
            studentNumACTV.setError("学号不能为空");
            focusView = studentNumACTV;
            focusView.requestFocus();
            cancel = true;
        } else if (!isNumValid(number)) {
            studentNumACTV.setError("输入的学号无效,请重新输入");
            focusView = studentNumACTV;
            focusView.requestFocus();
            cancel = true;
        }

        // 检查身份证号
        if (TextUtils.isEmpty(sfzh)) {
            studentSFZHACTV.setError("身份证号不能为空");
            focusView = studentSFZHACTV;
            focusView.requestFocus();
            cancel = true;
        } else if (!isSFZHValid(sfzh)) {
            studentSFZHACTV.setError("输入的身份证号无效,请重新输入");
            focusView = studentSFZHACTV;
            focusView.requestFocus();
            cancel = true;
        }

        // 检查验证码
        if (TextUtils.isEmpty(checkCode)) {
            studentCheckCodeACTV.setError("验证码不能为空");
            focusView = studentCheckCodeACTV;
            focusView.requestFocus();
            cancel = true;
        }

        if (cancel) {
            //有信息填写不正确则执行

//            focusView.requestFocus();
        } else {
            //所有信息填写正确后执行
            authTask = new UserLoginTask(number, sfzh, password, checkCode);
            authTask.execute((Void) null);
        }
    }

    //判断学号是否正确
    private boolean isNumValid(String number) {
        return number.length() >= 10;
    }

    //判断身份证号格式是否正确
    private boolean isSFZHValid(String sfzh) {
        return sfzh.length() == 18 || sfzh.length() == 15;
    }

    //判断密码格式是否正确
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    //登录教务管理系统
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String number;
        private final String password;
        private final String sfzh;
        private final String checkCode;


        private ProgressDialog progressDialog = new ProgressDialog(ActivityLoginJwgl.this);

        UserLoginTask(String number, String sfzh, String password, String checkCode) {
            this.number = number;
            this.sfzh = sfzh;
            this.password = password;
            this.checkCode = checkCode;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(FinalStrings.CommonStrings.LOGIN_LOGINING);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            return NetServer.isLoginJwglSuccess(ActivityLoginJwgl.this, FinalStrings.NetStrings.URL_JWGL_LOGIN, cookie, number, sfzh, password, checkCode);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            progressDialog.dismiss();
            if (success) {
                LogUtil.d("Cookie", cookie);
                editor.putString(FinalStrings.PrefServerStrings.GUEST_NUM, number);
                editor.putString(FinalStrings.PrefServerStrings.GUEST_SFZH, sfzh);
                editor.putString(FinalStrings.PrefServerStrings.GUEST_PASSORD, password);
                editor.putString(FinalStrings.PrefServerStrings.GUEST_COOKIE, cookie);
                editor.commit();
                Toast.makeText(ActivityLoginJwgl.this, FinalStrings.CommonStrings.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityLoginJwgl.this, ActivityOtherStudentGrade.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityLoginJwgl.this, FinalStrings.CommonStrings.LOGIN_FAILED, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
        }
    }


//    //Toolbar返回键监听
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

