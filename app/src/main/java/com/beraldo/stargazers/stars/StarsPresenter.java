package com.beraldo.stargazers.stars;

import android.support.annotation.NonNull;
import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.GenericError;
import com.beraldo.stargazers.data.source.StarsDataSource;
import com.beraldo.stargazers.data.source.net.GithubService;
import com.beraldo.stargazers.util.EspressoIdlingResource;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class StarsPresenter implements StarsContract.Presenter {
    private final GithubService mGithubService;
    private final StarsContract.View mStarsView;

    public StarsPresenter(@NonNull GithubService githubService, @NonNull StarsContract.View starsView) {
        mGithubService = checkNotNull(githubService, "githubService cannot be null");
        mStarsView = checkNotNull(starsView, "starsView cannot be null!");
        mStarsView.setPresenter(this);
    }

    @Override
    public void loadStars(String username, String repoName) {
            mStarsView.setLoadingIndicator(true);

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mGithubService.getStars(username, repoName, new StarsDataSource.GetStarsCallback() {
            @Override
            public void onCompleted(ArrayList<Star> result) {
                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }
                // The view may not be able to handle UI updates anymore
                if (!mStarsView.isActive()) {
                    return;
                }

                mStarsView.setLoadingIndicator(false);
                mStarsView.showStars(result);
            }

            @Override
            public void onError(GenericError err) {
                // The view may not be able to handle UI updates anymore
                if (!mStarsView.isActive()) {
                    return;
                }
                mStarsView.setLoadingIndicator(false);
                mStarsView.showLoadingStarsError(err);
            }
        });
    }

    @Override
    public void performSearch() {
        mStarsView.showSearchDialog();
    }

    @Override
    public void start() {
        loadStars("beraldofilippo", "android-hpe");
    }
}
