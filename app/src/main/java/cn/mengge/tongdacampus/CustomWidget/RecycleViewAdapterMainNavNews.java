package cn.mengge.tongdacampus.CustomWidget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataNewsLinkTitle;
import cn.mengge.tongdacampus.server.NetServer;
import cn.mengge.tongdacampus.server.StringServer;

/**
 * Created by MengGe on 2017/3/1.
 */

public class RecycleViewAdapterMainNavNews extends RecyclerView.Adapter<RecycleViewAdapterMainNavNews.ViewHolder> {

    private static final int UPDTAE_CONTENT = 1;

    private Context context;
    private List<DataNewsLinkTitle> newsList;
    // 五种颜色的背景
    private int[] background = {
            R.drawable.course_info_blue, R.drawable.course_info_deep_teal,
            R.drawable.course_info_green, R.drawable.course_info_red,
            R.drawable.course_info_oreagen, R.drawable.course_info_yellow,
            R.drawable.course_info_black, R.drawable.course_info_big_red,
            R.drawable.course_info_purple, R.drawable.course_info_gray
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDTAE_CONTENT:
                    String contentHtml =(String)((Map<String,Object>)msg.obj).get("contentHtml");
                    LogUtil.d("contentHtml",String.valueOf(contentHtml));


                    ViewHolder holder = (ViewHolder)((Map<String,Object>)msg.obj).get("ViewHolder");
                    String content = StringServer.getNewsContent(contentHtml);
                    LogUtil.d("content",String.valueOf(content));

                    Spanned spanned = Html.fromHtml(content);
//                    progressDialog.dismiss();
                    holder.progressBar.setVisibility(View.GONE);
                    holder.contentTv.setText(spanned);

                  break;
                default:

            }
        }
    };



//    private ProgressDialog progressDialog;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView titleTv;
//        TextView linkTv;
        TextView timeTv;
        TextView contentTv;
        ProgressBar progressBar;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            titleTv = (TextView)view.findViewById(R.id.main_nav_recv_news_title_tv);
//            linkTv = (TextView)view.findViewById(R.id.main_nav_recv_news_link_tv);
            timeTv = (TextView)view.findViewById(R.id.main_nav_recv_news_time_tv);
            contentTv = (TextView)view.findViewById(R.id.main_nav_recv_news_content_tv);
            progressBar = (ProgressBar)view.findViewById(R.id.main_nav_load_content_prob);
        }
    }

    public RecycleViewAdapterMainNavNews(List<DataNewsLinkTitle> newsList){

        this.newsList = newsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.main_nav_news_cardview_layout, parent, false);
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("正在获取内容......");
        ViewHolder holder = new ViewHolder(view);
//        int backgroundRandom = (int) (Math.random() * 10 - 1);
//        view.setBackgroundResource(background[backgroundRandom]);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                DataNewsLinkTitle news = newsList.get(position);
//                String link =  news.getLink();
//                String title = news.getTitle();
//                Intent intent = new Intent(v.getContext(), ActivityMoreNewsView.class);
//                intent.putExtra("SubTitle", title);
//                intent.putExtra("Link", "http://news.ntu.edu.cn" + link);
//                intent.putExtra("Flag", 0);
//                v.getContext().startActivity(intent);
////                if (position >= 6 && position <= 11) {
////                    intent.putExtra("Link", "http://www.ntu.edu.cn" + link);
////                    intent.putExtra("SubTitle", title);
////                    intent.putExtra("Flag", FinalStrings.NetStrings.NEWS_TZGG);
////                    v.getContext().startActivity(intent);
////                } else {
////
////                    intent.putExtra("Link", link);
////                    intent.putExtra("SubTitle", title);
////                    intent.putExtra("Flag", FinalStrings.NetStrings.NEWS_TDXW);
////                    v.getContext().startActivity(intent);
////                }
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.d("position",String.valueOf(position));
        DataNewsLinkTitle news = newsList.get(position);
        LogUtil.d("newsLink",news.getLink());
        holder.titleTv.setText(news.getTitle());
//        holder.linkTv.setText(news.getLink());

        holder.timeTv.setText(news.getTime());

        getContent("http://www.ntu.edu.cn"+news.getLink(),holder);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    private void getContent(final String urlStr, final ViewHolder holder){
//        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contentHtml = NetServer.getHtml(urlStr);
                LogUtil.d("contentHtml",String.valueOf(contentHtml));
                Map<String,Object> map=new HashMap<>();
                map.put("contentHtml",contentHtml);
                map.put("ViewHolder",holder);

                Message message = new Message();
                message.what = UPDTAE_CONTENT;
                message.obj = map;
                handler.sendMessage(message);
            }
        }).start();
    }

}
