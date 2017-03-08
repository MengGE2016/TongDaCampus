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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import cn.mengge.tongdacampus.Activies.ActivityZbxxContentView;
import cn.mengge.tongdacampus.R;
import cn.mengge.tongdacampus.classes.DataZbxx;

/**
 * Created by MengGE on 2016/10/14.
 */
public class FragmentZbxx extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private List<DataZbxx> lists;
    private Context context;

    public List<DataZbxx> getLists() {
        return lists;
    }

    public void setLists(List<DataZbxx> lists) {
        this.lists = lists;
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
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_list_view_lv);
        adapter = new AdapterZbxxClasses(getActivity(), R.layout.custom_listview_zbxx, lists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, ActivityZbxxContentView.class);
                intent.putExtra("CONTENT_FLAG", lists.get(position).getItemFlag());
                intent.putExtra("TITLE", lists.get(position).getItemTitleStr());
                intent.putExtra("LINK", lists.get(position).getItemLinkStr());
                startActivity(intent);
            }
        });
        return view;
    }
}
