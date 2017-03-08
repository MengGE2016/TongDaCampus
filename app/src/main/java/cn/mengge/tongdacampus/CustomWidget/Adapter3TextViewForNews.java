package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;

/**
 * Created by MengGE on 2016/10/23.
 */
public class Adapter3TextViewForNews extends ArrayAdapter<Data3Strings1IntTLTF> {

    private int resourceId;

    public Adapter3TextViewForNews(Context context, int resource, List<Data3Strings1IntTLTF> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data3Strings1IntTLTF moreNews = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else view = convertView;

        TextView titleTv = (TextView) view.findViewById(R.id.more_news_title_lv_tv);
        TextView linkTv = (TextView) view.findViewById(R.id.more_news_link_lv_tv);
        TextView timeTv = (TextView) view.findViewById(R.id.more_news_time_lv_tv);

        titleTv.setText(moreNews.getTitleStr());
        linkTv.setText(moreNews.getLinkStr());
        timeTv.setText(moreNews.getTimeStr());
        return view;
    }
}
