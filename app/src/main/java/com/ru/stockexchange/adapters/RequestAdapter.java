package com.ru.stockexchange.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.ru.stockexchange.R;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.database.entities.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<Request> mAllRequests = new ArrayList<>();
    private OnRequestClickListener mOnClickListener;
    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_in_list, parent, false);
        return new RequestAdapter.ViewHolder(itemView);
    }
    public RequestAdapter(RequestAdapter.OnRequestClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
    public interface OnRequestClickListener{
        void onRequestClick(Request request, int position);
    }
    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        Request currentRequest = mAllRequests.get(position);
        holder.requestText.setText(currentRequest.requestString);
        holder.itemView.setOnClickListener(v -> mOnClickListener.onRequestClick(currentRequest,position));
    }
    public void setRequests(List<Request> requests){
        mAllRequests = requests;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mAllRequests.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView requestText;
        ViewHolder(View v){
            super(v);
            requestText = v.findViewById(R.id.requestInListText);
        }
    }
}
