package cn.mengge.tongdacampus.Activies;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.mengge.tongdacampus.CustomWidget.CustomDialogImageAndTwoButtons;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.FileServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityAboutUS extends AppCompatActivity implements View.OnClickListener {
    private Button aboutMeBt;               //关于我按钮--那个软件图片
    private Button ntuWeiXinBt;             //微信按钮
    private Button ntuWeiBoBt;              //微博按钮
    private Button ntuSchoolCalendarBt;     //校历按钮
//    private Button clearCacheBtn;           //清空缓存
    private TextView versionTv;             //显示软件版本号
    private ProgressDialog progressDialog;  //加载图片的对话框
    private Toolbar toolbar;                //标题栏

    private FileServer fileServer;          //文件服务--存取文件

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            final Bitmap bitmap = (Bitmap) msg.obj;
            if (bitmap != null) {
                CustomDialogImageAndTwoButtons.Builder dialog = new CustomDialogImageAndTwoButtons.Builder(ActivityAboutUS.this);
                progressDialog.dismiss();
                switch (msg.what) {
                    //微信
                    case FinalStrings.NetStrings.FLAG_WE_CHAT:
                        dialog.setTitle(FinalStrings.NetStrings.NTU_WE_CHAT_IMG_DIALOG_TITLE).setBitmap((Bitmap) msg.obj).setStoreButton(FinalStrings.CommonStrings.STORE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //保存图片到SD卡,IMG_DIR为图片的保存路径, 文件名, 图片类型Bitmap
                                fileServer.saveImageFile2SD(FinalStrings.FileSreverStrings.IMAGE_DIR, FinalStrings.NetStrings.NTU_WE_CHAT_IMG_FILE_NAME, bitmap);
                                Toast.makeText(ActivityAboutUS.this, FinalStrings.CommonStrings.STORE_SUCCESS, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).setCancelButton(FinalStrings.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                        break;
                    //微博
                    case FinalStrings.NetStrings.FlAG_SINA_WB:
                        dialog.setTitle(FinalStrings.NetStrings.NTU_SINA_WB_IMG_DIALOG_TITLE).setBitmap((Bitmap) msg.obj).setStoreButton(FinalStrings.CommonStrings.STORE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fileServer.saveImageFile2SD(FinalStrings.FileSreverStrings.IMAGE_DIR, FinalStrings.NetStrings.NTU_SINA_WB_IMG_FILE_NAME, bitmap);
                                Toast.makeText(ActivityAboutUS.this, FinalStrings.CommonStrings.STORE_SUCCESS, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).setCancelButton(FinalStrings.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();


                        break;
                    //校历
                    case FinalStrings.NetStrings.FLAG_SCHOOL_CALENDAR:
                        dialog.setTitle(FinalStrings.NetStrings.NTU_SCHOOL_CAL_IMG_DIALOG_TITLE).setBitmap((Bitmap) msg.obj).setStoreButton(FinalStrings.CommonStrings.STORE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fileServer.saveImageFile2SD(FinalStrings.FileSreverStrings.IMAGE_DIR, FinalStrings.NetStrings.NTU_SCHOOL_CAL_IMG_FILE_NAME, bitmap);
                                Toast.makeText(ActivityAboutUS.this, FinalStrings.CommonStrings.STORE_SUCCESS, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).setCancelButton(FinalStrings.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                        break;
                }
            } else
                Toast.makeText(ActivityAboutUS.this, FinalStrings.CommonStrings.GET_CONTENT_FAILED, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initViews();
    }

    /**
     * 初始化控件
     **/
    private void initViews() {
        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ProgressDialog
        progressDialog = new ProgressDialog(ActivityAboutUS.this);
        progressDialog.setMessage(FinalStrings.CommonStrings.PLEASE_WAITE);

        //Button
        aboutMeBt = (Button) findViewById(R.id.about_us_bt);
        ntuWeiXinBt = (Button) findViewById(R.id.about_us_ntu_weixin_bt);
        ntuWeiBoBt = (Button) findViewById(R.id.about_us_ntu_weibo_bt);
        ntuSchoolCalendarBt = (Button) findViewById(R.id.about_us_ntu_school_calendar_bt);
//        clearCacheBtn = (Button) findViewById(R.id.about_us_clear_cache_bt);
        //为Button添加监听事件
        aboutMeBt.setOnClickListener(ActivityAboutUS.this);
        ntuWeiXinBt.setOnClickListener(ActivityAboutUS.this);
        ntuWeiBoBt.setOnClickListener(ActivityAboutUS.this);
        ntuSchoolCalendarBt.setOnClickListener(ActivityAboutUS.this);
//        clearCacheBtn.setOnClickListener(ActivityAboutUS.this);

        //TextView
        versionTv = (TextView) findViewById(R.id.about_us_version_tv);
        versionTv.setText(FinalStrings.CommonStrings.VERSION + getVersionName());

        //文件服务
        fileServer = new FileServer(ActivityAboutUS.this);

    }

    //按钮的监听事件处理
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_us_bt: //弹出对话框显示作者信息
                AlertDialog.Builder about_me_dl = new AlertDialog.Builder(
                        ActivityAboutUS.this);
                about_me_dl.setTitle(FinalStrings.CommonStrings.AUTHOR_INFO);
                about_me_dl
                        .setMessage(FinalStrings.CommonStrings.AUTHOR_INFO_CONTENT);
                about_me_dl.setPositiveButton(FinalStrings.CommonStrings.MAKE_SURE,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                about_me_dl.setCancelable(false);
                AlertDialog dialog = about_me_dl.create();
                dialog.show();
                break;

            //开启线程发起网络请求图片并发送到Handler进行处理
            case R.id.about_us_ntu_weixin_bt:
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取图片--微信二维码
                        Bitmap bitmap = NetServer.getBitmap(FinalStrings.NetStrings.URL_WE_CHAT_IMG);
                        Message message = new Message();
                        message.what = FinalStrings.NetStrings.FLAG_WE_CHAT;
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            //开启线程发起网络请求图片并发送到Handler进行处理
            case R.id.about_us_ntu_weibo_bt:
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取图片--微博二维码
                        Bitmap bitmap = NetServer.getBitmap(FinalStrings.NetStrings.URL_SINA_WB_LMG);
                        Message message = new Message();
                        message.what = FinalStrings.NetStrings.FlAG_SINA_WB;
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            //开启线程发起网络请求图片并发送到Handler进行处理
            case R.id.about_us_ntu_school_calendar_bt:
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取图片--校历图片
                        Bitmap bitmap = NetServer.getBitmap(FinalStrings.NetStrings.URL_SCHOOL_CALENDAR_IMG_2017_1);
                        Message message = new Message();
                        message.what = FinalStrings.NetStrings.FLAG_SCHOOL_CALENDAR;
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }
                }).start();
                break;

//
//            case R.id.about_us_clear_cache_bt:
//                applyLimit();
//
//                FileServer fileServer = new FileServer(ActivityAboutUS.this);
//
//
//
//                break;
        }
    }

    //检查申请权限
    private void applyLimit(){
//        if (ContextCompat.checkSelfPermission(ActivityNews.this, Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(ActivityNews.this, new String[]{Manifest.permission.INTERNET},1);
//        }
        if (ContextCompat.checkSelfPermission(ActivityAboutUS.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ActivityAboutUS.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    //获取软件的当前版本号
    private String getVersionName() {
        PackageManager packageManager = ActivityAboutUS.this.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(
                    ActivityAboutUS.this.getPackageName(), 0);
            String version = info.versionName;

            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
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
