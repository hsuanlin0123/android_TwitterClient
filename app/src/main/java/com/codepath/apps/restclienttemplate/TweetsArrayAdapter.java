package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hsuanlin on 2015/8/12.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvbody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);

        tvUserName.setText(tweet.getUser().getScreenName());
        tvbody.setText(Html.fromHtml(tweet.getBody()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setBackgroundResource(0);
        tvCreatedAt.setText(tweet.getCreatedAt());
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
                String screenName = (String) ivProfileImage.getTag();

                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("screen_name",screenName);
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
