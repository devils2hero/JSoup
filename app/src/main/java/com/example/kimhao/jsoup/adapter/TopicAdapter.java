package com.example.kimhao.jsoup.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kimhao.jsoup.DetailTopicActivity_;
import com.example.kimhao.jsoup.R;
import com.example.kimhao.jsoup.models.Topic;

import java.util.List;

/**
 * Author by AsianTech
 * Created by kimha on 29/06/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private Activity mActivity;
    private List<Topic> mTopics;

    public TopicAdapter(Activity mActivity, List<Topic> mTopics) {
        this.mActivity = mActivity;
        this.mTopics = mTopics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Topic topic = mTopics.get(position);
        Glide.with(mActivity)
                .load(topic.getImageSubject())
                .asBitmap()
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(holder.mImgTopic);
        holder.mTitleTopic.setText(topic.getTitleSubject());
        holder.mImgTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, DetailTopicActivity_.class).putExtra("topic", topic));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgTopic;
        private TextView mTitleTopic;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgTopic = (ImageView) itemView.findViewById(R.id.imgTopic);
            mTitleTopic = (TextView) itemView.findViewById(R.id.tvTopic);
        }
    }

}
