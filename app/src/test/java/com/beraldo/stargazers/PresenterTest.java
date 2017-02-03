package com.beraldo.stargazers;

import android.support.v7.widget.RecyclerView;

import com.beraldo.stargazers.data.source.GenericError;
import com.beraldo.stargazers.data.source.StarsDataSource;
import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.net.GithubService;
import com.beraldo.stargazers.stars.StarsContract;
import com.beraldo.stargazers.stars.StarsPresenter;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PresenterTest {
    private static List<Star> STARS;

    @Mock
    private GithubService mGithubService;

    @Mock
    private StarsContract.View mStarsView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<StarsDataSource.GetStarsCallback> mGetStarsCallbackArgumentCaptor;

    private StarsPresenter mStarsPresenter;

    @Before
    public void setupStarsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mStarsPresenter = new StarsPresenter(mGithubService, mStarsView);

        // The presenter won't update the view unless it's active.
        when(mStarsView.isActive()).thenReturn(true);

        // We start the tasks to 3, with one active and two completed
        STARS = Lists.newArrayList();
        STARS.add(new Star("Donald", "http://www.succedeoggi.it/wordpress/wp-content/uploads/2016/08/donald-trump.jpg"));
        STARS.add(new Star("Homer", "http://www.homersimpsonquotes.com/images/homer.gif"));
        STARS.add(new Star("Richard", "http://i.ytimg.com/vi/fGHsTArhpoU/hqdefault.jpg"));
    }

    @Test
    public void loadStarsAndLoadIntoView() {
        // Given an initialized TasksPresenter with initialized tasks
        // When loading of Tasks is requested
        mStarsPresenter.loadStars("beraldofilippo", "android-hpe");

        // Callback is captured and invoked with stubbed tasks
        verify(mGithubService).getStars(eq("beraldofilippo"), eq("android-hpe"), mGetStarsCallbackArgumentCaptor.capture());
        mGetStarsCallbackArgumentCaptor.getValue().onCompleted(new ArrayList<Star>(STARS));

        // Then progress indicator is shown
        InOrder inOrder = inOrder(mStarsView);
        inOrder.verify(mStarsView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all tasks are shown in UI
        inOrder.verify(mStarsView).setLoadingIndicator(false);
        ArgumentCaptor<ArrayList> showTasksArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(mStarsView).showStars(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void clickOnFab_ShowSearchStargazers() {
        // When adding a new task
        mStarsPresenter.performSearch();

        // Then add task UI is shown
        verify(mStarsView).showSearchDialog();
    }

    @Test
    public void unavailableStars_ShowsError() {
        // When tasks are loaded
        mStarsPresenter.loadStars("beraldofilippo", "android-hpe");

        GenericError err = new GenericError(new Throwable("Generic error to test."));

        // And the tasks aren't available in the repository
        verify(mGithubService).getStars(eq("beraldofilippo"), eq("android-hpe"), mGetStarsCallbackArgumentCaptor.capture());
        mGetStarsCallbackArgumentCaptor.getValue().onError(err);

        // Then an error message is shown
        verify(mStarsView).showLoadingStarsError(err);
    }
}