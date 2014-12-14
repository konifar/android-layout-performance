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

public class HomeTimelineFragment extends Fragment {

    private static final String TAG = HomeTimelineFragment.class.getSimpleName();

    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.listview)
    ListView mListview;

    private TweetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);
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
        adapter = new TweetAdapter(getActivity());
        mListview.setAdapter(adapter);
        mListview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadData();
            }
        });
    }

    private void loadData() {
        TweetModel.getInstance().getHomeTimeline(getLastId(), new TweetModel.HomeTimelineCallback() {
            @Override
            public void success(List<Tweet> tweets) {
                if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                    adapter.clear();
                }

                adapter.addAll(tweets);
            }

            @Override
            public void failure(Exception e) {
                if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }

                Log.e(TAG, e.getMessage());
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

}
