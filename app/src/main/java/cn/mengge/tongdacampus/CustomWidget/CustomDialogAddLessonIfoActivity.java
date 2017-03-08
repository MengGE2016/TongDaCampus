package cn.mengge.tongdacampus.CustomWidget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CpuUsageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import cn.mengge.tongdacampus.Activies.ActivityClassTable;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

public class CustomDialogAddLessonIfoActivity extends AppCompatActivity {

    private Button monBtn;
    private Button thuBtn;
    private Button wedBtn;
    private Button tueBtn;
    private Button friBtn;
    private Button satBtn;
    private Button sunBtn;

    private Spinner beginSp;
    private Spinner endSp;

    private AutoCompleteTextView kcmcACTV;
    private AutoCompleteTextView teacherACTV;
    private AutoCompleteTextView weeksACTV;
    private AutoCompleteTextView classroomACTV;

    private Button positiveBtn;
    private Button negativeBtn;

    private String week;
    private int begin = 1;
    private int end = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_add_lesson_ifo_activity);

        initViews();
    }

    private void initViews() {

        monBtn = (Button) findViewById(R.id.add_lesson_week_1_btn);
        thuBtn = (Button) findViewById(R.id.add_lesson_week_2_btn);
        wedBtn = (Button) findViewById(R.id.add_lesson_week_3_btn);
        tueBtn = (Button) findViewById(R.id.add_lesson_week_4_btn);
        friBtn = (Button) findViewById(R.id.add_lesson_week_5_btn);
        satBtn = (Button) findViewById(R.id.add_lesson_week_6_btn);
        sunBtn = (Button) findViewById(R.id.add_lesson_week_7_btn);
        positiveBtn = (Button) findViewById(R.id.add_lesson_positive_btn);
        negativeBtn = (Button) findViewById(R.id.add_lesson_negative_btn);

        monBtn.setOnClickListener(new ButtonOnClickListener());
        thuBtn.setOnClickListener(new ButtonOnClickListener());
        wedBtn.setOnClickListener(new ButtonOnClickListener());
        tueBtn.setOnClickListener(new ButtonOnClickListener());
        friBtn.setOnClickListener(new ButtonOnClickListener());
        satBtn.setOnClickListener(new ButtonOnClickListener());
        sunBtn.setOnClickListener(new ButtonOnClickListener());
        positiveBtn.setOnClickListener(new ButtonOnClickListener());
        negativeBtn.setOnClickListener(new ButtonOnClickListener());


        beginSp = (Spinner) findViewById(R.id.add_lesson_jieci_begin_sp);
        endSp = (Spinner) findViewById(R.id.add_lesson_jieci_end_sp);
        String[] jieciStrs = new String[12];
        for (int index = 1; index <= 12; index++) {
            jieciStrs[index - 1] = "第" + index + "节";
        }
        beginSp.setAdapter(new ArrayAdapter<>(CustomDialogAddLessonIfoActivity.this, android.R.layout.simple_spinner_dropdown_item, jieciStrs));
        endSp.setAdapter(new ArrayAdapter<>(CustomDialogAddLessonIfoActivity.this, android.R.layout.simple_spinner_dropdown_item, jieciStrs));
        beginSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        endSp.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());

        kcmcACTV = (AutoCompleteTextView) findViewById(R.id.add_lesson_kcmc_actv);
        teacherACTV = (AutoCompleteTextView) findViewById(R.id.add_lesson_teacher_actv);
        weeksACTV = (AutoCompleteTextView) findViewById(R.id.add_lesson_weeks_actv);
        classroomACTV = (AutoCompleteTextView) findViewById(R.id.add_lesson_classroom_actv);
    }

    //Spinner监听事件
    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            switch (adapterView.getId()) {
                case R.id.add_lesson_jieci_begin_sp:
                    begin = position + 1;
                    break;
                case R.id.add_lesson_jieci_end_sp:
                    end = position + 1;
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    //Button监听事件
    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_lesson_week_1_btn:
                    setOtherButtonBackground(1);
                    week = "1";
                    break;
                case R.id.add_lesson_week_2_btn:
                    setOtherButtonBackground(2);
                    week = "2";
                    break;
                case R.id.add_lesson_week_3_btn:
                    setOtherButtonBackground(3);
                    week = "3";
                    break;
                case R.id.add_lesson_week_4_btn:
                    setOtherButtonBackground(4);
                    week = "4";
                    break;
                case R.id.add_lesson_week_5_btn:
                    setOtherButtonBackground(5);
                    week = "5";
                    break;
                case R.id.add_lesson_week_6_btn:
                    setOtherButtonBackground(6);
                    week = "6";
                    break;
                case R.id.add_lesson_week_7_btn:
                    setOtherButtonBackground(7);
                    week = "7";
                    break;
                case R.id.add_lesson_positive_btn:
                    if (addLesson2SQL()) {
                        Toast.makeText(CustomDialogAddLessonIfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("IsSuccess", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                case R.id.add_lesson_negative_btn:
                    finish();
                    break;
            }
        }
    }

    //添加课程信息到数据库中
    private boolean addLesson2SQL() {
        if (week == null) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "请选择星期", Toast.LENGTH_SHORT).show();
            return false;
        }

        String kcmc = kcmcACTV.getText().toString();
        String teacher = teacherACTV.getText().toString();
        String weeks = weeksACTV.getText().toString();
        String classroom = classroomACTV.getText().toString();

        if (kcmc.indexOf(" ") != -1) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "请不要填写空格符号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (teacher.indexOf(" ") != -1) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "请不要填写空格符号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (weeks.indexOf(" ") != -1) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "请不要填写空格符号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (classroom.indexOf(" ") != -1) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "请不要填写空格符号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (kcmc.length() == 0) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "课程名称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (teacher.length() == 0) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "教师姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (weeks.length() == 0) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "上课周次不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (classroom.length() == 0) {
            Toast.makeText(CustomDialogAddLessonIfoActivity.this, "教室称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }


        String lessonInfo = kcmc + " [" + teacher + " (" + weeks + "周) " + classroom + "]";
        SharedPreferences preferences = getSharedPreferences(FinalStrings.PrefServerStrings.STUDENT_PREF, MODE_PRIVATE);
        String sqlName = preferences.getString(FinalStrings.PrefServerStrings.STUDENT_SQL_NAME, FinalStrings.PrefServerStrings.STUDENT_SQL_NAME_DEFAULT);
        SQLServer sqlServer = new SQLServer(CustomDialogAddLessonIfoActivity.this, sqlName, FinalStrings.SQLServerStrings.SQL_VERSION);
        sqlServer.saveLessonInfo2SQL(FinalStrings.SQLServerStrings.CLASS_TABLE_NAME, week, begin, end, lessonInfo);

        return true;
    }

    //设置选择的星期的按钮的背景颜色
    private void setOtherButtonBackground(int selected) {
        switch (selected) {
            case 1:

                monBtn.setBackgroundResource(R.color.colorGreenBlack);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 2:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorGreenBlack);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 3:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorGreenBlack);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 4:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorGreenBlack);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 5:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorGreenBlack);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 6:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorGreenBlack);
                sunBtn.setBackgroundResource(R.color.colorHalfTransparent);
                break;

            case 7:
                monBtn.setBackgroundResource(R.color.colorHalfTransparent);
                thuBtn.setBackgroundResource(R.color.colorHalfTransparent);
                wedBtn.setBackgroundResource(R.color.colorHalfTransparent);
                tueBtn.setBackgroundResource(R.color.colorHalfTransparent);
                friBtn.setBackgroundResource(R.color.colorHalfTransparent);
                satBtn.setBackgroundResource(R.color.colorHalfTransparent);
                sunBtn.setBackgroundResource(R.color.colorGreenBlack);
                break;
        }
    }
}
