package com.example.kimhao.jsoup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kimhao.jsoup.adapter.TopicAdapter;
import com.example.kimhao.jsoup.models.Topic;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.adView)
    AdView mAdView;
    @ViewById(R.id.recyclerViewContainer)
    RecyclerView mRecyclerViewTopic;

    public static final String MY_URL = "http://600tuvungtoeic.com/";

    private TopicAdapter mTopicAdapter;
    private List<Topic> mTopics;

    @AfterViews
    void afterViews() {
        // Load an ad into the AdMob banner view.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mAdView.loadAd(adRequest);

        loadData();
        //download data from url
        new DownloadTask().execute(MY_URL);
    }

    private void loadData() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewTopic.hasFixedSize();
        mRecyclerViewTopic.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            Document document;
            mTopics = new ArrayList<>();
            try {
                document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements sub = document.select("div.gallery-item");
                    for (Element element : sub) {
                        Topic topic = new Topic();
                        Element titleSubject = element.getElementsByTag("h3").first();
                        Element imgSubject = element.getElementsByTag("img").first();
                        Element urlSubject = element.getElementsByTag("a").first();

                        //Parse to model
                        if (titleSubject != null) {
                            topic.setTitleSubject(titleSubject.text());
                        }

                        if (imgSubject != null) {
                            topic.setImageSubject(imgSubject.attr("src"));
                        }

                        if (urlSubject != null) {
                            topic.setUrlSubject(urlSubject.attr("href"));
                        }

                        //add topic to list topic
                        mTopics.add(topic);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mTopics;
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);
//            Log.e("aaaa", "onPostExecute: " + topics.size());
            //Setup data recyclerView
            mTopicAdapter = new TopicAdapter(MainActivity.this, topics);
            mRecyclerViewTopic.setAdapter(mTopicAdapter);
        }
    }
}
