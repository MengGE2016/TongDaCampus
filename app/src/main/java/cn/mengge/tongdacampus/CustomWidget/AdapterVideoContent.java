package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataUriVideo;

/**
 * Created by MengGE on 2016/10/22.
 */
public class AdapterVideoContent extends ArrayAdapter<DataUriVideo> {
    int resourceId;

    public AdapterVideoContent(Context context, int resource, List<DataUriVideo> list) {
        super(context, resource, list);

        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataUriVideo uriVideo = getItem(position);
        View view;
        if (convertView == null) view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        else view = convertView;

//        ImageView imageView = (ImageView) view.findViewById(R.id.school_video_content_lv_iv);
//        imageView.setImageBitmap(uriVideo.getBitmap());
//
//        TextView titleTv = (TextView) view.findViewById(R.id.school_video_content_lv_tv);
//        titleTv.setText(uriVideo.getTitleStr());

        return view;
    }

}
