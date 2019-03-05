package com.example.dell.campuslostandfoundplatform;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;





public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;
    private LostFragment mLostFragment;
    private FoundFragment mFoundFragment;
    private HomeFragment mHomeFragment;
    private MyFragment mMyFragment;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=(TextView)findViewById(R.id.text_title);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        init();


    }
    private void init() {
        //要先设计模式后再添加图标！
        //设置按钮模式  MODE_FIXED表示固定   MODE_SHIFTING表示转移
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBarBackgroundColor("#1ccbae");
        //设置背景风格
        // BACKGROUND_STYLE_STATIC表示静态的
        //BACKGROUND_STYLE_RIPPLE表示涟漪的，也就是可以变化的 ，跟随setActiveColor里面的颜色变化
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.setActiveColor("#ed4255")  //背景颜色
                .setInActiveColor("#F5F5F5")        //未选中颜色
                .setBarBackgroundColor("#ffffff");  //选中颜色

        bottomNavigationBar.setTabSelectedListener(this);

        //添加并设置图标、图标的颜色和文字
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.homeicon, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.losticon, "失物招领"))
                .addItem(new BottomNavigationItem(R.drawable.foundicon, "寻物启事"))
                .addItem(new BottomNavigationItem(R.drawable.myicon, "我的"))
                .setFirstSelectedPosition(0)
                .initialise();

        setDefaultFragment();
    }
    private void setDefaultFragment() {

        title.setText("首页");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mHomeFragment = HomeFragment.newInstance();
        transaction.replace(R.id.layFrame, mHomeFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {

        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, mHomeFragment);
                title.setText("首页");
                break;
            case 1:
                if (mLostFragment == null) {
                    mLostFragment = LostFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, mLostFragment);
                title.setText("失物招领");
                break;
            case 2:
                if (mFoundFragment == null) { ;
                    mFoundFragment = FoundFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, mFoundFragment);
                title.setText("寻物启事");
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                transaction.replace(R.id.layFrame, mMyFragment);
                title.setText("我的");
                break;

            default:
                break;
        }

        transaction.commit();// 事务提交
    }
    @Override
    public void onTabUnselected(int position) {

    }

    /**
     * 设置释放Fragment 事务
     */
    @Override
    public void onTabReselected(int position) {

    }


}
