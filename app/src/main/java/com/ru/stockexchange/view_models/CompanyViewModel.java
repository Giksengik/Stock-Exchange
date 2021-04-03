package com.ru.stockexchange.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ru.stockexchange.api.models.CompanyNews;
import com.ru.stockexchange.api.models.HistoricalPrices;
import com.ru.stockexchange.repositories.CompanyRepository;
import com.ru.stockexchange.database.entities.Company;

import java.util.List;

public class CompanyViewModel extends AndroidViewModel {
    private CompanyRepository mCompanyRepository;
    private LiveData<List<Company>> mAllCompanies;
    private LiveData<List<Company>> mAllFavouriteCompanies;

    public CompanyViewModel(Application application) {
        super(application);
        mCompanyRepository = new CompanyRepository(application);
        mAllCompanies = mCompanyRepository.getAllCompanies();
        mAllFavouriteCompanies = mCompanyRepository.getAllFavouriteCompanies();
    }
    public LiveData<List<Company>> getAllCompanies(){
        return mAllCompanies;
    }
    public LiveData<List<Company>> getAllFavouriteCompanies(){
        return mAllFavouriteCompanies;
    }
    public void insertCompany(Company company) { mCompanyRepository.insertCompany(company); }
    public void updateAllCompanies(Application application){
        new Thread(){
            @Override
            public void run() {
                super.run();
                mCompanyRepository.updateCompaniesByNetwork(application);
            }
        }.start();

    }
    public void updateCompany(Company company) {
        mCompanyRepository.updateCompany(company);
    }
    public LiveData<List<Company>> getResponsibleCompanies() {
        return mCompanyRepository.getReceivedCompanies();
    }
    public void findCompaniesByQuery(String query, Application application){
        new Thread(){
            @Override
            public void run() {
                super.run();
                mCompanyRepository.loadCompaniesByQuery(query, application);
            }
        }.start();
    }
    public void deleteCompany(Company company){
        mCompanyRepository.deleteCompany(company);
    }
    public LiveData<HistoricalPrices> getHistoricalPrices() { return mCompanyRepository.getHistoricalPrices();}
    public void uploadHistoricalPrices(Company company){
        mCompanyRepository.uploadHistoricalPrices(company);
    }
    public LiveData<List<CompanyNews>> getCompanyNews(){ return mCompanyRepository.getCompanyNews();}

    public void uploadCompanyNews(Company company){ mCompanyRepository.uploadCompanyNews(company);}
}
