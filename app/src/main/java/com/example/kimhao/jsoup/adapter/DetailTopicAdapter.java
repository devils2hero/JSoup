package com.example.kimhao.jsoup.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kimhao.jsoup.MainActivity;
import com.example.kimhao.jsoup.R;
import com.example.kimhao.jsoup.models.DetailTopic;

import java.util.List;

/**
 * Author by AsianTech
 * Created by kimha on 29/06/2017.
 */

public class DetailTopicAdapter extends RecyclerView.Adapter<DetailTopicAdapter.ViewHolder> {
    private MediaPlayer mMediaPlayer;
    private Activity mActivity;
    private List<DetailTopic> mDetailTopics;

    public DetailTopicAdapter(Activity mActivity, List<DetailTopic> mDetailTopics) {
        this.mActivity = mActivity;
        this.mDetailTopics = mDetailTopics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DetailTopic detailTopic = mDetailTopics.get(position);
        Glide.with(mActivity)
                .load(detailTopic.getImgTopicDetail())
                .asBitmap()
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(holder.mImgTopicDetail);
        holder.mTvVocabulary.setText(detailTopic.getVocabulary());
        holder.mTvExplain.setText(detailTopic.getExplain());
        holder.mTvCategory.setText(detailTopic.getCategory());
        holder.mTvExample.setText(detailTopic.getExample());

        holder.mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    killMediaPlayer();
                    playAudio(MainActivity.MY_URL+detailTopic.getUrlAudio());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailTopics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgTopicDetail;
        private TextView mTvVocabulary;
        private TextView mTvExplain;
        private TextView mTvCategory;
        private TextView mTvExample;
        private Button mBtnPlay;

        ViewHolder(View itemView) {
            super(itemView);
            mImgTopicDetail = (ImageView) itemView.findViewById(R.id.imgTopicDetail);
            mTvVocabulary = (TextView) itemView.findViewById(R.id.tvVocabulary);
            mTvExplain = (TextView) itemView.findViewById(R.id.tvExplain);
            mTvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            mTvExample = (TextView) itemView.findViewById(R.id.tvExample);
            mBtnPlay = (Button) itemView.findViewById(R.id.btnPlay);
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
