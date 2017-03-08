package cn.mengge.tongdacampus.Activies;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityReQuestion extends AppCompatActivity implements View.OnClickListener {
    private EditText requestionEd;
    private Button submitBtn;

    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_question);

        initViews();
    }

    //初始化控件并添加监听事件
    private void initViews() {
        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor(FinalStrings.ColorStrins.COLOR_WHITE)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestionEd = (EditText) findViewById(R.id.requestion_ed);
        submitBtn = (Button) findViewById(R.id.requestion_submit_bt);
        submitBtn.setOnClickListener(ActivityReQuestion.this);

        sendFilter = new IntentFilter();
        sendFilter.addAction(FinalStrings.ActionStrings.SEMD_MESSAGE);
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver, sendFilter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.requestion_submit_bt) {
            final String questionStr = requestionEd.getText().toString();
            if (questionStr.length() != 0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityReQuestion.this);
                dialog.setTitle("提示").setMessage("该操作将会发送一普通短信给作者(15706299377),确定发送?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendMessage(questionStr);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } else Toast.makeText(ActivityReQuestion.this, "您还没有输入内容", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendMessage(String message) {
        SmsManager smsManager = SmsManager.getDefault();
        Intent sendIntent = new Intent(FinalStrings.ActionStrings.SEMD_MESSAGE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityReQuestion.this, 0, sendIntent, 0);
        smsManager.sendTextMessage(FinalStrings.AuthorInfoStrings.TEL, null, message, pendingIntent, null);
    }

    private class SendStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK) {
                Toast.makeText(ActivityReQuestion.this, "信息已发送,感谢您的反馈", Toast.LENGTH_SHORT).show();
                requestionEd.setText("");
            } else Toast.makeText(ActivityReQuestion.this, "信息发送失败", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendStatusReceiver);
    }
}
