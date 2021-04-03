package com.ru.stockexchange.ui.company_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Area;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ru.stockexchange.CompanyActivity;
import com.ru.stockexchange.Converters;
import com.ru.stockexchange.MainActivity;
import com.ru.stockexchange.R;
import com.ru.stockexchange.adapters.NewsAdapter;
import com.ru.stockexchange.api.models.CompanyNews;
import com.ru.stockexchange.api.models.HistoricalPrice;
import com.ru.stockexchange.api.models.HistoricalPrices;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.view_models.CompanyViewModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlaceHolderFragmentCompany extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TEXT_BUY_BUTTON = "Buy for";
    private Company mCompany;
    private CompanyViewModel mCompanyViewModel;
    private Cartesian mChart;
    private List<DataEntry> mChartData;
    private HistoricalPrices mHistoricalPrices;
    private MaterialButton materialButtonAllTime ;
    private MaterialButton materialButton6M ;
    private MaterialButton materialButtonM ;
    private MaterialButton materialButtonW ;
    private AnyChartView mAnyChartView;
    private int mIndex;
    public static PlaceHolderFragmentCompany newInstance(int index) {
        PlaceHolderFragmentCompany fragment = new PlaceHolderFragmentCompany();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mCompany = ((CompanyActivity) getActivity()).getCompanyToSee();
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        mIndex = index;
        View root;
        switch(index){
            case 2:
                root = inflater.inflate(R.layout.fragment_company_summary, container, false);
                if(mCompany != null) {
                    ((TextView) root.findViewById(R.id.companyCountry)).setText(mCompany.country);
                    ((TextView) root.findViewById(R.id.companyCapitalization)).setText(Converters.getCurrencySymbol(mCompany.currency) +
                            mCompany.markerCapitalization * 1000);
                    ((TextView) root.findViewById(R.id.companyWebSite)).setText(mCompany.webUrl);
                }
                break;
            case 3:
                root = inflater.inflate(R.layout.fragment_company_news, container, false);
//                Пока не удается решить проблему с API запросом, в дальнейшем обязательно это исправлю и будут показываться новости
//                RecyclerView newsView = root.findViewById(R.id.newsView);
//                newsView.setLayoutManager(new LinearLayoutManager(getContext()));
//                NewsAdapter newsAdapter = new NewsAdapter((news, position) -> {
//                });
//                newsView.setAdapter(newsAdapter);
//                mCompanyViewModel.getCompanyNews().observe(getViewLifecycleOwner(), companyNews -> {
//                    newsAdapter.setNews(companyNews);
//                });
//                mCompanyViewModel.uploadCompanyNews(mCompany);
                return inflater.inflate(R.layout.fragment_company_news, container, false);
            case 4:
                return inflater.inflate(R.layout.fragment_company_forecast, container, false);
            case 5:
                return inflater.inflate(R.layout.fragment_company_ideas, container, false);
            default:
                root = inflater.inflate(R.layout.fragment_chart_company, container, false);
                Button buyButton = root.findViewById(R.id.buttonBuyStock);
                materialButtonAllTime = root.findViewById(R.id.chartButtonAll);
                materialButton6M = root.findViewById(R.id.chartButton6M);
                materialButtonM = root.findViewById(R.id.chartButtonM);
                materialButtonW = root.findViewById(R.id.chartButtonW);
                defineChart();
                ((TextView) root.findViewById(R.id.stockPriceInCompanyInfo)).setText(Converters.getCurrencySymbol(mCompany.currency) + mCompany.price);
                assignPriceChangeTextView(root.findViewById(R.id.stockPriceChangeInCompanyInfo),mCompany);
                if (mCompany!= null) {
                    buyButton.setText(TEXT_BUY_BUTTON + " " + mCompany.price +
                            Converters.getCurrencySymbol(mCompany.currency));
                }
                buyButton.setOnClickListener(v -> {
                    mCompanyViewModel.insertCompany(mCompany);
                    startMainActivity();
                        }
                );
                mCompanyViewModel.
                        getHistoricalPrices().
                        observe(getViewLifecycleOwner(), historicalPrices -> {
                            mHistoricalPrices = historicalPrices;
                    for(int i = historicalPrices.prices.size() - 1; i >= 0; i--){
                        mChartData.add(new ValueDataEntry(
                                Converters.parseDate(historicalPrices.prices.get(i).date),historicalPrices.prices.get(i).openPrice));
                    }
                    mChart.data(mChartData);
                    ((ProgressBar)root.findViewById(R.id.progressBarChart)).setVisibility(View.INVISIBLE);
                    mAnyChartView = (AnyChartView) root.findViewById(R.id.chartView);
                    mAnyChartView.setChart(mChart);
                    defineButtonListeners();
                });

        }
        return root;
    }
    @SuppressLint("ResourceAsColor")
    private void defineButtonListeners(){
        materialButtonW.setOnClickListener(v -> {
            defineChartForNDays(7);
        });
        materialButton6M.setOnClickListener(v -> {
            defineChartForNDays(190);
        });
        materialButtonM.setOnClickListener(v -> {
            defineChartForNDays(31);
        });
        materialButtonAllTime.setOnClickListener(v -> {
            List<DataEntry> newValues = new ArrayList<>();
            for(int i = mHistoricalPrices.prices.size() - 1; i >= 0; i--){
                newValues.add(new ValueDataEntry(
                        Converters.parseDate(mHistoricalPrices.prices.get(i).date) ,mHistoricalPrices.prices.get(i).openPrice));
            }
            mChart.data(newValues);
            mAnyChartView.clear();
            mAnyChartView.setChart(mChart);
        });
    }
    private void defineChartForNDays(int N){
            List<HistoricalPrice> dataToShow = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                if( i  < mHistoricalPrices.prices.size()) {
                    dataToShow.add(mHistoricalPrices.prices.get(i));
                }
            }
            List<DataEntry> newValues = new ArrayList<>();
            for(int i = dataToShow.size() - 1; i >= 0; i--){
                newValues.add(new ValueDataEntry(
                        Converters.parseDate(dataToShow.get(i).date) ,dataToShow.get(i).openPrice));
            }
            mChart.data(newValues);
            mAnyChartView.clear();
            mAnyChartView.setChart(mChart);
    }
    @Override
    public void onStart() {
        super.onStart();
        mCompanyViewModel.uploadHistoricalPrices(mCompany);
    }

    private void defineChart () {
        mChart = AnyChart.area();
        mChartData =  new ArrayList<>();
        mChart.yAxis(false);
        mChart.xAxis(false);
        mChart.animation(true);
        mChart.tooltip().positionMode(TooltipPositionMode.POINT);
        Set set = Set.instantiate();
        Mapping series1Data = set.mapAs("{ x : 'x' }");
        Area series1 = mChart.area(series1Data);
        series1.name("$ ");
        series1.normal().stroke("#000000", 3, "0 0","bevel", "round");
        series1.hovered().stroke("#000000", 3, "0 0","bevel",  "round");
        series1.selected().stroke("#000000", 3, "0 0","bevel",  "round");
        series1.fill("['white', '#F5F5F5']", 90 );
        series1.hovered().stroke("3 #fff");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d)
                .stroke("1.5 #fff");
        series1.markers().zIndex(100d);
    }
    private void assignPriceChangeTextView (TextView textView, Company currentCompany ) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder priceChangeText = new StringBuilder();

        if(currentCompany.priceChange > 0) {
            priceChangeText.append("+");
            textView.setTextColor(Color.GREEN);
        }
        else if (currentCompany.priceChange < 0) {
            priceChangeText.append("-");
           textView.setTextColor(Color.RED);
        }

        if(currentCompany.currency != null) {
            priceChangeText.append(Converters.getCurrencySymbol(currentCompany.currency));
            priceChangeText.append(" ");
        }
        priceChangeText.append(decimalFormat.format(Math.abs(currentCompany.priceChange)));
        priceChangeText.append(" ");

        if(currentCompany.priceChange != 0) {
            priceChangeText.append("(").append(decimalFormat.format((Double)(currentCompany.priceChange / currentCompany.price) * 100)).append("%").append(")");
        }else priceChangeText.append("(0%)");
        textView.setText(priceChangeText.toString());
    }
    private void startMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
