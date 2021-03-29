package com.ru.stockexchange.repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.ru.stockexchange.Converters;
import com.ru.stockexchange.api.AdditionalNetworkService;
import com.ru.stockexchange.api.NetworkService;
import com.ru.stockexchange.api.models.HistoricalPrice;
import com.ru.stockexchange.api.models.HistoricalPrices;
import com.ru.stockexchange.api.models.RequestResult;
import com.ru.stockexchange.api.models.StockQuote;
import com.ru.stockexchange.database.CompanyDatabase;
import com.ru.stockexchange.database.DAO.CompanyDAO;
import com.ru.stockexchange.database.entities.Company;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyRepository {
    private CompanyDAO mCompanyDAO;
    private LiveData<List<Company>> mAllCompanies;
    private LiveData<List<Company>> mAllFavouriteCompanies;
    private NetworkService mNetworkService;
    private MutableLiveData<List<Company>> mReceivedCompanies;
    private AdditionalNetworkService mAdditionalNetworkService;
    private MutableLiveData<HistoricalPrices> mHistoricalPrices;
    private static final String START_UPDATING = "Start updating companies";
    private static final String COMPANIES_UPDATED = "Companies are updated";

    private static final String GETTING_PRICES_OF_RECEIVED_COMPANIES = "Start getting received companies price";
    private static final String FAIL_TO_LOAD_SEARCH_RESULTS = "Failed to find companies by search request";
    private static final String SHOW_RECEIVED_COMPANIES = "Сompanies are updated and assigned";

    private static final String TAG = "myLogs";
    private static final int QUERY_DELIMITER = 7;
    private static final String COMMON_STOCK_TYPE = "Common Stock";

    public CompanyRepository(Application application) {
        CompanyDatabase companyDatabase = CompanyDatabase.getDatabase(application);
        mCompanyDAO = companyDatabase.companyDAO();
        mAllCompanies = mCompanyDAO.getAll();
        mAllFavouriteCompanies = mCompanyDAO.getAllFavourite();
        mNetworkService = new NetworkService();
        mAdditionalNetworkService = new AdditionalNetworkService();
        mReceivedCompanies = new MutableLiveData<>();
        mHistoricalPrices =  new MutableLiveData<>();
    }

    public LiveData<HistoricalPrices> getHistoricalPrices() {
        return mHistoricalPrices;
    }

    public LiveData<List<Company>> getReceivedCompanies() {
        return mReceivedCompanies;
    }

    public LiveData<List<Company>> getAllCompanies() {
        return mAllCompanies;
    }

    public LiveData<List<Company>> getAllFavouriteCompanies() {
        return mAllFavouriteCompanies;
    }

    public void loadCompaniesByQuery(String query, Application application) {
        List<Company> companiesToShow = new ArrayList<>();
        ;
        Observable.create((ObservableOnSubscribe<List<Company>>) emitter ->
                mNetworkService.getJSONApi()
                        .getNewCompaniesByRequest(query)
                        .enqueue(new Callback<RequestResult>() {
                            @Override
                            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                                if (response.body() != null) {
                                    List<Company> companiesToAdd = response.body().companies;
                                    if (companiesToAdd != null && companiesToAdd.size() != 0) {
                                        companiesToShow.addAll(companiesToAdd);
                                        emitter.onNext(companiesToShow);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RequestResult> call, Throwable t) {
                                Toast.makeText(application.getApplicationContext(), "Failed to find companies by search request", Toast.LENGTH_SHORT)
                                        .show();
                                t.printStackTrace();
                            }
                        })).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<Company>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<Company> companies) {
                        List<String> keys = new ArrayList<>();
                        for (Company item : companies) {
                            if (item.type.equals(COMMON_STOCK_TYPE))
                                keys.add(item.symbol);
                        }
                        updateReceivedCompanies(keys, application);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(application.getApplicationContext(), FAIL_TO_LOAD_SEARCH_RESULTS, Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successfully load companies from search");
                    }
                });
    }

    private void updateReceivedCompanies(List<String> keys, Application application) {
        List<Company> companiesToSee = new ArrayList<>();
        List<String> responses = new ArrayList<>();
        // В API есть ограничение на количество запросов, поэтому я использую ограничитель
        for (int i = 0; i < QUERY_DELIMITER; i++) {
            if (keys.size() >= i + 1) {
                if (keys.get(i).length() != 0)
                    responses.add(keys.get(i));
            }
        }
        final int[] count = {0};
        Observable.create((ObservableOnSubscribe<Company>) emitter -> {
            for (String key : responses) {
                mNetworkService.getJSONApi()
                        .getCompanyProfileBySymbol(key)
                        .enqueue(new Callback<Company>() {
                            @Override
                            public void onResponse(Call<Company> call, Response<Company> response) {
                                Company company = response.body();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        if (company != null) {
                                            if (company.logoURL != null && company.logoURL.length() != 0) {
                                                try {
                                                    company.iconString = Converters.encodeToBase64(Picasso.with(application)
                                                            .load(company.logoURL).get());
                                                    emitter.onNext(company);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    emitter.onNext(company);
                                                }
                                            } else {
                                                emitter.onNext(company);
                                            }
                                        }
                                    }
                                }.start();
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Company> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Company>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Company company) {
                        count[0]++;
                        if (company != null && company.ticker != null && company.ticker.length() != 0) {
                            companiesToSee.add(company);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (count[0] == responses.size()) {
                            getPriceAndAssignReceivedCompanies(companiesToSee);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getPriceAndAssignReceivedCompanies(List<Company> companies) {
        List<String> quoteKeys = new ArrayList<>();
        for (Company company : companies) {
            quoteKeys.add(company.ticker);
        }
        List<Company> companiesToReturn = companies;
        final int[] count = {0};
        Observable.create((ObservableOnSubscribe<StockQuote>) emitter -> {
            for (final int[] i = {0}; i[0] < companies.size(); i[0]++) {
                mNetworkService.getJSONApi()
                        .getStockQuoteWithSymbol(companies.get(i[0]).ticker)
                        .enqueue(new Callback<StockQuote>() {

                            @Override
                            public void onResponse(Call<StockQuote> call, Response<StockQuote> response) {
                                StockQuote quote = response.body();
                                count[0]++;
                                if (quote != null) {
                                    emitter.onNext(quote);
                                }
                            }

                            @Override
                            public void onFailure(Call<StockQuote> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<StockQuote>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, GETTING_PRICES_OF_RECEIVED_COMPANIES);
                    }

                    @Override
                    public void onNext(@NonNull StockQuote stockQuote) {
                        if (stockQuote != null) {
                            companiesToReturn.get(count[0] - 1).price = stockQuote.currentPrice;
                            companiesToReturn.get(count[0] - 1).priceChange = stockQuote.currentPrice - stockQuote.openPriceOfTheDay;
                        }
                        if (count[0] >= companies.size()) {
                            mReceivedCompanies.postValue(companiesToReturn);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, SHOW_RECEIVED_COMPANIES);
                    }
                });
    }

    public void insertCompany(Company company) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mCompanyDAO.insertAll(company);
            }

        }.start();
    }

    public void updateCompaniesByNetwork(Application application) {
        List<String> items = new ArrayList<>();
        for (Company company : mAllCompanies.getValue()) {
            items.add(company.ticker);
        }
        Observable.create((ObservableOnSubscribe<Company>)
                emitter -> {
                    for (int i = 0; i < items.size(); i++) {
                        boolean isFav = false;
                        String iconString = null;
                        if (mAllCompanies.getValue().get(i).isFavourite.equals("FAV"))
                            isFav = true;
                        boolean finalIsFav = isFav;
                        if (mAllCompanies.getValue().get(i).iconString != null) {
                            iconString = mAllCompanies.getValue().get(i).iconString;
                        }
                        String finalIconString = iconString;
                        mNetworkService.getJSONApi()
                                .getCompanyProfileBySymbol(items.get(i))
                                .enqueue(new Callback<Company>() {
                                    @Override
                                    public void onResponse(Call<Company> call, Response<Company> response) {
                                        Company company = response.body();
                                        if (company != null) {
                                            if (finalIsFav) company.isFavourite = "FAV";
                                            if (finalIconString != null)
                                                company.iconString = finalIconString;
                                            emitter.onNext(company);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Company> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                    }
                }).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Company>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        Log.d(TAG, START_UPDATING);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Company company) {
                        mNetworkService.getJSONApi()
                                .getStockQuoteWithSymbol(company.ticker)
                                .enqueue(new Callback<StockQuote>() {
                                    @Override
                                    public void onResponse(Call<StockQuote> call, Response<StockQuote> response) {

                                        StockQuote quote = response.body();
                                        if (quote != null) {
                                            company.price = quote.currentPrice;
                                            company.priceChange = quote.currentPrice - quote.openPriceOfTheDay;
                                        }
                                        if (company.iconString == null) {
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    if (company.logoURL != null && company.logoURL.length() != 0) {
                                                        try {
                                                            company.iconString = Converters.encodeToBase64(Picasso.with(application)
                                                                    .load(company.logoURL).get());
                                                            updateCompany(company);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        updateCompany(company);
                                                    }
                                                }
                                            }.start();
                                        } else {
                                            updateCompany(company);
                                        }
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<StockQuote> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, COMPANIES_UPDATED);
                    }
                });
    }

    public void deleteCompany(Company company) {
        mCompanyDAO.delete(company);
    }

    public void updateCompany(Company company) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mCompanyDAO.update(company);
            }
        }.start();
    }

    public void uploadHistoricalPrices(Company company) {
        // 2010-10-10 - рандомная далекая дата, на самом деле api выдает информацию о каждом дне начиная с октября-ноября 2020
        String date = NetworkService.GetUTCdatetimeAsString();
        if(date != null || date.length() == 0) {
            mAdditionalNetworkService
                    .getJSONApi()
                    .getData(company.ticker, "2010-10-10", date)
                    .enqueue(new Callback<HistoricalPrices>() {
                        @Override
                        public void onResponse(Call<HistoricalPrices> call, Response<HistoricalPrices> response) {
                            HistoricalPrices historicalPrices = response.body();
                            if(historicalPrices != null) {
                                mHistoricalPrices.postValue(historicalPrices);
                            }
                        }

                        @Override
                        public void onFailure(Call<HistoricalPrices> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }
}
