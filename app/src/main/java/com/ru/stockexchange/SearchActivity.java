package com.ru.stockexchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ru.stockexchange.view_models.RequestViewModel;
import com.ru.stockexchange.view_models.CompanyViewModel;

public class SearchActivity extends AppCompatActivity {
    private RequestViewModel mRequestViewModel;
    private CompanyViewModel mCompanyViewModel;
    private SearchView mSearchView;
    public NavController mNavController;
    private void initializeSearchView(){
        mSearchView = findViewById(R.id.searchViewInSearchActivity);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mNavController.getCurrentDestination().getId() != R.id.resultsFragment) {
                    mNavController.navigate(R.id.action_openFragment_to_resultsFragment);
                }else if((query == null || query.length() == 0) &&
                        mNavController.getCurrentDestination().getId() != R.id.openFragment){
                    mNavController.navigate(R.id.action_resultsFragment_to_openFragment);
                }
                else {
                    mCompanyViewModel.findCompaniesByQuery(query,getApplication());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    private void initializeBackButton(){
        ImageButton buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(v -> {
            Intent i = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(i);
        });
    }
    public NavController getNavController(){
        return mNavController;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeSearchView();
        initializeBackButton();
        mNavController = Navigation.findNavController(this,R.id.nav_host);
        Intent intent = getIntent();
        if(intent.getStringExtra("request") != null){
            SearchView searchView = findViewById(R.id.searchViewInSearchActivity);
            searchView.setQuery(((CharSequence) intent.getStringExtra("request")),false);
        }

    }

}
