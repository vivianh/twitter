package com.codepath.apps.twitter.fragments;

import android.os.Bundle;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    @Override
    protected void populateTimeline() {
        if (!isOnline()) {
            addAll(Tweet.getHomeTimelineTweets());
            return;
        }

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                swipeRefreshLayout.setRefreshing(false);
                addAll(Tweet.fromJSONArray(json, false));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    protected void populateTimelineMore(long maxId) {
        client.getHomeTimelineWithMaxId(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                hideProgressBar();
                addMore(Tweet.fromJSONArray(json, false));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void displayTweet() {

    }
}
