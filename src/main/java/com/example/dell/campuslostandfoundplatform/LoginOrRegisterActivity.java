package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class LoginOrRegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private TextView casual;
    private static ProgressBar progress;

    private static Handler handler=new Handler();

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    private static String school;
    private static String qq;
    private static String tel;
    private static int uid;
    private static String usernameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        loadSharedPreferences();
        if(!usernameData.equals("")){
            finish();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }


        TextView title=(TextView)findViewById(R.id.text_title);
        title.setText("登录");
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        casual=(TextView)findViewById(R.id.casual);
        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        initLoginButton();
        initRegisterButton();
        initCasual();


    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        usernameData=sharedPreferences.getString("username","");
    }

    private void initCasual(){
        casual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action=motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        Intent intent=new Intent(LoginOrRegisterActivity.this,GetSchoolActivity.class);
                        intent.putExtra("ACTION","casual");
                        startActivity(intent);
                }
                return false;
            }
        });
    }

    private void initRegisterButton(){
        //注册按钮
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginOrRegisterActivity.this,GetSchoolActivity.class);
                intent.putExtra("ACTION","register");
                startActivity(intent);
            }
        });
    }

    private void initLoginButton(){
        //登陆按钮监听器
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("")||password.getText().toString().equals("")){
                    Toast.makeText(LoginOrRegisterActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result=false;
                            try{
                                String usr=username.getText().toString();
                                String psw=password.getText().toString();

                                OkHttpClient client=new OkHttpClient();

                                String url="http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/login.action";

                                FormBody.Builder builder = new FormBody.Builder();
                                builder.add("username",usr);
                                builder.add("password",psw);
                                RequestBody formBody = builder.build();

                                Request request=new Request.Builder()
                                        .url(url)
                                        .post(formBody)
                                        .build();
                                Response response=client.newCall(request).execute();
                                String responseData=response.body().string();
                                result=parseJsonWithJsonObject(responseData);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {

                                if(result){
                                    UpdateGUI();
                                    saveSharedPreferences();
                                    Intent intent=new Intent(LoginOrRegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    UpdateGUI();
                                    Looper.prepare();
                                    Toast.makeText(LoginOrRegisterActivity.this,"密码错误或用户名不存在",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt("uid",uid);
        editor.putString("username",username.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.putString("school",school);
        editor.putString("nowschool",school);
        editor.putString("qq",qq);
        editor.putString("tel",tel);
        editor.commit();
    }

    private boolean parseJsonWithJsonObject(String jsonData){
        Boolean result=false;
        try{
            JSONArray jsonArray=new JSONArray(jsonData);

//            for (int i=0;i<jsonArray.length();i++){
//                JSONObject jsonObject=jsonArray.getJSONObject(i);
//                result=jsonObject.getBoolean("result");
//            }
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            uid=jsonObject.getInt("uid");
            result=jsonObject.getBoolean("result");
            school=jsonObject.getString("school");
            qq=jsonObject.getString("qq");
            tel=jsonObject.getString("tel");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void UpdateGUI(){
        handler.post(RefreshLable);
    }
    private static Runnable RefreshLable=new Runnable() {
        @Override
        public void run() {
            progress.setVisibility(View.INVISIBLE);
        }
    };
}
