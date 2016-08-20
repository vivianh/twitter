package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    private User user;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        // args.putParcelable("user", Parcels.wrap(user));
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        // user = Parcels.unwrap(getArguments().getParcelable("user"));
        populateTimeline();
    }

    @Override
    protected void populateTimeline() {
        if (!isOnline()) {
            return;
        }

        String screenName = getArguments().getString("screen_name");

        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                swipeRefreshLayout.setRefreshing(false);
                addAll(Tweet.fromJSONArray(json, false));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    protected void populateTimelineMore(long maxId) {
        String screenName = getArguments().getString("screen_name");

        client.getUserTimelineWithMaxId(screenName, maxId, new JsonHttpResponseHandler() {
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
}
