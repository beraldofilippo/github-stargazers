package com.beraldo.stargazers.stars;

import com.beraldo.stargazers.BasePresenter;
import com.beraldo.stargazers.BaseView;
import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.GenericError;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface StarsContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showStars(ArrayList<Star> stars);
        void showLoadingStarsError(GenericError err);
        void showSearchDialog();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadStars(String username, String repoName);
        void performSearch();
    }
}
