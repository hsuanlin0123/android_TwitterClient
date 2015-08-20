package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.fragments.HomeLineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelilneFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class TimelineActivity extends ActionBarActivity implements composeDialog.composeDialogListener {
    private  ViewPager viewPager;

    @Override
    public void onFinishFragment( boolean result) {
        if( result ) {
            Toast.makeText(this, "post success", Toast.LENGTH_SHORT).show();
            TweetPagerAdapter tweetPagerAdapter = (TweetPagerAdapter) viewPager.getAdapter();
            tweetPagerAdapter.startUpdate();
        }
        else{
            Toast.makeText(this,"post fail",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

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


        return super.onOptionsItemSelected(item);
    }

    public void onClickComposeBtn( MenuItem item){
        FragmentManager fm = getSupportFragmentManager();
        composeDialog composeDg = composeDialog.newInstance();
        composeDg.show(fm,"test");
    }

    public void onClickProfileBtn(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public class TweetPagerAdapter extends FragmentPagerAdapter{
        private String tabTitles[] = {"Home","Mentions"};
        FragmentManager fragmentManager;

        public TweetPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }


        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeLineFragment();
            }
            else if(position == 1){
                return new MentionsTimelilneFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        public void startUpdate() {
            List<Fragment> fragments = fragmentManager.getFragments();
            for(int i = 0; i < fragments.size(); i++){
                TweetsListFragment fragment = (TweetsListFragment) fragments.get(i);
                if( fragment != null ) {
                    fragment.refreshTweets();
                }
            }
        }
    }
}
