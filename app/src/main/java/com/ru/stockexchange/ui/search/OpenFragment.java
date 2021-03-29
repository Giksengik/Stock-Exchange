package com.ru.stockexchange.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ru.stockexchange.R;
import com.ru.stockexchange.SearchActivity;
import com.ru.stockexchange.adapters.RequestAdapter;
import com.ru.stockexchange.database.entities.Request;
import com.ru.stockexchange.view_models.RequestViewModel;

import java.util.List;

public class OpenFragment extends Fragment {
    RequestViewModel mRequestViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empty_search, container, false);
        mRequestViewModel = new ViewModelProvider(this).get(RequestViewModel .class);
        RecyclerView popularRequests = root.findViewById(R.id.listPopularRequests);
        RecyclerView searchedRequests = root.findViewById(R.id.listSearched);
        RequestAdapter popularRequestAdapter = new RequestAdapter((company, position) -> { });
        RequestAdapter searchedRequestAdapter =  new RequestAdapter(new RequestAdapter.OnRequestClickListener() {
            @Override
            public void onRequestClick(Request request, int position) {
                SearchView searchView = (SearchView)(getActivity().findViewById(R.id.searchViewInSearchActivity));
                searchView.setQuery(request.requestString,false);

            }
        });
        popularRequests.setLayoutManager(new GridLayoutManager(getContext(), 2,LinearLayoutManager.HORIZONTAL,false));
        searchedRequests.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL,false));

        popularRequests.setAdapter(popularRequestAdapter);
        searchedRequests.setAdapter(searchedRequestAdapter);


        ((SearchView)(getActivity().findViewById(R.id.searchViewInSearchActivity))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && newText.length() != 0){
                    SearchActivity searchActivity = (SearchActivity) getActivity();
                    if(searchActivity != null) {
                        searchActivity.getNavController().navigate(R.id.action_openFragment_to_resultsFragment);
                    }
                }
                return false;
            }
        });
        mRequestViewModel.getAllRequests().observe(getViewLifecycleOwner(), new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                searchedRequestAdapter.setRequests(requests);
            }
        });
        return root;
    }

}
