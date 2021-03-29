package com.ru.stockexchange.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.ru.stockexchange.CompanyActivity;
import com.ru.stockexchange.R;
import com.ru.stockexchange.adapters.StockAdapter;
import com.ru.stockexchange.ConnectionHelper;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.view_models.CompanyViewModel;


import java.util.List;


public class PlaceholderFragment extends Fragment {
    private CompanyViewModel mCompanyViewModel;
    private static final String ARG_SECTION_NUMBER = "section_number";
    // Нужно ли при запуске приложения при наличии интернета сразу обновлять данные об акции
    private boolean mIsAutoUpdating = false;
    private StockAdapter mStockAdapter;
    private CoordinatorLayout mLayout;
    private RecyclerView mRecyclerView;
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompanyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mLayout = root.findViewById(R.id.mainCoordinatorLayout);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        RecyclerView mRecyclerView = root.findViewById(R.id.stockList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStockAdapter = new StockAdapter((company, position) -> {
            Intent i = new Intent(getActivity(), CompanyActivity.class);
            i.putExtra("company",company);
            startActivity(i);
        }, mCompanyViewModel);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mStockAdapter);
        switch (index) {
            case 1:
                mCompanyViewModel.getAllCompanies().observe(getViewLifecycleOwner(), companies -> {
                    mStockAdapter.setCompanies(companies);
                    if (mIsAutoUpdating) {
                        mIsAutoUpdating = false;
                        mCompanyViewModel.updateAllCompanies(getActivity().getApplication());
                        ((SwipeRefreshLayout)root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(true);
                    }else {
                        ((SwipeRefreshLayout)root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }

                });
                defineSwipeLayout(root);
                break;
            case 2:
                mCompanyViewModel.getAllFavouriteCompanies().observe(getViewLifecycleOwner(), new Observer<List<Company>>() {
                    @Override
                    public void onChanged(List<Company> companies) {
                        mStockAdapter.setCompanies(companies);
                    }
                });
                SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
                swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(),"Update stocks in 'Stocks' tab",Toast.LENGTH_LONG).show();
                });
                break;
        }
        return root;
    }
    @SuppressLint("StaticFieldLeak")
    private void defineSwipeLayout(View root){
        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(ConnectionHelper.getInstance(getContext()).isConnected()) {
                mCompanyViewModel.updateAllCompanies(getActivity().getApplication());
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(7500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }.execute();
            }else {
                Snackbar.make(mLayout,"To update companies info, turn on the internet",Snackbar.LENGTH_LONG);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
