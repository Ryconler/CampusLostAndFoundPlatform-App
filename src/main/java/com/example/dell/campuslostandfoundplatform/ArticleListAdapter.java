package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleListAdapter extends ArrayAdapter {
    private final int resourceId;

    public ArticleListAdapter(Context context, int textViewResourceId, List<ArticleList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleList articleList = (ArticleList)getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView title = (TextView) view.findViewById(R.id.title);//获取该布局内的文本视图
        TextView difDate = (TextView) view.findViewById(R.id.difDate);
        TextView hasFound=(TextView)view.findViewById(R.id.hasFound);

        title.setText(articleList.getTitle());//为文本视图设置文本内容
        difDate.setText(articleList.getDifTime());
        if(articleList.getState().equals("0")) hasFound.setVisibility(View.GONE);
        return view;

    }
}
