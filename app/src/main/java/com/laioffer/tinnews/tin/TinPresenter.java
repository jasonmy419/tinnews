package com.laioffer.tinnews.tin;

import com.laioffer.tinnews.profile.CountryEvent;
import com.laioffer.tinnews.retrofit.response.News;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class TinPresenter implements TinContract.Presenter {
    private TinContract.View view;
    private TinContract.Model model;

    public TinPresenter(){
        this.model = new TinModel();
        this.model.setPresenter(this);
    }
    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);

    }

    @Override
    public void onViewAttached(TinContract.View view) {
        this.view = view;
        this.model.fetchData("us");
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CountryEvent countryEvent) {
        if (this.view != null) {
            this.model.fetchData(countryEvent.country);
        }
    }

    @Override
    public void showNewsCard(List<News> newsList) {
        if(view != null){
            view.showNewsCard(newsList);
        }
    }

    @Override
    public void saveFavoriteNews(News news) {
        model.saveFavoriteNews(news);
    }

    @Override
    public void onError() {
        if(view != null) {
            this.view.onError();
        }
    }
}
