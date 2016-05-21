package com.alisher.android.stayalive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DeadActivity extends AppCompatActivity {
    private final static String TAG = "DEAD_ACTIVITY";

    private Bitmap photo;
    private ImageView qrImg;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrImg = (ImageView) findViewById(R.id.qr_image);
        getQrImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(DeadActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DeadActivity.this, MainActivity.class));
    }

    private void getQrImage() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserAchievements");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d("PROVERKA", list.size()+"");
                    if (list.isEmpty()){
                        Toast.makeText(DeadActivity.this, "Event not started", Toast.LENGTH_SHORT).show();
                    }
                    for (ParseObject p : list) {
                        ParseFile file = p.getParseFile("qrCode");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                qrImg.setImageBitmap(photo);
                            }
                        });
                    }
                } else {
                    Log.e("DEAD_ACTIVITY", e.getMessage());
                }
            }
        });
    }
}
