package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataZbxx;

/**
 * Created by MengGE on 2016/10/14.
 */
public class AdapterZbxxClasses extends ArrayAdapter<DataZbxx> {

    private int resourcesId;
    private Context context;

    public AdapterZbxxClasses(Context context, int resource, List<DataZbxx> objects) {
        super(context, resource, objects);
        this.resourcesId = resource;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataZbxx data = getItem(position);
        View view;
        if (convertView == null) view = LayoutInflater.from(context).inflate(resourcesId, null);
        else view = convertView;

        TextView itemTitle = (TextView) view.findViewById(R.id.zbxx_title_lv_tv);
        TextView itemLink = (TextView) view.findViewById(R.id.zbxx_link_lv_tv);
        TextView itemTime = (TextView) view.findViewById(R.id.zbxx_time_lv_tv);

        itemTitle.setText(data.getItemTitleStr());
        itemLink.setText(data.getItemLinkStr());
        itemTime.setText(data.getItemTimeStr());


        return view;
    }
}
