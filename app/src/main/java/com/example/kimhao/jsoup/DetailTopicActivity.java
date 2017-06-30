package com.example.kimhao.jsoup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.kimhao.jsoup.adapter.DetailTopicAdapter;
import com.example.kimhao.jsoup.models.DetailTopic;
import com.example.kimhao.jsoup.models.Topic;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_detail_topic)
public class DetailTopicActivity extends AppCompatActivity {
    @ViewById(R.id.tvTitleTopic)
    TextView mTitleTopic;
    @ViewById(R.id.recyclerViewDetail)
    RecyclerView mRecyclerViewDetail;
    @ViewById(R.id.adView)
    AdView mAdView;

    @AfterViews
    void afterViews() {
        // Load an ad into the AdMob banner view.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mAdView.loadAd(adRequest);

        Topic mTopic = getIntent().getParcelableExtra("topic");
        String urlSubject = mTopic.getUrlSubject();
        String url = MainActivity.MY_URL + urlSubject;

        loadData();
        new DownloadTaskDetail().execute(url);
        new DownloadTitleTopic().execute(url);
    }

    private void loadData() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecyclerViewDetail.hasFixedSize();
        mRecyclerViewDetail.setLayoutManager(layoutManager);
    }

    private class DownloadTaskDetail extends AsyncTask<String, Void, List<DetailTopic>> {
        @Override
        protected List<DetailTopic> doInBackground(String... params) {
            Document document;
            List<DetailTopic> mDetailTopics = new ArrayList<>();
            try {
                document = Jsoup.connect(params[0]).get();
                if (document != null) {
                    Elements sub = document.select("div.tuvung");

                    for (int i = 0; i < sub.size(); i++) {
                        String imgTopicDetail;
                        String vocabulary;
                        String explain;
                        String category;
                        String example;
                        String urlAudio;

                        DetailTopic detailTopic = new DetailTopic();
                        imgTopicDetail = sub.get(i).getElementsByTag("img").first().attr("src");

                        Elements contents = sub.get(i).select("div.noidung");
                        String strContents = contents.get(0).text();

                        Elements strSpan = contents.get(0).getElementsByTag("span");

                        vocabulary = strSpan.get(0).text() + strSpan.get(1).text();
                        category = strSpan.get(3).text();
                        example = strSpan.get(4).text();

                        int indexCategory = strContents.lastIndexOf(category);
                        explain = strContents.substring(vocabulary.length() + 2, indexCategory);

                        int indexExample = strContents.lastIndexOf(example);
                        category = strContents.substring(vocabulary.length() + explain.length() + 2, indexExample);

                        example = strContents.substring(vocabulary.length() + explain.length() +
                                category.length() + 1, strContents.lastIndexOf(getString(R.string.string_audio)));
                        urlAudio = contents.get(0).getElementsByTag("source").first().attr("src");

                        //set detailTopic
                        detailTopic.setImgTopicDetail(imgTopicDetail);
                        detailTopic.setVocabulary(vocabulary);
                        detailTopic.setExplain(explain);
                        detailTopic.setCategory(category);
                        detailTopic.setExample(example);
                        detailTopic.setUrlAudio(urlAudio);

                        mDetailTopics.add(detailTopic);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mDetailTopics;
        }

        @Override
        protected void onPostExecute(List<DetailTopic> detailTopics) {
            super.onPostExecute(detailTopics);
            DetailTopicAdapter mDetailTopicAdapter = new DetailTopicAdapter(DetailTopicActivity.this, detailTopics);
            mRecyclerViewDetail.setAdapter(mDetailTopicAdapter);
        }
    }

    private class DownloadTitleTopic extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Document document;
            String title = null;
            try {
                document = Jsoup.connect(params[0]).get();
                if (document != null) {
                    Elements elements  = document.select("div.toggle-content");
                    title = elements.get(0).getElementsByTag("h2").first().text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTitleTopic.setText(s);
        }
    }
}
