package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FoundFragment extends Fragment {
    public static int MODE= Context.MODE_PRIVATE;
    public static final String PREFERENCE_NAME="SavaSetting";
    public String[] articleTypes={"全部","证件","钱包","宠物","钥匙","书本","电子产品","其他"};

    private static ListView listView;
    private static ProgressBar progress;

    private static String difDate;

    private static String nowSchool;

    private static String articleType;

    private static ArticleListAdapter articleListAdapter;
    private static ArticleRecyclerAdapter articleRecyclerAdapter;
    private static List<ArticleRecycler> articleRecyclerList;
    private static List<String> listAid;

    private static Handler handler=new Handler();

    private Button publishLost;

    private RecyclerView articleTypeView;

    private Spinner timeSpinner;

    public static FoundFragment newInstance() {
        FoundFragment fragment = new FoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FoundFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_found,container,false);
        listView=(ListView)view.findViewById(R.id.listView);

        articleRecyclerList=new ArrayList<ArticleRecycler>();
        progress=(ProgressBar)view.findViewById(R.id.progress);
        publishLost=(Button)view.findViewById(R.id.publishLost);
        articleTypeView=(RecyclerView)view.findViewById(R.id.articleType);
        timeSpinner=(Spinner)view.findViewById(R.id.timeSpinner);
        articleType="全部";
        difDate="七天内";
        listAid=new ArrayList<String>();

        loadSharedPreferences();


        initRecyclerView();
        initListView();
        initTimeSpinner();
        initPublishButton();

        return view;
    }
    private void initTimeSpinner(){
        List<String> list=new ArrayList<String>();
        list.add("七天内");
        list.add("三天内");
        list.add("当天");
        list.add("一个月内");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(arrayAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                difDate=timeSpinner.getSelectedItem().toString();
                setListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initPublishButton(){
        publishLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),PublishFoundActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView(){
        articleRecyclerList=new ArrayList<ArticleRecycler>();
        ArticleRecycler a=new ArticleRecycler(articleTypes[0],R.drawable.shape2);
        articleRecyclerList.add(a);
        ArticleRecycler a1=new ArticleRecycler(articleTypes[1],R.drawable.shape3);
        articleRecyclerList.add(a1);
        ArticleRecycler a2=new ArticleRecycler(articleTypes[2],R.drawable.shape3);
        articleRecyclerList.add(a2);
        ArticleRecycler a3=new ArticleRecycler(articleTypes[3],R.drawable.shape3);
        articleRecyclerList.add(a3);
        ArticleRecycler a4=new ArticleRecycler(articleTypes[4],R.drawable.shape3);
        articleRecyclerList.add(a4);
        ArticleRecycler a5=new ArticleRecycler(articleTypes[5],R.drawable.shape3);
        articleRecyclerList.add(a5);
        ArticleRecycler a6=new ArticleRecycler(articleTypes[6],R.drawable.shape3);
        articleRecyclerList.add(a6);
        ArticleRecycler a7=new ArticleRecycler(articleTypes[7],R.drawable.shape3);
        articleRecyclerList.add(a7);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        articleTypeView.setLayoutManager(layoutManager);
        articleRecyclerAdapter=new ArticleRecyclerAdapter(articleRecyclerList);
        articleTypeView.setAdapter(articleRecyclerAdapter);
        setRecyclerItemClick();
    }

    private void setRecyclerItemClick(){
        articleRecyclerAdapter.setOnItemClickListener(new ArticleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //将选中的item背景设置为黄色
                articleRecyclerList.get(position).setBackground(R.drawable.shape2);
//                Log.i("tag",String.valueOf(position));
                //其他的背景恢复原来的
//                Log.i("tag",String.valueOf(articleRecyclerList.size()));
                for(int i=0;i<8;i++){
//                    Log.i("tag",String.valueOf(articleRecyclerList.get(i).getBackground()));
                    if (i!=position){
                        articleRecyclerList.get(i).setBackground(R.drawable.shape3);
                    }
                }
                //更新适配器
                articleRecyclerAdapter.notifyDataSetChanged();
                //获取指定位置的类型名
                articleType=articleTypes[position];

                //重新获取listview

                setListView();

            }
        });
    }

    private void initListView(){
        //先定义监听器，再在下面用特殊方法注册，从而实现在子线程中更新ui
        setListView();
        //item点击事件监听器
        AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),ArticleDetailActivity.class);
                intent.putExtra("aid", listAid.get(i));
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(listener);
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        nowSchool=sharedPreferences.getString("nowschool","");
    }
    public static void UpdateGUI(){
        handler.post(RefreshLable);
    }
    private static Runnable RefreshLable=new Runnable() {
        @Override
        public void run() {
            progress.setVisibility(View.GONE);
            listView.setAdapter(articleListAdapter);
//            arrayListAdapter.notifyDataSetChanged();
        }
    };

    private void setListView(){
        progress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ArticleList> articleListList= new ArrayList<ArticleList>();
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/listArticle.action";

                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("school",nowSchool);
                    builder.add("articleType",articleType);
                    builder.add("type","1");
                    builder.add("difDate",difDate);
                    RequestBody formBody = builder.build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    listAid.clear();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ArticleList articleList =new ArticleList(jsonObject.getString("aid"),jsonObject.getString("title")
                                ,jsonObject.getString("date"),jsonObject.getString("state"));
                        articleListList.add(articleList);
                        listAid.add(jsonObject.getString("aid"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try
                    {
                        Thread.currentThread().sleep(100);//毫秒
                        articleListAdapter=new ArticleListAdapter(getActivity(),R.layout.article_list_item, articleListList);
                        UpdateGUI();
                    }
                    catch(Exception e){}

                }
            }
        }).start();
    }



}
