package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.twitter.EndlessScrollListener;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TweetsArrayAdapter;
import com.codepath.apps.twitter.models.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    @BindView(R.id.lvTweets) ListView lvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBarFooter;

    // inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, v);

        View footer = inflater.inflate(R.layout.footer_progress, null);
        progressBarFooter = (ProgressBar) footer.findViewById(R.id.pbFooterLoading);
        lvTweets.addFooterView(footer);

        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                if (isOnline()) {
                    showProgressBar();
                    populateTimelineMore(tweets.get(tweets.size() - 1).getUid());
                    return true;
                }
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getContext(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.clear();
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
    }

    public void addMore(List<Tweet> tweets) {
        if (tweets.size() > 0) {
            tweets.remove(0);
            aTweets.addAll(tweets);
            aTweets.notifyDataSetChanged();
        }
    }

    public void showProgressBar() {
        progressBarFooter.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarFooter.setVisibility(View.INVISIBLE);
    }

    protected abstract void populateTimeline();

    protected abstract void populateTimelineMore(long maxId);

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
