package com.konifar.android_layout_performance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.konifar.android_layout_performance.events.LoginSuccessEvent;
import com.konifar.android_layout_performance.fragments.LoginFragment;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class LoginActivity extends FragmentActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), LoginFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onEvent(LoginSuccessEvent event) {
        MainActivity.start(this);
        finish();
    }

}
