package com.konifar.android_layout_performance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.konifar.android_layout_performance.fragments.HomeTimelineNarrowFragment;
import com.konifar.android_layout_performance.fragments.HomeTimelineNarrowOverDrawnFragment;
import com.konifar.android_layout_performance.fragments.HomeTimelineShallowFragment;
import com.konifar.android_layout_performance.fragments.LoginFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (Twitter.getSessionManager().getActiveSession() == null) {
            LoginActivity.start(this);
        }

        if (savedInstanceState == null) {
            changeToolbarSubTitle(R.string.mode_shallow_layout);
            showFragment(new HomeTimelineShallowFragment());
        }

        setSupportActionBar(mToolbar);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, ((Object) fragment).getClass().getSimpleName())
                .commit();
    }

    private void changeToolbarSubTitle(int stringResId) {
        mToolbar.setSubtitle(stringResId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_shallow_layout:
                changeToolbarSubTitle(R.string.mode_shallow_layout);
                showFragment(new HomeTimelineShallowFragment());
                break;
            case R.id.action_narrow_layout:
                changeToolbarSubTitle(R.string.mode_narrow_layout);
                showFragment(new HomeTimelineNarrowFragment());
                break;
            case R.id.action_overdrawn_layout:
                changeToolbarSubTitle(R.string.mode_overdrawn_layout);
                showFragment(new HomeTimelineNarrowOverDrawnFragment());
                break;
            case R.id.action_logout:
                Twitter.logOut();
                LoginActivity.start(this);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
