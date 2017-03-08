package cn.mengge.tongdacampus.CustomWidget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataNewsLinkTitle;

/**
 * Created by Administrator on 2017/3/2.
 */

public class RecycleViewAdapterMainNavBroadcast extends RecyclerView.Adapter<RecycleViewAdapterMainNavBroadcast.ViewHolder> {

    private List<DataNewsLinkTitle> linkTitles;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.main_nav_broadcast_tv);
        }
    }

    public RecycleViewAdapterMainNavBroadcast(List<DataNewsLinkTitle> linkTitles) {
        this.linkTitles = linkTitles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_nav_news_broadcast,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataNewsLinkTitle newsLinkTitle = linkTitles.get(position);
        holder.title.setText(newsLinkTitle.getTitle());
    }

    @Override
    public int getItemCount() {
        return linkTitles.size();
    }
}
