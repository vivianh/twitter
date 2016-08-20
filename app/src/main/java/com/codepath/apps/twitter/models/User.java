package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Table(name = "Users")
@Parcel(Parcel.Serialization.BEAN)
public class User extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "unique_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "tag_line")
    private String tagline;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "following_count")
    private int followingCount;

    public User() {
        super();
    }

    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagline = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public static User getUser(long uniqueId) {
        return new Select()
                .from(User.class)
                .where("uniqueId = ?", uniqueId)
                .executeSingle();
    }
}
