package cn.mengge.tongdacampus.Activies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.CustomWidget.CustomDialogImageEditextActivity;
import cn.mengge.tongdacampus.CustomWidget.RecyclerViewAdapterGrade;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.CustomWidget.AdapterGrade;
import cn.mengge.tongdacampus.classes.DataGradeNameAndOthers;
import cn.mengge.tongdacampus.server.JsonServer;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class ActivityGradeQuery extends AppCompatActivity implements View.OnClickListener {

//    private ListView gradeLv;
    private ProgressDialog progressDialog;

    private List<DataGradeNameAndOthers> gradeList;
    private List<DataGradeNameAndOthers> decGradeList;
    private SQLServer sqlServer;
    private Toolbar toolbar;
    private String COOKIE;
//    private AdapterGrade adapterGrade;

    private SharedPreferences preferences;

//    private FloatingActionButton refreshFab;

    private RecyclerView gradeRecv;
    private RecyclerViewAdapterGrade recyclerViewAdapterGrade;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityGradeQuery.this,2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_quey);
        init();

//        getGradeJsonDataAndStroreDB();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar_tl);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("PERSONAL_PREF", MODE_PRIVATE);

//        gradeLv = (ListView) findViewById(R.id.grade_list_lv);
        gradeRecv = (RecyclerView) findViewById(R.id.grade_list_recv);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在查询成绩......");
        progressDialog.show();

//        refreshFab = (FloatingActionButton) findViewById(R.id.grade_query_fresh_fab);
//        refreshFab.setOnClickListener(ActivityGradeQuery.this);

        sqlServer = new SQLServer(ActivityGradeQuery.this, preferences.getString("student_personal_db", "0"), FinalStrings.SQLServerStrings.SQL_VERSION);
        gradeList = sqlServer.getGradeNameFromSQL(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME);
        decGradeList = new ArrayList<>();
        for (int i = gradeList.size() - 1; i >= 0; i--) {
            decGradeList.add(gradeList.get(i));
        }

//        adapterGrade = new AdapterGrade(ActivityGradeQuery.this, R.layout.custom_listview_grade, decGradeList);
//        gradeLv.setAdapter(adapterGrade);

        recyclerViewAdapterGrade = new RecyclerViewAdapterGrade(decGradeList,sqlServer);
        gradeRecv.setLayoutManager(gridLayoutManager);
        gradeRecv.setAdapter(recyclerViewAdapterGrade);

        toolbar.setSubtitle("你一共有" + gradeList.size() + "门成绩");

        progressDialog.dismiss();
//        gradeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String kcmc = decGradeList.get(position).getKcmc();
//                String cjid = decGradeList.get(position).getCjid();
//                String details = sqlServer.getGradeDetails(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME,
//                        cjid);
//                AlertDialog.Builder dialog = new AlertDialog.Builder(
//                        ActivityGradeQuery.this);
//                dialog.setTitle(kcmc)
//                        .setCancelable(false)
//                        .setMessage(details)
//                        .setPositiveButton("确定",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                dialog.show();
//            }
//        });

    }

    private void getGradeJsonDataAndStroreDB() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String jsonDataStr = NetServer.getGradeJsonData(COOKIE);

                ActivityGradeQuery.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (jsonDataStr.length() != 0) {
                            List<DataGrade> list = JsonServer.parseGradeTableData(sqlServer.getSQLTotalCount(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME), jsonDataStr);
                            if (list.size() != 0) {
                                sqlServer.addGrade2GradeTable(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME, list);
                                gradeList.clear();
                                decGradeList.clear();
                                gradeList = sqlServer.getGradeNameFromSQL(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME);
                                for (int i = gradeList.size() - 1; i >= 0; i--) {
                                    decGradeList.add(gradeList.get(i));
                                }
                                recyclerViewAdapterGrade.notifyDataSetChanged();
//                                gradeLv.setAdapter(new AdapterGrade(ActivityGradeQuery.this, R.layout.custom_listview_grade, gradeList));
                            }
                        } else
                            Toast.makeText(ActivityGradeQuery.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 1:
                if (requestCode == RESULT_OK){
                    gradeList.clear();
                    gradeList = sqlServer.getGradeNameFromSQL(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME);
                    toolbar.setSubtitle("你一共有" + gradeList.size() + "门成绩");
                    decGradeList.clear();
                    for (int i = gradeList.size() - 1; i >= 0; i--) {
                        decGradeList.add(gradeList.get(i));
                    }
                    recyclerViewAdapterGrade.notifyDataSetChanged();
                }
        }

    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.grade_query_fresh_fab) {
//            Intent intent = new Intent(ActivityGradeQuery.this, CustomDialogImageEditextActivity.class);
//            intent.putExtra("function", 2);
////            startActivity(intent);
//            startActivityForResult(intent,1);
//        }
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