package com.konifar.android_layout_performance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.android_layout_performance.R;
import com.konifar.android_layout_performance.events.LoginSuccessEvent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @InjectView(R.id.btn_twitter_login)
    TwitterLoginButton mBtnTwitterLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);

        initTwitterLoginBtn();

        return view;
    }

    private void initTwitterLoginBtn() {
        mBtnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                EventBus.getDefault().post(new LoginSuccessEvent());
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBtnTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

}
