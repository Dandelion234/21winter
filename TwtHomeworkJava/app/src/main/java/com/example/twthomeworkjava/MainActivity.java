package com.example.twthomeworkjava;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.twthomeworkjava.initBanner.BannerItem;
import com.example.twthomeworkjava.initBanner.BannerList;
import com.example.twthomeworkjava.initBanner.InitBanner;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    int countDay=1;
    BannerList bannerList;
    List<String> bannerImg=new ArrayList<>();
    List<String> bannerTitle=new ArrayList<>();
    public int m_var; //需要在消息处理中访问的成员变量，一定要声明成public
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerview;
    private MassageAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initNews();        //初始化
        initLoginInMessage();

    }


    public String GetDate(int i){
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DATE)+i);
        Date endDate=null;
            try {
            endDate = dft.parse(dft.format(calendar.getTime()));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.d("aaaaaaaa",dft.format(endDate));
            return dft.format(endDate);
    }

    public void initLoginInMessage(){
        button=findViewById(R.id.login_entrance);   //登录界面
        button.setOnClickListener(v -> {
            Intent intent =new Intent();
            intent.setClass(MainActivity.this,LoginInActivity.class);
            startActivityForResult(intent,1);
        });
    }

    public void initNews() {
        if (getSupportActionBar() !=null){      //隐藏标题栏
            getSupportActionBar().hide();
        }
            new Thread(() -> {
                try {
                    OkHttpClient clientBanner = new OkHttpClient();//新建一个OKHttp的对象
                    Request requestBanner = new Request.Builder()
                            .url("https://news-at.zhihu.com/api/3/news/hot")
                            .build();//创建一个Request对象
                    Response responseBanner = clientBanner.newCall(requestBanner).execute();//发送请求获取返回数据
                    assert responseBanner.body() != null;
                    String responseDataBanner = responseBanner.body().string();//处理返回的数据
                    Gson gson = new Gson();
                    bannerList=gson.fromJson(responseDataBanner, BannerList.class);
                        OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                        Request request = new Request.Builder()
                                .url("https://news-at.zhihu.com/api/3/news/before/"
                                +GetDate(countDay))
                                .build();//创建一个Request对象
                        Response response = client.newCall(request).execute();//发送请求获取返回数据
                        assert response.body() != null;
                        String responseData = response.body().string();//处理返回的数据

                        NewsBean newsBean=gson.fromJson(responseData,NewsBean.class);
                    showResponse(newsBean.getStories(),bannerList.getRecent());//更新ui
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
    }
    public void showResponse(List<NewsItem> response,List<BannerItem> bannerItemList){
        runOnUiThread(()->{
            for (int i=0;i<bannerItemList.size();i++){
                bannerTitle.add(bannerItemList.get(i).getTitle());
                bannerImg.add(bannerItemList.get(i).getThumbnail());
            }
            recyclerview=findViewById(R.id.recyclerview);
            layoutManager=new LinearLayoutManager(this);
            adapter=new MassageAdapter(bannerImg,bannerTitle,response,this);
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
            adapter.clear();
            initNews();
            swipeRefreshLayout.setRefreshing(false);
        }

//    private MyHandler myHandler = new MyHandler(this.getMainLooper(), this);
//
//    private static class MyHandler extends Handler {
//        WeakReference<MainActivity> mActivity;
//        public MyHandler(@NonNull Looper looper, MainActivity activity) {
//            super(looper);
//            mActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            // 处理消息...
//        }
//
//        @Override
//        public void publish(LogRecord record) {
//
//        }
//
//        @Override
//        public void flush() {
//
//        }
//
//        @Override
//        public void close() throws SecurityException {
//
//        }
//    }
}




