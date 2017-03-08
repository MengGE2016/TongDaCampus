package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataGradeNameAndOthers;

/**
 * Created by MengGE on 2016/9/15.
 */
public class AdapterGrade extends ArrayAdapter<DataGradeNameAndOthers> {

    private int resource;

    public AdapterGrade(Context context, int textViewResourceId,
                        List<DataGradeNameAndOthers> objects) {
        super(context, textViewResourceId, objects);
        resource = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataGradeNameAndOthers gradeNameAndOthers = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, null);
        TextView kcmcTv = (TextView) view.findViewById(R.id.grade_kcmc_tv);
        TextView gradeTv = (TextView) view.findViewById(R.id.grade_grade_tv);
        TextView xqTv = (TextView) view.findViewById(R.id.grade_xq_tv);

        String KCMC = gradeNameAndOthers.getKcmc();
        String GRADE = "总评成绩：" + gradeNameAndOthers.getGrade();
        String XQ = "学期：" + gradeNameAndOthers.getXq();

        //本学期成绩学期显示蓝色
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int nextYear = thisYear + 1;
        int month = calendar.get(Calendar.MONTH) + 1;
        String xq = thisYear + "-" + nextYear + "-2";;
        if (month >= 9) xq = thisYear + "-" + nextYear + "-1";
        if (XQ.equals("学期：" + xq)) xqTv.setTextColor(Color.BLUE);
        //不及格课程显示红色
        if (Float.valueOf(gradeNameAndOthers.getPxcj()) < 60.0f) kcmcTv.setTextColor(Color.RED);
        //达到学位课程要求分数显示绿色
        if (Float.valueOf(gradeNameAndOthers.getPxcj()) >= 75.0f) kcmcTv.setTextColor(Color.parseColor("#ff1ea909"));

        kcmcTv.setText(KCMC);
        gradeTv.setText(GRADE);
        xqTv.setText(XQ);

        return view;
    }

}