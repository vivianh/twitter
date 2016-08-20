package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "body")
	private String body;

    @Column(name = "unique_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; // unique ID

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete= Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "is_mention")
    private boolean isMention;

    public static Tweet fromJSON(JSONObject json, boolean isMention) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
            tweet.isMention = isMention;
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray json, boolean isMention) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                tweets.add(Tweet.fromJSON((JSONObject) json.get(i), isMention));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public boolean isMention() {
        return isMention;
    }

    public static List<Tweet> getHomeTimelineTweets() {
        return new Select()
                .from(Tweet.class)
                .where("is_mention = ?", false)
                .orderBy("created_at DESC")
                .execute();
    }

    public static List<Tweet> getMentionsTimelineTweets() {
        return new Select()
                .from(Tweet.class)
                .where("is_mention = ?", true)
                .orderBy("created_at DESC")
                .execute();
    }

}
