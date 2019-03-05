package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ThankListAdapter extends ArrayAdapter {
    private final int resourceId;

    public ThankListAdapter(Context context, int textViewResourceId, List<ThankList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThankList thankList = (ThankList) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView sUsername = (TextView) view.findViewById(R.id.sUsername);//获取该布局内的文本视图
        TextView rUsername = (TextView) view.findViewById(R.id.rUsername);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView difTime=(TextView)view.findViewById(R.id.difDate);


        sUsername.setText(thankList.getsUsername());//为文本视图设置文本内容
        rUsername.setText(thankList.getrUsername());
        content.setText(thankList.getContent());
        difTime.setText(thankList.getDifTime());

        return view;

    }
}
