package com.newsclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hejiao on 2017/12/6.
 */

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.news_title_frag,container,false);

        RecyclerView recyclerView= view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter= new NewsAdapter(getNews());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<News> getNews(){
        List<News> mNewsList = new ArrayList<>();
        for (int i=0; i<50; i++){
            News news= new News();
            news.setTitle("This is news Title"+i);
            news.setContent(getRandomLengthContent());
            mNewsList.add(news);
        }
        return mNewsList;
    }

    private String getRandomLengthContent(){
        Random random= new Random();
        int len= random.nextInt(20);
        StringBuilder builder= new StringBuilder();
        for (int i=0; i<len; i++){
            builder.append(("This is news Content")).append(i).append(".").append("\r\n");
        }
        return builder.toString();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout)!= null){
            isTwoPane= true;    //可以找到news_content_layout时为双页模式
        }else {
            isTwoPane= false;
        }
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;

            ViewHolder(View itemView) {
                super(itemView);
                newsTitleText= itemView.findViewById(R.id.news_title);
            }
        }
        public NewsAdapter(List<News> newsList){
            this.mNewsList= newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=  LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder= new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news= mNewsList.get(holder.getAdapterPosition());
                    if (isTwoPane){
                        //如果是双页模式，则刷新NewsContentFragment
                        NewsContentFragment newsContentFragment= (NewsContentFragment)
                                getFragmentManager().findFragmentById(R.id.news_content_fragment);

                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }else {
                        //如果是单页模式，直接加载NewsContentActivity
                        NewsContentActivity.actionStart(getContext(),news.getTitle(),news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news= mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
