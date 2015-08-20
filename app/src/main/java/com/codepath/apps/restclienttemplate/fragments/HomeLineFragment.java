package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.util.Log;
import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hsuanlin on 2015/8/19.
 */
public class HomeLineFragment extends TweetsListFragment {
    private TwitterClient client;
    private String max_id = "999999999999999999";
    private long current_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        current_id = new Long(max_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshTweets();
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });

        populateTimeline();
    }

    public void refreshTweets(){
        current_id = new Long(max_id);
        tweets.clear();
        populateTimeline();
    }

    private long getNextMaxId( ArrayList<Tweet> tweets ) {
        if( tweets.size() > 0 ) {
            Tweet lastTweet = tweets.get(tweets.size() - 1);
            return lastTweet.getUid() - 1;
        }
        else
            return current_id;

    }
    public void populateTimeline()
    {
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                current_id = getNextMaxId(tweets);
                setTweets(tweets);
                hideProgressBar();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBIG", errorResponse.toString());
                hideProgressBar();
                swipeContainer.setRefreshing(false);

            }
        }, current_id);
    }
}
