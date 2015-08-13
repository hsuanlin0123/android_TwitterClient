package com.codepath.apps.restclienttemplate;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity implements composeDialog.composeDialogListener {
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private String max_id = "999999999999999999";
    private long current_id;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onFinishFragment( boolean result) {
        if( result ) {
            Toast.makeText(this,"post success",Toast.LENGTH_SHORT).show();
            refreshTweets();
        }
        else{
            Toast.makeText(this,"post fail",Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshTweets(){
        current_id = new Long(max_id);
        tweets.clear();
        populateTimeline();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshTweets();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        current_id = new Long(max_id);
        lvTweets = (ListView) findViewById(R.id.lvTwitter);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this,tweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getHomeTimeline();
            }
        });

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateTimeline()
    {
        getHomeTimeline();
    }

    private long getNextMaxId( ArrayList<Tweet> tweets ) {
        if( tweets.size() > 0 ) {
            Tweet lastTweet = tweets.get(tweets.size() - 1);
            return lastTweet.getUid() - 1;
        }
        else
            return current_id;

    }

    private void getHomeTimeline()
    {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                current_id = getNextMaxId( tweets );
                aTweets.addAll(tweets);
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBIG", errorResponse.toString());
                swipeContainer.setRefreshing(false);

            }
        }, current_id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickComposeBtn( MenuItem item){
        FragmentManager fm = getSupportFragmentManager();
        composeDialog composeDg = composeDialog.newInstance();
        composeDg.show(fm,"test");
    }
}
