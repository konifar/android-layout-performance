package com.konifar.android_layout_performance.views.adapters;

import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.konifar.android_layout_performance.R;
import com.konifar.android_layout_performance.models.TweetModel;
import com.konifar.android_layout_performance.utils.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.internal.util.AspectRatioImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    public TweetAdapter(Context context) {
        super(context, R.layout.item_tweet);
    }

    @Override
    public View getView(final int pos, View view, final ViewGroup parent) {
        ViewHolder holder;
        Tweet tweet = getItem(pos);

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        bindData(tweet, holder);

        view.setTag(holder);

        return view;
    }

    private void bindData(Tweet tweet, ViewHolder holder) {
        holder.mTxtDate.setText(DateUtils.getTwitterRelativeTime(tweet.createdAt));

        Tweet retweet = tweet.retweetedStatus;
        if (retweet != null) {
            holder.mTxtRetweetedMsg.setVisibility(View.VISIBLE);
            String text = getContext().getString(R.string.retweeted_msg, tweet.user.name);
            holder.mTxtRetweetedMsg.setText(text);
            tweet = retweet;
        } else {
            holder.mTxtRetweetedMsg.setVisibility(View.GONE);
        }

        bindUser(tweet, holder);
        bindTweet(tweet, holder);
    }

    private void bindTweet(Tweet tweet, ViewHolder holder) {
        holder.mTxtTweet.setText(Html.fromHtml(tweet.text));
        Linkify.addLinks(holder.mTxtTweet, Linkify.ALL);

        if (tweet.entities != null) {
            List<MediaEntity> mediaEntities = tweet.entities.media;
            if (mediaEntities != null && !mediaEntities.isEmpty()) {
                holder.mImgTweetMedia.setVisibility(View.VISIBLE);
                MediaEntity media = mediaEntities.get(0);
                ImageLoader.getInstance().displayImage(media.mediaUrl, holder.mImgTweetMedia);
            } else {
                holder.mImgTweetMedia.setVisibility(View.GONE);
            }
        }

        if (tweet.retweetCount > 0) {
            holder.mTxtRetweet.setText(String.valueOf(tweet.retweetCount));
        }
        if (tweet.favoriteCount > 0) {
            holder.mTxtFavorite.setText(String.valueOf(tweet.favoriteCount));
        }
    }

    private void bindUser(Tweet tweet, ViewHolder holder) {
        User user = tweet.user;
        ImageLoader.getInstance().displayImage(user.profileImageUrl, holder.mImgUser);
        holder.mTxtUserScreenName.setText(TweetModel.PREFIX_SCREEN_NAME + user.screenName);
        holder.mTxtUserName.setText(user.name);
    }

    static class ViewHolder {
        @InjectView(R.id.txt_retweeted_msg)
        TextView mTxtRetweetedMsg;
        @InjectView(R.id.img_user)
        ImageView mImgUser;
        @InjectView(R.id.txt_user_screen_name)
        TextView mTxtUserScreenName;
        @InjectView(R.id.txt_user_name)
        TextView mTxtUserName;
        @InjectView(R.id.txt_date)
        TextView mTxtDate;
        @InjectView(R.id.txt_tweet)
        TextView mTxtTweet;
        @InjectView(R.id.img_tweet_media)
        AspectRatioImageView mImgTweetMedia;
        @InjectView(R.id.txt_retweet)
        TextView mTxtRetweet;
        @InjectView(R.id.txt_favorite)
        TextView mTxtFavorite;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
