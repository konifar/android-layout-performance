package com.konifar.android_layout_performance.models;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

public class TweetModel {

    public static final String PREFIX_SCREEN_NAME = "@";

    private static final int COUNT = 50;

    private static TweetModel instance;

    private TweetModel() {
        //
    }

    public static TweetModel getInstance() {
        if (instance == null) {
            instance = new TweetModel();
        }
        return instance;
    }

    public void getHomeTimeline(long sinceId, final HomeTimelineCallback cb) {
        getHomeTimeline(COUNT, sinceId, cb);
    }

    public void getHomeTimeline(int count, long sinceId, final HomeTimelineCallback cb) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        service.homeTimeline(count, sinceId, null, false, false, true, true,
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        cb.success(listResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        cb.failure(e);
                    }
                });
    }

    public interface HomeTimelineCallback {
        public void success(List<Tweet> tweets);

        public void failure(Exception e);
    }

}