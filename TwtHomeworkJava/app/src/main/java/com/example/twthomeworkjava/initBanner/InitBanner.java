package com.example.twthomeworkjava.initBanner;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InitBanner {
    List<BannerItem> bannerItems;
    List<String> bannerImg=new ArrayList<>();
    List<String> bannerTitle=new ArrayList<>();
    BannerList bannerList;
    public void initBanner() {
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
                bannerList=gson.fromJson(responseData, BannerList.class);

                bannerItems=bannerList.getRecent();
                for (int i=0;i<bannerItems.size();i++){
                    bannerTitle.add(bannerItems.get(i).getTitle());
                    bannerImg.add(bannerItems.get(i).getThumbnail());
                }
                //showResponse(bannerList.getBannerItems());//更新ui
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public List<String> getBannerImg() {
        return bannerImg;
    }

    public List<String> getBannerTitle() {
        return bannerTitle;
    }
}
