package com.ru.stockexchange.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.database.entities.Request;
import com.ru.stockexchange.repositories.RequestRepository;

import java.util.List;

public class RequestViewModel extends AndroidViewModel {
    private RequestRepository mRequestRepository;
    private LiveData<List<Request>> mAllRequests;
    public RequestViewModel(@NonNull Application application) {
        super(application);
        mRequestRepository = new RequestRepository(application);
        mAllRequests = mRequestRepository.getSearchedRequests();
    }
    public LiveData<List<Request>> getAllRequests(){
        return mAllRequests;
    }
    public void addRequest(Request request) {
        mRequestRepository.addRequest(request);
    }

}
