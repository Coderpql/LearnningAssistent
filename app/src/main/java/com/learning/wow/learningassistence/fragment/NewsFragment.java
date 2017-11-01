package com.learning.wow.learningassistence.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.wow.learningassistence.R;
import com.learning.wow.learningassistence.db.News;
import com.learning.wow.learningassistence.util.HttpUtil;
import com.learning.wow.learningassistence.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wow on 2017/9/28.
 */

public class NewsFragment extends Fragment {
    private TextView mNewsTextView;
    private TextView mNewsTitleTextView;
    private ProgressDialog progressDialog;
    private List<News> mNews;
    private ImageButton refeshButton;
    private News news;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment,container,false);
        mNewsTitleTextView = (TextView)view.findViewById(R.id.news_title_view);
        mNewsTextView = (TextView)view.findViewById(R.id.news_text_view);
        refeshButton = (ImageButton)view.findViewById(R.id.refresh_button);
        news = new News();
        news.setText(" ");
        news.setTitle(" ");
        news.save();
        queryNews();
        refeshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryNews();
                mNews = DataSupport.findAll(News.class);
                news = mNews.get(0);
                String title =  news.getTitle();
                String text = news.getText();
                mNewsTitleTextView.setText(title);
                mNewsTextView.setText(text);
            }
        });
        return view;
    }

    public void update(){
        mNews = DataSupport.findAll(News.class);
        news = mNews.get(0);
        String title =  news.getTitle();
        String text = news.getText();
        mNewsTitleTextView.setText(title);
        mNewsTextView.setText(text);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 查询服务器上的每日推荐文章
     */
    private void queryNews(){
            String address = "http://www.bihengshai.cn/test/article.htm";
            queryFromServer(address);
            update();

    }
    private void queryFromServer(String address){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread（）方法回到回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                news = mNews.get(0);
                result=Utility.handleProvinceResponse(responseText,news);
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });

                }
            }
        });

    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
