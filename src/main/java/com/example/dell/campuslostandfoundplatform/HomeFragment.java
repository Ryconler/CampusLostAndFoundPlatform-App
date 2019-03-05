package com.example.dell.campuslostandfoundplatform;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class HomeFragment extends Fragment {
    private TextView mySchool;
    private Button changeNowSchool;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout search;
    private static ListView listView;

    private static ThankListAdapter thankListAdapter;
    private static ProgressBar progress;

    public static int MODE= Context.MODE_PRIVATE;
    public static final String PREFERENCE_NAME="SavaSetting";

    private static Handler handler=new Handler();

    private static String nowSchool;
    private static String resultAid;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        mySchool=(TextView)view.findViewById(R.id.myShcool);
        changeNowSchool=(Button)view.findViewById(R.id.changeNowSchool);
        rl2=(RelativeLayout)view.findViewById(R.id.rl2);
        rl3=(RelativeLayout)view.findViewById(R.id.rl3);
        search=(RelativeLayout) view.findViewById(R.id.rl1);
        listView=(ListView)view.findViewById(R.id.thankList);
        progress=(ProgressBar)view.findViewById(R.id.progress);

        loadSharedPreferences();
        initRl2Rl3();
        initChangeButton();
        initSearchRL();
        setListView();
        mySchool.setText(nowSchool);
        return view;
    }

    private void initSearchRL(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText=new EditText(getActivity());
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
//                dialog.setTitle("写下你的感谢");
                dialog.setTitle("失物搜索");
                dialog.setView(editText);
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editText.getText().toString().equals("")){
                            Toast.makeText(getActivity(),"搜索内容不能为空哦",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String content=editText.getText().toString();
                            resultAid="";
                            getResultAid(content);
                            try
                            {
                                Thread.currentThread().sleep(300);//毫秒
                                if(resultAid.equals("")){
                                    Toast.makeText(getActivity(),"未找到相应失物，请再试一次",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intent=new Intent(getActivity(),ArticleDetailActivity.class);
                                    intent.putExtra("aid",resultAid);
                                    startActivity(intent);
                                }
                            }
                            catch(Exception e){}


                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });
    }
    private void getResultAid(final String content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/listArticleBySearch.action";
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("searchContent",content);
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    resultAid= jsonObject.getString("aid");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initRl2Rl3(){
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),PublishLostActivity.class);
                startActivity(intent);
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),PublishFoundActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initChangeButton(){
        changeNowSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),GetSchoolActivity.class);
                intent.putExtra("ACTION","changeNowSchool");
                startActivity(intent);
            }
        });
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
            listView.setAdapter(thankListAdapter);
//            arrayListAdapter.notifyDataSetChanged();
        }
    };
    private void setListView(){
        progress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ThankList> thankList= new ArrayList<ThankList>();
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/listThank.action";

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ThankList list =new ThankList(jsonObject.getJSONObject("userBySuid").getString("username")
                                ,jsonObject.getJSONObject("userByRuid").getString("username")
                                ,jsonObject.getString("content"),jsonObject.getString("date"));
                        thankList.add(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try
                    {
                        Thread.currentThread().sleep(100);//毫秒
                        thankListAdapter=new ThankListAdapter(getActivity(),R.layout.thank_list_item,thankList);
                        UpdateGUI();
                    }
                    catch(Exception e){}

                }
            }
        }).start();
    }

}
