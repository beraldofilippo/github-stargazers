package com.beraldo.stargazers.stars;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.beraldo.stargazers.R;
import com.beraldo.stargazers.data.source.net.GithubService;
import com.beraldo.stargazers.util.ActivityUtils;
import com.beraldo.stargazers.util.EspressoIdlingResource;

public class StarsActivity extends AppCompatActivity {
    private StarsPresenter mStarsPresenter;

    private static String USERNAME = "username";
    private static String REPONAME = "reponame";
    private String username;
    private String repoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        StarsFragment starsFragment =
                (StarsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (starsFragment == null) {
            // Create the fragment
            starsFragment = StarsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), starsFragment, R.id.contentFrame);
        }

        // Create the presenter
        mStarsPresenter = new StarsPresenter(GithubService.getInstance(), starsFragment);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            username = savedInstanceState.getString(USERNAME);
            repoName = savedInstanceState.getString(REPONAME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USERNAME, username);
        outState.putString(REPONAME, repoName);

        super.onSaveInstanceState(outState);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
