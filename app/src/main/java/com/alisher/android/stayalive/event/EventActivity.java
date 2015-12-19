package com.alisher.android.stayalive.event;

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
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private List<Event> events;
    private Bitmap bmp;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Event eventItem = events.get(position);
                Toast.makeText(getApplicationContext(), eventItem.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ShowEvents.class);
                intent.putExtra("name", eventItem.getName());
                intent.putExtra("age", eventItem.getAge());
                intent.putExtra("photo", eventItem.getPhotoId());
                intent.putExtra("start", eventItem.getStart());
                intent.putExtra("end", eventItem.getEnd());
                startActivity(intent);
            }
        }));

        initializeData();
    }

    private void initializeData() {
        events = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        final Event event = new Event();
                        ParseObject o = list.get(i);
                        event.setName(o.getString("title"));
                        event.setAge(o.getString("description"));
                        ParseFile image = o.getParseFile("logo");
                        event.setStart(o.getString("start"));
                        event.setEnd(o.getString("end"));
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    bmp = BitmapFactory.decodeByteArray(bytes, 0,
                                            bytes.length);
                                    event.setPhotoId(bmp);
                                    Log.e("parse file ok", " null");
                                } else {
                                    Log.e("paser after downloade", " null");
                                }
                            }
                        });
                        //event.setPhotoid(R.drawable.calendar53);
                        Log.d("EVENTS", event.getName() + event.getAge());
                        events.add(event);
                    }
                    RVAdapter adapter = new RVAdapter(events);
                    rv.setAdapter(adapter);
                } else {
                    Toast.makeText(EventActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(EventActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
