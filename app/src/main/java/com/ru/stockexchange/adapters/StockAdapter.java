package com.ru.stockexchange.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.Edits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ru.stockexchange.Converters;
import com.ru.stockexchange.R;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.view_models.CompanyViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    public List<Company> mCompanies = new ArrayList<>();
    private final StockAdapter.OnStockClickListener mOnClickListener;
    private final CompanyViewModel mCompanyViewModel;
    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_in_list, parent, false);
        return new ViewHolder(itemView);
    }
    public StockAdapter(StockAdapter.OnStockClickListener onClickListener, CompanyViewModel companyViewModel){
        this.mOnClickListener = onClickListener;
        this.mCompanyViewModel = companyViewModel;
    }
    public interface OnStockClickListener{
        void onStockClick(Company company, int position);
    }
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
        Company currentCompany = mCompanies.get(position);

        if(position % 2 == 0) holder.stockBlock.setBackgroundResource(R.drawable.stock_in_list_corner);
        else holder.stockBlock.setBackgroundResource(R.drawable.stock_in_list_corner2);

        holder.ticker.setText(currentCompany.ticker);

        if(currentCompany.name != null) {
            if (currentCompany.name.length() > 20)
                holder.companyName.setText(currentCompany.name.substring(0, 20) + "...");
            else holder.companyName.setText(currentCompany.name);
        }else holder.companyName.setText("");

        if (currentCompany.isFavourite.equals("FAV"))
            holder.buttonFavourite.setImageResource(R.drawable.button_favourite_stock);
        else holder.buttonFavourite.setImageResource(R.drawable.button_not_favourite_stock);

        holder.buttonFavourite.setOnClickListener(v -> {
            if(currentCompany.isFavourite.equals("FAV")) {
                currentCompany.isFavourite = "NOTFAV";
                mCompanyViewModel.updateCompany(currentCompany);
            }
            else {
                currentCompany.isFavourite = "FAV";
                mCompanyViewModel.updateCompany(currentCompany);
            }
            notifyItemChanged(position);
        });

        holder.itemView.setOnClickListener(v -> mOnClickListener.onStockClick(currentCompany,position));

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder priceChange = new StringBuilder();

        if(currentCompany.priceChange > 0) {
            priceChange.append("+");
            holder.stockPriceChange.setTextColor(Color.GREEN);
        }
        else if (currentCompany.priceChange < 0) {
            priceChange.append("-");
            holder.stockPriceChange.setTextColor(Color.RED);
        }

        if(currentCompany.currency != null) {
            priceChange.append(Converters.getCurrencySymbol(currentCompany.currency));
            priceChange.append(" ");
        }
            priceChange.append(decimalFormat.format(Math.abs(currentCompany.priceChange)));
            priceChange.append(" ");

        if(currentCompany.priceChange != 0) {
            priceChange.append("(").append(decimalFormat.format((Double)(currentCompany.priceChange / currentCompany.price) * 100)).append("%").append(")");
        }else priceChange.append("(0%)");
        holder.stockPriceChange.setText(priceChange.toString());
        if(currentCompany.currency != null){
            holder.stockPrice.setText(Converters.getCurrencySymbol(currentCompany.currency)+" " + currentCompany.price);
        } else holder.stockPrice.setText(" " + currentCompany.price);

        if(currentCompany.iconString != null) holder.companyIcon.setImageBitmap(Converters.decodeBase64(currentCompany.iconString));
        else holder.companyIcon.setImageResource(R.drawable.undefined_stock_icon);
        holder.companyIcon.setClipToOutline(true);
    }
    public void setCompanies(List<Company> companies){
        if(mCompanies != null && mCompanies.size() != 0){
            TreeMap<Integer,Company> companiesToAssign = new TreeMap<>();
            for (int i = 0; i < companies.size(); i++) {
                if(mCompanies.size() > i){
                    if(mCompanies.get(i).price != companies.get(i).price || mCompanies.get(i).priceChange != companies.get(i).priceChange){
                        companiesToAssign.put(i, companies.get(i));
                    }
                }else{
                    companiesToAssign.put(i, companies.get(i));
                }
            }
            for (int nextNum : companiesToAssign.keySet()) {
                if (mCompanies.size() > nextNum) {
                    mCompanies.set(nextNum, companiesToAssign.get(nextNum));
                    notifyItemChanged(nextNum);
                } else {
                    mCompanies.add(companiesToAssign.get(nextNum));
                    notifyDataSetChanged();
                }
            }
        }else{
            mCompanies = companies;
            notifyDataSetChanged();
        }
    }
    public List<Company> getData(){
        return mCompanies;
    }
    @Override
    public int getItemCount() {
        return mCompanies.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView companyIcon;
        final TextView ticker , companyName, stockPrice, stockPriceChange;
        final ImageButton buttonFavourite;
        final ConstraintLayout stockBlock;
        ViewHolder(View v){
            super(v);
            stockBlock = v.findViewById(R.id.stockInListBlock);
            buttonFavourite = v.findViewById(R.id.imageButtonIsStockFavourite);
            companyIcon = v.findViewById(R.id.companyInListIcon);
            ticker = v.findViewById(R.id.stockInListTicker);
            companyName = v.findViewById(R.id.companyNameInList);
            stockPrice = v.findViewById(R.id.stockPriceInList);
            stockPriceChange = v.findViewById(R.id.stockPriceChangeInList);
        }
    }
}
