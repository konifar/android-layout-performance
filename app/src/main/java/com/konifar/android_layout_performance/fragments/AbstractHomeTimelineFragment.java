package com.konifar.android_layout_performance.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.konifar.android_layout_performance.R;
import com.konifar.android_layout_performance.models.TweetModel;
import com.konifar.android_layout_performance.views.adapters.TweetAdapter;
import com.konifar.android_layout_performance.views.listeners.EndlessScrollListener;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class AbstractHomeTimelineFragment extends Fragment {

    private static final String TAG = AbstractHomeTimelineFragment.class.getSimpleName();

    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.listview)
    ListView mListview;
    @InjectView(R.id.loading)
    View mLoading;

    private TweetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.inject(this, view);

        initSwipeRefresh();
        initListView();
        loadData();

        return view;
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.cyan300, R.color.cyan600, R.color.cyan900);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                loadData();
            }
        });
    }

    private void initListView() {
        adapter = new TweetAdapter(getActivity(), getTweetLayoutResId());
        mListview.setAdapter(adapter);
        mListview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadData();
            }
        });
    }

    private void loadData() {
        final Long lastId = getLastId();
        if (lastId == null) mLoading.setVisibility(View.VISIBLE);

        TweetModel.getInstance().getHomeTimeline(lastId, new TweetModel.HomeTimelineCallback() {
            @Override
            public void success(List<Tweet> tweets) {
                if (mLoading != null) mLoading.setVisibility(View.GONE);

                if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                    adapter.clear();
                }

                adapter.addAll(tweets);
            }

            @Override
            public void failure(Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void complete() {
                if (mLoading != null) mLoading.setVisibility(View.GONE);

                if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private Long getLastId() {
        if (mSwipeRefresh.isRefreshing() || adapter.isEmpty()) {
            return null;
        } else {
            return adapter.getItem(adapter.getCount() - 1).id;
        }
    }

    abstract int getTweetLayoutResId();

    abstract int getLayoutResId();
}
