package com.example.twthomeworkjava;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerview;
    private MassageAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button;
    JsonRootBean jsonRootBean;
    public int m_var; //需要在消息处理中访问的成员变量，一定要声明成public
    MyHandler handler=new MyHandler(Looper.myLooper(),this);
    static class MyHandler extends Handler {
        WeakReference<MainActivity> mactivity;
        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, MainActivity activity) {
            super(looper);//调用父类的显式指明的构造函数
            mactivity = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity nactivity = mactivity.get();
            if (nactivity == null)
                return;//avtivity都没了还处理个XXX
            if (msg.what == 0) {//在这里通过nactivity引用外部类
                nactivity.m_var = 0;
            }
        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() !=null){      //隐藏标题栏
            getSupportActionBar().hide();
        }
        sendRequestWithOkHttp();        //初始化
        button=findViewById(R.id.login_entrance);   //登录界面
        button.setOnClickListener(v -> {
            Intent intent =new Intent();
            intent.setClass(MainActivity.this,LoginInActivity.class);
            startActivityForResult(intent,1);
        });
    }


    private void sendRequestWithOkHttp() {
            new Thread(() -> {
                try {
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    Request request = new Request.Builder()
                            .url("https://news-at.zhihu.com/api/3/news/hot")
                            .build();//创建一个Request对象
                    Response response = client.newCall(request).execute();//发送请求获取返回数据
                    assert response.body() != null;
                    String responseData = response.body().string();//处理返回的数据
                    Gson gson = new Gson();
                    jsonRootBean = gson.fromJson(responseData, JsonRootBean.class);
                    showResponse(jsonRootBean.getRecent());//更新ui
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

    }

    private void showResponse(List<Recent> response){
        runOnUiThread(() -> {
            recyclerview=findViewById(R.id.recyclerview);      //配置recyclerview适配器
            layoutManager=new LinearLayoutManager(this);
            adapter=new MassageAdapter(response,this);
            adapter.setOnclick(new MassageAdapter.Onclick() {
                @Override
                public void click(String str) {
                    Intent intent=new Intent(MainActivity.this,ContentMessage.class);
                    intent.putExtra("url",str);
                    startActivity(intent);
                }
            });
            swipeRefreshLayout=findViewById(R.id.refresh);
            swipeRefreshLayout.setOnRefreshListener(this);
            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setAdapter(adapter);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //登录后将按钮显示为已登录
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK){    //运用intent传递数据
            assert data != null;
            String date=data.getStringExtra("LoginIn");
            button.setText(date);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new MyHandler(getMainLooper(),this).postDelayed(() -> {
            adapter.clear();
            sendRequestWithOkHttp();
            adapter.addFirstAll(jsonRootBean.getRecent());
            swipeRefreshLayout.setRefreshing(false);
            },2000);
        }


}




