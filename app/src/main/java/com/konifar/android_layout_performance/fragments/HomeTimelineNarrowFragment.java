package com.konifar.android_layout_performance.fragments;

import com.konifar.android_layout_performance.R;

public final class HomeTimelineNarrowFragment extends AbstractHomeTimelineFragment {

    int getTweetLayoutResId() {
        return R.layout.item_tweet_narrow;
    }

    @Override
    int getLayoutResId() {
        return R.layout.fragment_home_timeline_narrow;
    }

}
