package com.ru.stockexchange;

import android.content.Intent;
import android.os.Bundle;

import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.ru.stockexchange.ui.main.SectionPagerAdapter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionPagerAdapter sectionsPagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        SearchView searchView = findViewById(R.id.searchViewInSearchActivity);
        searchView.onWindowFocusChanged(false);
        searchView.setOnSearchClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                String searchText = "";
                if(searchView.getQuery() != null && searchView.getQuery().length() != 0){
                    searchText = searchView.getQuery().toString();
                }
                i.putExtra("request",searchText);
                startActivity(i);
                return false;
            }
        });
    }
}


