package com.codepath.apps.twitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagLine) TextView tvTagLine;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        client = TwitterApplication.getRestClient();
        String screenName = getIntent().getStringExtra("screen_name");
        // user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        if (screenName == null) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            });
        } else {
            client.getOtherUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            });
        }

        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        String followersCount = getResources().getString(R.string.num_followers, user.getFollowersCount());
        String followingCount = getResources().getString(R.string.num_following, user.getFollowingCount());
        tvFollowers.setText(followersCount);
        tvFollowing.setText(followingCount);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}