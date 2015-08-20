package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.activeandroid.util.Log;
import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hsuanlin on 2015/8/18.
 */
public class TweetsListFragment extends Fragment {

    public ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    public ListView lvTweets;
    public SwipeRefreshLayout swipeContainer;
    public ProgressBar progressBarFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        setupListWithFooter(v, inflater);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }
    // Adds footer to the list default hidden progress
    public void setupListWithFooter(View v, LayoutInflater inflater) {
        // Find the ListView
        lvTweets = (ListView) v.findViewById(R.id.lvTwitter);
        // Inflate the footer
        View footer = inflater.inflate(
                R.layout.footer_progress, null);
        // Find the progressbar within footer
        progressBarFooter = (ProgressBar)
                footer.findViewById(R.id.pbFooterLoading);
        // Add footer to ListView before setting adapter
        lvTweets.addFooterView(footer);
        // Set the adapter AFTER adding footer
        lvTweets.setAdapter(aTweets);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(),tweets);
    }

    // Show progress
    public void showProgressBar() {
        progressBarFooter.setVisibility(View.VISIBLE);
    }

    // Hide progress
    public void hideProgressBar() {
        progressBarFooter.setVisibility(View.GONE);
    }

    public void refreshTweets(){}
    public void setOnRefreshListener( SwipeRefreshLayout.OnRefreshListener listener )
    {
        swipeContainer.setOnRefreshListener( listener );
    }

    public void setTweets( ArrayList<Tweet> tweets)
    {
        aTweets.addAll(tweets);
    }

}
