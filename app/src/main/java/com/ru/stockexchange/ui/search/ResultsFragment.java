package com.ru.stockexchange.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ru.stockexchange.CompanyActivity;
import com.ru.stockexchange.R;
import com.ru.stockexchange.SearchActivity;
import com.ru.stockexchange.adapters.StockAdapter;
import com.ru.stockexchange.database.entities.Request;
import com.ru.stockexchange.view_models.RequestViewModel;
import com.ru.stockexchange.view_models.CompanyViewModel;

import java.util.ArrayList;

public class ResultsFragment extends Fragment {
    private CompanyViewModel mCompanyViewModel;
    private RequestViewModel mRequestViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_result_search, container, false);

        mCompanyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        mRequestViewModel = new ViewModelProvider(this).get(RequestViewModel .class);

        RecyclerView receivedStocksRecyclerView = root.findViewById(R.id.listOfResponsibleCompanies);
        ProgressBar progressBar = root.findViewById(R.id.progressBarSearch);

        progressBar.setVisibility(View.INVISIBLE);

        StockAdapter receivedStocksAdapter = new StockAdapter((company, position) -> {
            SearchView searchView =((SearchView)(getActivity().findViewById(R.id.searchViewInSearchActivity)));
            mRequestViewModel.addRequest(new Request(searchView.getQuery().toString()));
            Intent i = new Intent(getActivity(), CompanyActivity.class);
            i.putExtra("company",company);
            startActivity(i);
        }, mCompanyViewModel);

        receivedStocksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        receivedStocksRecyclerView.setAdapter(receivedStocksAdapter);
        mCompanyViewModel.getResponsibleCompanies().observe(getViewLifecycleOwner(), companies -> {
            progressBar.setVisibility(View.INVISIBLE);
            if(companies != null && companies.size() != 0) {
                receivedStocksAdapter.setCompanies(companies);
            }
        });
        ((SearchView)(getActivity().findViewById(R.id.searchViewInSearchActivity))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query == null || query.length() == 0){
                    SearchActivity searchActivity = (SearchActivity) getActivity();

                    if(searchActivity != null) {
                        searchActivity.getNavController().navigate(R.id.action_resultsFragment_to_openFragment);
                    }
                }else{
                    TextView searchHint = root.findViewById(R.id.searchHint);
                    searchHint.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    receivedStocksAdapter.setCompanies(new ArrayList<>());
                    mCompanyViewModel.findCompaniesByQuery(query, getActivity().getApplication());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.length() == 0) {
                    SearchActivity searchActivity = (SearchActivity) getActivity();
                    if (searchActivity != null) {
                        searchActivity.getNavController().navigate(R.id.action_resultsFragment_to_openFragment);
                    }
                }
                return true;
            }
        });
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
