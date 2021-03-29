package com.ru.stockexchange.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import com.ru.stockexchange.database.DAO.RequestDAO;
import com.ru.stockexchange.database.RequestDatabase;
import com.ru.stockexchange.database.entities.Request;
import java.util.List;


public class RequestRepository {
    private RequestDAO mRequestDAO;
    private LiveData<List<Request>> mAllSearchedRequests;
    public RequestRepository(Application application) {
        RequestDatabase requestDatabase = RequestDatabase.getDatabase(application);
        mRequestDAO = requestDatabase.requestDAO();
        mAllSearchedRequests = mRequestDAO.getAll();
    }
    public LiveData<List<Request>> getSearchedRequests() {
        return mAllSearchedRequests;
    }

    public void addRequest(Request request) {
        mRequestDAO.addRequest(request);
    }
}
