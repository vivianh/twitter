package com.codepath.apps.twitter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public static class ViewHolder {
        @BindView(R.id.ivUserProfileImage) ImageView ivUserProfileImage;
        @BindView(R.id.tvTweetBody) TextView tvTweetBody;
        @BindView(R.id.tvTweetCreatedAt) TextView tvTweetCreatedAt;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTweetBody.setText(tweet.getBody());
        viewHolder.tvTweetCreatedAt.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        String atScreenName = getContext().getResources().getString(R.string.at_screen_name, tweet.getUser().getScreenName());
        viewHolder.tvUserScreenName.setText(atScreenName);
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(viewHolder.ivUserProfileImage);

        viewHolder.ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ProfileActivity.class);
                // i.putExtra("user", Parcels.wrap(tweet.getUser()));
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                view.getContext().startActivity(i);
            }
        });

        return convertView;
    }

    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
