package com.codepath.apps.restclienttemplate;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity {
    private TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        String screen_name = getIntent().getStringExtra("screen_name");

        if( screen_name == null ) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    popularUserInfo(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBIG", errorResponse.toString());

                }
            });
        }
        else
        {
            client.getOtherUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    popularUserInfo(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBIG", errorResponse.toString());

                }
            }, screen_name);
        }

        if( savedInstanceState == null ) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screen_name);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    private void popularUserInfo(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollows = (TextView) findViewById(R.id.tvFollows);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivImgProfile);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagLine());
        tvFollows.setText(user.getFollowsCount() + " follows");
        tvFollowing.setText(user.getFollowingCount() + " following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfile);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
