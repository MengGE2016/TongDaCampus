package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataGradeNameAndOthers;
import cn.mengge.tongdacampus.server.SQLServer.SQLServer;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by Administrator on 2017/3/4.
 */

public class RecyclerViewAdapterGrade extends RecyclerView.Adapter<RecyclerViewAdapterGrade.ViewHolder> {

    private List<DataGradeNameAndOthers> gradeList;
    private Context context;

    private SQLServer sqlServer;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView kcmc;
        private TextView grade;
        private TextView teacher;
        private TextView kcsx;
        private TextView xf;
        private TextView xq;

        private ImageView isPassIv;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            kcmc = (TextView)itemView.findViewById(R.id.grade_kcmc_tv);
            grade = (TextView)itemView.findViewById(R.id.grade_grade_tv);
            teacher = (TextView)itemView.findViewById(R.id.grade_teacher_tv);
            kcsx = (TextView)itemView.findViewById(R.id.grade_kcsx_tv);
            xf = (TextView)itemView.findViewById(R.id.grade_xf_tv);
            xq = (TextView)itemView.findViewById(R.id.grade_xq_tv);

            isPassIv = (ImageView) itemView.findViewById(R.id.grade_ispass_iv);
        }
    }

    public RecyclerViewAdapterGrade(List<DataGradeNameAndOthers> gradeList, SQLServer sqlServer) {
        this.gradeList = gradeList;
        this.sqlServer = sqlServer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context==null) context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_grade,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String kcmc = gradeList.get(position).getKcmc();
                String cjid = gradeList.get(position).getCjid();
                String details = sqlServer.getGradeDetails(FinalStrings.SQLServerStrings.GRADE_TABLE_NAME,
                        cjid);
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                       context);
                dialog.setTitle(kcmc)
                        .setCancelable(false)
                        .setMessage(details)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.show();
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataGradeNameAndOthers grade = gradeList.get(position);
        holder.kcmc.setText(grade.getKcmc());
        holder.grade.setText("总评成绩："+grade.getGrade());
        holder.teacher.setText("教师姓名："+grade.getTeacher());
        holder.kcsx.setText("课程属性："+grade.getKcsx());
        holder.xf.setText("学分："+grade.getXf());
        holder.xq.setText("学期："+grade.getXq());

        if (Float.valueOf(grade.getPxcj())>=75.0f)
            holder.isPassIv.setImageResource(R.drawable.course_info_green);


        if (Float.valueOf(grade.getPxcj())>=60.0f && Float.valueOf(grade.getPxcj())<75.0f)
            holder.isPassIv.setImageResource(R.drawable.course_info_yellow);


        if (Float.valueOf(grade.getPxcj())<60.0f)
            holder.isPassIv.setImageResource(R.drawable.course_info_big_red);

    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }
}
