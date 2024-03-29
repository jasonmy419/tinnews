package com.laioffer.tinnews.tin;

import android.database.sqlite.SQLiteConstraintException;

import com.laioffer.tinnews.TinApplication;
import com.laioffer.tinnews.database.AppDatabase;
import com.laioffer.tinnews.retrofit.NewsRequestApi;
import com.laioffer.tinnews.retrofit.RetrofitClient;
import com.laioffer.tinnews.retrofit.response.News;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TinModel implements TinContract.Model {

    //keep the reference of TinContract.Presenter
    private TinContract.Presenter presenter;
    private final NewsRequestApi newsRequestApi;
    private final AppDatabase database;
    public TinModel(){
        newsRequestApi = RetrofitClient.getInstance().create(NewsRequestApi.class);
        database = TinApplication.getDataBase();
    }
    @Override
    public void setPresenter(TinContract.Presenter presenter) {
        //assign the presenter
        this.presenter = presenter;
    }

    @Override
    public void fetchData(String country) {
        newsRequestApi.getNewsByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(baseResponse -> baseResponse != null && baseResponse.articles != null)
                .subscribe(baseResponse -> {
                    //5.8 pass the fetch data to the model
                    presenter.showNewsCard(baseResponse.articles);
                });

    }

    @Override
    public void saveFavoriteNews(News news) {
        Disposable disposable = Completable.fromAction(() -> database.newsDao().insertNews(news)).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{

        }, error -> {
                    if(error instanceof SQLiteConstraintException){
                        this.presenter.onError();
                    }
        });

    }
}

