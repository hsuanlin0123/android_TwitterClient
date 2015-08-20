package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hsuanlin on 2015/8/12.
 */
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagLine;
    private String followsCount;
    private String followingCount;

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

    public String getTagLine() {
        return tagLine;
    }

    public String getFollowsCount() {
        return followsCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public static User fromJson( JSONObject jsonObject){
        User u = new User();
        try {
            u.name = jsonObject.getString("name");

            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.tagLine = jsonObject.getString("description");
            u.followsCount = jsonObject.getString("followers_count");
            u.followingCount = jsonObject.getString("friends_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return u;
    }
}
