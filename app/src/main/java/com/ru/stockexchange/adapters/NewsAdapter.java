package com.ru.stockexchange.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ru.stockexchange.R;
import com.ru.stockexchange.api.models.CompanyNews;
import com.ru.stockexchange.database.entities.Request;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<CompanyNews> mAllNews = new ArrayList<>();
    private OnNewsClickListener mOnClickListener;
    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_in_list, parent, false);
        return new NewsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyNews currentNews = mAllNews.get(position);
        holder.newsTitle.setText(currentNews.getHeadline());
        holder.newsSummary.setText(currentNews.getSummary());
        holder.itemView.setOnClickListener(v -> mOnClickListener.onNewsClick(currentNews,position));
    }

    public NewsAdapter(NewsAdapter.OnNewsClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
    public interface OnNewsClickListener{
        void onNewsClick(CompanyNews news, int position);
    }
    public void setNews(List<CompanyNews> news){
        mAllNews = news;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mAllNews.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView newsTitle;
        final TextView newsSummary;
        ViewHolder(View v){
            super(v);
            newsTitle = v.findViewById(R.id.newsTtitle);
            newsSummary = v.findViewById(R.id.newsSummary);
        }
    }
}
