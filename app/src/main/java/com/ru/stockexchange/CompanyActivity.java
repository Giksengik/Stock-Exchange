package com.ru.stockexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.ui.company_info.SectionsPagerAdapterCompany;
import com.ru.stockexchange.ui.main.SectionPagerAdapter;
import com.ru.stockexchange.view_models.CompanyViewModel;

public class CompanyActivity extends AppCompatActivity {
    private Company mCompanyToSee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        CompanyViewModel companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        if(getIntent().getSerializableExtra("company") != null)
            mCompanyToSee = (Company) getIntent().getSerializableExtra("company");
        SectionsPagerAdapterCompany sectionsPagerAdapter = new SectionsPagerAdapterCompany(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_company);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabsCompany);
        ImageButton imageButtonIsFavouriteCompany = ((ImageButton) findViewById(R.id.imageButtonIsFavouriteCompany));
        if(mCompanyToSee.isFavourite.equals("FAV")){
            imageButtonIsFavouriteCompany.setBackgroundResource(R.drawable.button_favourite_stock);
        }else imageButtonIsFavouriteCompany.setBackgroundResource(R.drawable.button_is_favourite_company);
        tabs.setupWithViewPager(viewPager);
        ((TextView) findViewById(R.id.companyTicker)).setText(mCompanyToSee.ticker);
        ((TextView) findViewById(R.id.companyName)).setText(mCompanyToSee.name);
        ((ImageButton) findViewById(R.id.backButtonCompany)).setOnClickListener((View.OnClickListener) v -> {
            Intent i = new Intent(CompanyActivity.this, MainActivity.class);
            startActivity(i);
        });
        imageButtonIsFavouriteCompany.setOnClickListener((View.OnClickListener) v -> {
            if(mCompanyToSee.isFavourite.equals("NOTFAV")){
                mCompanyToSee.isFavourite = "FAV";
                imageButtonIsFavouriteCompany.setBackgroundResource(R.drawable.button_favourite_stock);
            }
            else {
                mCompanyToSee.isFavourite = "NOTFAV";
                imageButtonIsFavouriteCompany.setBackgroundResource(R.drawable.button_is_favourite_company);
            }
            companyViewModel.updateCompany(mCompanyToSee);

        });

    }
    public Company getCompanyToSee(){
        return mCompanyToSee;
    }
}
