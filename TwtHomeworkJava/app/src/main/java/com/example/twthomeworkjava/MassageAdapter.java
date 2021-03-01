package com.example.twthomeworkjava;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.twthomeworkjava.initBanner.BannerItem;
import com.example.twthomeworkjava.initBanner.InitBanner;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MassageAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<NewsItem> newsItemList;
    List<String> bannerImg;
    private Context context;
    Onclick onclick;
    List<String> bannerTitle;
    public MassageAdapter(List<String> bannerImg, List<String> bannerTitle,List<NewsItem> newsItemList, Context context) {
        this.bannerImg = bannerImg;
        this.bannerTitle = bannerTitle;
        this.newsItemList=newsItemList;
        this.context = context;
    }


    public interface Onclick {
        void click(String str);
    }

    public void setOnclick(Onclick aclick) {
        this.onclick = aclick;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType == Constants.TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.banner_main, parent, false);
            return new HeadHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.massage_item, parent, false);
            return new ViewHolderNormal(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position==0) {
            bindViewHolderHead((HeadHolder) holder);
        } else {
            bindViewHolderNormal((ViewHolderNormal) holder, position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constants.TYPE_HEAD;
        }
        else {
            return Constants.TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return newsItemList.size()+1;
    }




    public static class HeadHolder extends RecyclerView.ViewHolder {
        Banner banner;
        public HeadHolder(View view) {
            super(view);
            banner = itemView.findViewById(R.id.banner);
        }
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView img;

        public ViewHolderNormal(View view) {
            super(view);
            title = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.image);
        }
    }

    private void bindViewHolderNormal(ViewHolderNormal viewHolderNormal, int position) {
        NewsItem newsItem=newsItemList.get(position-1);
        viewHolderNormal.title.setText(newsItem.getTitle());
        Glide.with(context).load(newsItem.getImages().get(0)).into(viewHolderNormal.img);
        viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.click(newsItem.getUrl());
            }
        });
    }

    private void bindViewHolderHead(HeadHolder viewHolderHead) {

        viewHolderHead.banner.setImageLoader(new ImageLoadBanner())
                .setBannerTitles(bannerTitle)
                .setDelayTime(2000)   //更换时间
                .isAutoPlay(true)    //自动播放
                .setIndicatorGravity(BannerConfig.CENTER)    //位置
                .setBannerAnimation(Transformer.Accordion)   //动画效果
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)  //样式
                .setImages(bannerImg)
                .start();
    }

    public void addFirstAll(List<NewsItem> list) {
        newsItemList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        newsItemList.clear();
        notifyDataSetChanged();
    }
//
//    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
//    public void updateList(List<Recent> newDatas, boolean hasMore) {
//        // 在原有的数据之上增加新数据
//        if (newDatas != null) {
//            massageList.addAll(newDatas);
//        }
//        this.hasMore = hasMore;
//        notifyDataSetChanged();
//    }
//
//
}
    class ImageLoadBanner extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .into(imageView);
            //imageView.setImageResource(Integer.parseInt(path.toString()));
        }
    }





