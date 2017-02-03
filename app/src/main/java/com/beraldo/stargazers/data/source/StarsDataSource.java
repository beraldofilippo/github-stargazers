package com.beraldo.stargazers.data.source;

import android.support.annotation.NonNull;

import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.net.GithubService;

import java.util.ArrayList;

/** Interface that helps to abstarct the data layer from its real location, be on the web or coming
 * from a local storage, this help mocking the data source when it comes to testing.
 * */

public interface StarsDataSource {
    void getStars(@NonNull String username, @NonNull String repoName,
                             final @NonNull GetStarsCallback callback);

    interface GetStarsCallback {
        void onCompleted(ArrayList<Star> result);
        void onError(GenericError err); // This will contain both APIError and Throwable exceptions
    }
}
