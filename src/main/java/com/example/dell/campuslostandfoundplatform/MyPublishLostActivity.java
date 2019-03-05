package com.example.dell.campuslostandfoundplatform;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPublishLostActivity extends AppCompatActivity {
    private static String username;
    private static TextView defaultInfo;
    private static ListView listView;
    private static ArticleListAdapter arrayAdapter;
    private static List<ArticleList> articleListList;
    private static ProgressBar progress;

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish_lost);

        TextView text_title=(TextView)findViewById(R.id.text_title);
        defaultInfo=(TextView)findViewById(R.id.defaultInfo);

        text_title.setText("我发布的失物招领");

        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        listView=(ListView)findViewById(R.id.listView);
        articleListList = new ArrayList<ArticleList>();

        loadSharedPreferences();
        //延迟0.1秒，让子线程（获取网络数据））加载完毕后再执行
        try
        {
            Thread.currentThread().sleep(300);//毫秒
            initListView();
        }
        catch(Exception e){}

    }
    @Override
    protected void onResume(){
        super.onResume();

    }



    private void initListView(){
        //先定义监听器，再在下面用特殊方法注册，从而实现在子线程中更新ui
        setListView();
        //item点击事件监听器
        AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MyPublishLostActivity.this,ArticleDetailActivity.class);
                intent.putExtra("title", articleListList.get(i).getTitle());
                intent.putExtra("aid", articleListList.get(i).getAid());
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(listener);
    }
    private void setListView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/listMyArticle.action";

                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("username",username);
                    builder.add("type","0");
                    RequestBody formBody = builder.build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    articleListList.clear();
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ArticleList articleList =new ArticleList(jsonObject.getString("aid"),jsonObject.getString("title")
                                ,jsonObject.getString("date"),jsonObject.getString("state"));
                        articleListList.add(articleList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    arrayAdapter=new ArticleListAdapter(MyPublishLostActivity.this,R.layout.article_list_item, articleListList);
                    UpdateGUI();
                }
            }
        }).start();
    }

    public static void UpdateGUI(){
        handler.post(RefreshLable);
    }
    private static Runnable RefreshLable=new Runnable() {
        @Override
        public void run() {
            if(articleListList.isEmpty()) {
                defaultInfo.setText("你还没有发布过哦！");
            }
            else defaultInfo.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            listView.setAdapter(arrayAdapter);
        }
    };
    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        username=sharedPreferences.getString("username","");

    }
}
