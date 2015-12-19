package com.alisher.android.stayalive.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisher.android.stayalive.MainActivity;
import com.alisher.android.stayalive.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowEvents extends AppCompatActivity {
    private TextView name;
    private TextView age;
    private ImageView photo;
    private Button agree;
    private String start_date;
    private String end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);
        initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        agree = (Button) findViewById(R.id.agree);
        getExrtras();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getExrtras() {
        name = (TextView) findViewById(R.id.name_text);
        age = (TextView) findViewById(R.id.year_text);
        photo = (ImageView) findViewById(R.id.logo_image);

        Intent i = getIntent();
        name.setText(i.getExtras().getString("name"));
        age.setText(i.getExtras().getString("age"));
        Bitmap bitmap = (Bitmap) i.getParcelableExtra("photo");
        start_date = i.getExtras().getString("start");
        end_date = i.getExtras().getString("end");
        photo.setImageBitmap(bitmap);
    }

    public void imageTargetShow(View view) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date current = new Date();
            String s = dateFormat.format(current);
            current = dateFormat.parse(s);
            //Start Date
            Date start = dateFormat.parse(start_date);
            //End Date
            Date end = dateFormat.parse(end_date);

            if (!current.after(start)) {
                Toast.makeText(ShowEvents.this, "It Works: " + (start.getTime() - end.getTime()), Toast.LENGTH_SHORT).show();
            } else {
                agree.setClickable(false);
                Toast.makeText(ShowEvents.this, "Clickable false", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ShowEvents.this,EventActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}