package com.konifar.android_layout_performance.fragments;

import com.konifar.android_layout_performance.R;

public final class HomeTimelineShallowFragment extends AbstractHomeTimelineFragment {

    int getTweetLayoutResId() {
        return R.layout.item_tweet_shallow;
    }

    @Override
    int getLayoutResId() {
        return R.layout.fragment_home_timeline_shallow;
    }

}
