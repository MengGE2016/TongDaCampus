package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataNewsDrawerTodayClassTable;

/**
 * Created by MengGE on 2016/10/12.
 */
public class AdapterNewsDrawerClasses extends ArrayAdapter<DataNewsDrawerTodayClassTable> {
    private int resourcesId;
    private Context context;

    public AdapterNewsDrawerClasses(Context context, int resource, List<DataNewsDrawerTodayClassTable> objects) {
        super(context, resource, objects);
        this.resourcesId = resource;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataNewsDrawerTodayClassTable classNewsDrawerClass = getItem(position);

        View view = LayoutInflater.from(context).inflate(resourcesId, null);
        ;
//        if (context == null) {
//            view
//        } else {
//            view = convertView;
//        }

        TextView jieCiTv = (TextView) view.findViewById(R.id.news_drawer_layout_drawer_lv_jieci_tv);
        TextView kcmcTv = (TextView) view.findViewById(R.id.news_drawer_layout_drawer_lv_kcmc_tv);
        TextView classroomTv = (TextView) view.findViewById(R.id.news_drawer_layout_drawer_lv_jiaoshi_tv);

        jieCiTv.setText(classNewsDrawerClass.getJieCiStr());
        kcmcTv.setText(classNewsDrawerClass.getKcmcStr());
        classroomTv.setText(classNewsDrawerClass.getClassroomStr());

        return view;
    }
}
