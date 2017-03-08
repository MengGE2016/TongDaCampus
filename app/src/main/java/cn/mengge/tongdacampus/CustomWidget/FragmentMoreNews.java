package cn.mengge.tongdacampus.CustomWidget;

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

import cn.mengge.tongdacampus.Activies.ActivityMoreNewsView;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;

/**
 * Created by MengGE on 2016/10/23.
 */
public class FragmentMoreNews extends Fragment {

    private List<Data3Strings1IntTLTF> list;

    public List<Data3Strings1IntTLTF> getList() {
        return list;
    }

    public void setList(List<Data3Strings1IntTLTF> list) {
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.fragment_list_view, null);

        final ListView listView = (ListView) view.findViewById(R.id.fragment_list_view_lv);
        listView.setAdapter(new Adapter3TextViewForNews(getActivity(), R.layout.custom_listview_3textview_for_news, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ActivityMoreNewsView.class);
                intent.putExtra("SubTitle", list.get(position).getTitleStr());
                intent.putExtra("Link", "http://news.ntu.edu.cn" + list.get(position).getLinkStr());
                intent.putExtra("Flag", list.get(position).getFlag());
                startActivity(intent);
            }
        });

        return view;
    }
}
