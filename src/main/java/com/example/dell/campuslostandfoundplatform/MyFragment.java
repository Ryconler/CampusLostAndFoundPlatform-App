package com.example.dell.campuslostandfoundplatform;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyFragment extends Fragment {
    private TextView username;
    private TextView tel;
    private Button quit;
    private ImageView headImage;


    private RelativeLayout myPublishLost;
    private RelativeLayout myPublishFound;
    private RelativeLayout myInfo;
    private RelativeLayout aboutUs;
    private RelativeLayout receiveThank;

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    private String usernameData;

    private String telData;

    private Uri tempUri;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);
        loadSharedPreferences();
        if(usernameData.equals("")){
            getActivity().finish();
            Intent intent=new Intent(getActivity(),LoginOrRegisterActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
        }
        username=(TextView)view.findViewById(R.id.username);
        tel=(TextView)view.findViewById(R.id.tel);
        quit=(Button) view.findViewById(R.id.quit);
        headImage=(ImageView)view.findViewById(R.id.headImage);
        myPublishLost=(RelativeLayout)view.findViewById(R.id.myPublishLost);
        myPublishFound=(RelativeLayout)view.findViewById(R.id.myPublishFound);
        myInfo=(RelativeLayout)view.findViewById(R.id.myInfo);
        aboutUs=(RelativeLayout)view.findViewById(R.id.aboutUs);
        receiveThank=(RelativeLayout)view.findViewById(R.id.receiveThank);

        initInfo();
        initRelativeLayoutClick();
        initQuitButton();
        initImageView();
        return view;
    }




    private void initImageView(){
        headImage.setImageResource(R.drawable.boy1);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });
    }


    private void initRelativeLayoutClick(){
        myPublishLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyPublishLostActivity.class);
                startActivity(intent);
            }
        });
        myPublishFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyPublishFoundActivity.class);
                startActivity(intent);
            }
        });
        receiveThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyRecevieThank.class);
                startActivity(intent);
            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyInfoActivity.class);
                startActivity(intent);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置头像");
        String[] items = { "boy1", "boy2","boy3","girl1","girl2" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        headImage.setImageResource(R.drawable.boy1);
                        break;
                    case 1:
                        headImage.setImageResource(R.drawable.boy2);
                        break;
                    case 2:
                        headImage.setImageResource(R.drawable.boy3);
                        break;
                    case 3:
                        headImage.setImageResource(R.drawable.girl1);
                        break;
                    case 4:
                        headImage.setImageResource(R.drawable.girl2);
                        break;
                }
            }
        });
        builder.create().show();
    }


    private void initQuitButton(){
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                dialog.setTitle("确认");
                dialog.setMessage("确定退出当前账号？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearSharedPreferences();
                        Intent intent=new Intent(getActivity(),LoginOrRegisterActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(),"登出成功",Toast.LENGTH_SHORT).show();
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

    private void initInfo(){
        username.setText(usernameData);
        tel.setText("手机号："+telData);
    }

    private void clearSharedPreferences(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        usernameData=sharedPreferences.getString("username","");


        telData=sharedPreferences.getString("tel","");

    }

}
