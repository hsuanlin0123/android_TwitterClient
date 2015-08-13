package com.codepath.apps.restclienttemplate; /**
 * Created by hsuanlin on 2015/8/13.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class composeDialog extends DialogFragment {

    private EditText etBody;
    private composeDialogListener listener;
    private TextView tvTextCount;
    private static int maxCount = 140;

    public interface composeDialogListener{
        void onFinishFragment( boolean result );
    }

    public composeDialog(){

    }

    public static composeDialog newInstance()
    {
        composeDialog frag = new composeDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.compose_action, container );

        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        etBody = (EditText) view.findViewById(R.id.etBody);
        tvTextCount = (TextView) view.findViewById(R.id.tvTextCount);
        tvTextCount.setText(Integer.toString(maxCount));

        listener = (composeDialogListener) getActivity();

        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = maxCount - etBody.length();
                tvTextCount.setText(Integer.toString(count));
            }
        });

        btSubmit.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //AdvanceFilterListener listenner = (AdvanceFilterListener) getActivity();
                //listenner.onFinishSettingFragment(spImageSize.getSelectedItem().toString(), spColorFilter.getSelectedItem().toString(), spImageType.getSelectedItem().toString(), etSiteFilter.getText().toString());
                TwitterClient client = TwitterApplication.getRestClient();
                client.postTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Log.d("DEBIG", response.toString());
                        listener.onFinishFragment(true);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBIG", errorResponse.toString());
                        listener.onFinishFragment(false);
                    }
                }, etBody.getText().toString());

                dismiss();
            }
        });
        return view;
    }

}
