package cn.mengge.tongdacampus.CustomWidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.mengge.tongdacampus.Activies.ActivityWebViewer;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/10/3.
 */
public class FragmentJwglListView extends Fragment {

    private ListView listView;
    private Adapter3TextViewForNews adapter;
    private List<Data3Strings1IntTLTF> newsLinkTitles;
    private Context context;

    public List<Data3Strings1IntTLTF> getNewsLinkTitles() {
        return newsLinkTitles;
    }

    public void setNewsLinkTitles(List<Data3Strings1IntTLTF> newsLinkTitles) {
        this.newsLinkTitles = newsLinkTitles;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_list_view_lv);
        adapter = new Adapter3TextViewForNews(getActivity(), R.layout.custom_listview_3textview_for_news, newsLinkTitles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, ActivityWebViewer.class);
                String title = newsLinkTitles.get(position).getTitleStr();
                String link = newsLinkTitles.get(position).getLinkStr();
                intent.putExtra("LINK", link);
                intent.putExtra("CONTENT", 0);
                intent.putExtra("SubTitle", title);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
