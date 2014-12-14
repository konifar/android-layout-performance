package com.konifar.android_layout_performance.fragments;

import com.konifar.android_layout_performance.R;

public final class HomeTimelineNarrowOverDrawnFragment extends AbstractHomeTimelineFragment {

    int getTweetLayoutResId() {
        return R.layout.item_tweet_narrow_over_drawn;
    }

    @Override
    int getLayoutResId() {
        return R.layout.fragment_home_timeline_narrow_over_drawn;
    }

}
