package com.laioffer.tinnews.save;

import com.laioffer.tinnews.retrofit.response.News;

import java.util.List;

//1.5 Create SavedNewsPresenter which implements SavedNewsContract.Presenter
public class SavedNewsPresenter implements SavedNewsContract.Presenter {
    private final SavedNewsContract.Model model;
    private SavedNewsContract.View view;

    public SavedNewsPresenter() {
        model = new SavedNewsModel();
        model.setPresenter(this);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewAttached(SavedNewsContract.View view) {
        this.view = view;
        if (view.isViewEmpty()) {
            this.model.fetchData();
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void loadSavedNews(List<News> newsList) {
        if (view != null) {
            view.loadSavedNews(newsList);
        }
    }
}

