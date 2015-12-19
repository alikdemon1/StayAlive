package com.alisher.android.stayalive.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alisher.android.stayalive.MainActivity;
import com.alisher.android.stayalive.R;
import com.alisher.android.stayalive.event.RecyclerItemClickListener;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 13.12.2015.
 */
public class NewsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private Bitmap bmp;
    private ArrayList<NewsItem> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new NewsAdapter(getApplicationContext());
        initializeData();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(NewsActivity.this, "Item clicked = " + position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initializeData() {
        news = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("News");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        final NewsItem newsItem = new NewsItem();
                        ParseObject o = list.get(i);
                        newsItem.setName(o.getString("name"));
                        newsItem.setDes(o.getString("desc"));
                        ParseFile image = (ParseFile) o.get("logo");
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    Log.d("BMP", bmp.toString());
                                    newsItem.setmThumbnail(bmp);
                                    Log.e("parse file ok", " null");
                                } else {
                                    Log.e("parse after download", " null");
                                }
                            }
                        });
                        news.add(newsItem);
                        Log.d("IMAGE BLYAD0", news.get(0).getmThumbnail() + "");
                        Log.d("IMAGE BLYAD1", news.get(1).getmThumbnail() + "");
                    }
                    ((NewsAdapter)mAdapter).setNews(news);
                } else {
                    Toast.makeText(NewsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(NewsActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}