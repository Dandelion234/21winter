package com.example.twthomeworkjava;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private final List<Recent> massageList;
    Context context;
    Onclick onclick;
    private final int TYPE_HEAD = 0;
    private final int TYPE_NORMAL = 1;

    public interface Onclick {
        void click(String str);
    }

    public void setOnclick(Onclick aclick) {
        this.onclick = aclick;
    }

        public MassageAdapter(List<Recent> massageList, Context context) {
        this.massageList = massageList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.massage_item, parent, false);
            return new ViewHolderNormal(view);
        } else if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.banner_main, parent, false);
            return new ViewHolderHead(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            bindViewHolderHead((ViewHolderHead) holder);
        } else {

            bindViewHolderNormal((ViewHolderNormal) holder, position);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_HEAD) {
            return TYPE_HEAD;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return massageList.size();
    }

    public static class ViewHolderHead extends RecyclerView.ViewHolder {
        Banner banner;

        public ViewHolderHead(View itemView) {
            super(itemView);
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
        Recent massage = massageList.get(position - 1);
        viewHolderNormal.title.setText(massage.getTitle());
        Glide.with(context).load(massage.getThumbnail()).into(viewHolderNormal.img);
        viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.click(massage.getUrl());
            }
        });
    }

    private void bindViewHolderHead(ViewHolderHead viewHolderHead) {
        List<String> bannerImg = new ArrayList<>();
        List<String> bannerText = new ArrayList<>();
        for (int i = 0; i < massageList.size(); i++) {
            bannerText.add(massageList.get(i).getTitle());
            bannerImg.add(massageList.get(i).getThumbnail());
        }


        viewHolderHead.banner.setImageLoader(new ImageLoadBanner())
                .setBannerTitles(bannerText)
                .setDelayTime(2000)   //更换时间
                .isAutoPlay(true)    //自动播放
                .setIndicatorGravity(BannerConfig.CENTER)    //位置
                .setBannerAnimation(Transformer.Accordion)   //动画效果
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)  //样式
                .setImages(bannerImg)
                .start();

    }

    public void addFirstAll(List<Recent> list) {
        massageList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        massageList.clear();
        notifyDataSetChanged();
    }




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





