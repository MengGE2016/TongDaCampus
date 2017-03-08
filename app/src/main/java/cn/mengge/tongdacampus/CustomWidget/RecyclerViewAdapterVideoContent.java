package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mengge.tongdacampus.Activies.ActivitySchoolVideoShow;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataUriVideo;

/**
 * Created by MengGE on 2017/3/1.
 */

public class RecyclerViewAdapterVideoContent extends RecyclerView.Adapter<RecyclerViewAdapterVideoContent.ViewHolder> {


    private Context context;
    private List<DataUriVideo> videoList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            imageView = (ImageView)itemView.findViewById(R.id.school_video_content_lv_iv);
            textView = (TextView)itemView.findViewById(R.id.school_video_content_lv_tv);
        }
    }


    public RecyclerViewAdapterVideoContent(List<DataUriVideo> videoList) {
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null) context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_view_school_video, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DataUriVideo video = videoList.get(position);
                Intent intent = new Intent(v.getContext(), ActivitySchoolVideoShow.class);
                intent.putExtra("title", video.getTitleStr());
                intent.putExtra("URI", "http://news.ntu.edu.cn"+video.getVideoUrl());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataUriVideo video = videoList.get(position);
        Glide.with(context).load("http://news.ntu.edu.cn"+video.getImageUrl()).into(holder.imageView);
        holder.textView.setText(video.getTitleStr());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
