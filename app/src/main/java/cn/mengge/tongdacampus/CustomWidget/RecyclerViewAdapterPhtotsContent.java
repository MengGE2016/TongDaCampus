package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataUriVideo;

/**
 * Created by Administrator on 2017/3/8.
 */

public class RecyclerViewAdapterPhtotsContent extends RecyclerView.Adapter<RecyclerViewAdapterPhtotsContent.ViewHolder> {

    private List<DataUriVideo> photoList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView;
            imageView = (ImageView)itemView.findViewById(R.id.school_video_content_lv_iv);
            titleTv = (TextView) itemView.findViewById(R.id.school_video_content_lv_tv);
        }
    }

    public RecyclerViewAdapterPhtotsContent(List<DataUriVideo> photoList) {
        this.photoList = photoList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_view_school_video, parent, false);


        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataUriVideo photoUrl = photoList.get(position);
        Glide.with(context).load("http://news.ntu.edu.cn"+photoUrl.getImageUrl()).into(holder.imageView);
        holder.titleTv.setText(photoUrl.getTitleStr());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
