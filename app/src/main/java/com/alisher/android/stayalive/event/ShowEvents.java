package com.alisher.android.stayalive.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisher.android.stayalive.MainActivity;
import com.alisher.android.stayalive.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowEvents extends AppCompatActivity {

    private static final int WIDTH = 220;
    private static final int BLACK = Color.BLACK;
    private static final int WHITE = Color.WHITE;

    private boolean flag = false;
    private TextView name;
    private TextView age;
    private ImageView photo;
    private Button agree;
    private String start_date;
    private String end_date;
    private String currentToken = ParseUser.getCurrentUser().getObjectId();

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
                ParseQuery<ParseObject> checkQuery = ParseQuery.getQuery("StayAliveUsers");
                checkQuery.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId());
                checkQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null){
                            Log.d("size", list.size()+"");
                            if (list.size() == 0){
                                addUserToEvent();
                                Toast.makeText(ShowEvents.this, "Спасибо, Вы зарегистрировались в этом мероприятие", Toast.LENGTH_SHORT).show();
                            }  else {
                                Toast.makeText(ShowEvents.this, "Извените, но вы уже подтвердили участие", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ShowEventsActivity: ", e.getMessage());
                        }
                    }
                });
            } else {
                agree.setClickable(false);
                Toast.makeText(ShowEvents.this, "Дата регистрации подошла к концу", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ShowEvents.this, EventActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap encodeAsBitmap(String currentToken) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(currentToken,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }

    private void addUserToEvent() {
        Bitmap bitmap = null;
        try {
            bitmap = encodeAsBitmap(currentToken);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        final ParseFile file = new ParseFile("qr.png", byteArray);

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    for (ParseUser objUser : list) {
                        ParseObject stayAliveObj = new ParseObject("StayAliveUsers");
                        ParseObject SAAvhievement = new ParseObject("UserAchievements");
                        stayAliveObj.put("user_id", objUser.getObjectId());
                        stayAliveObj.put("counter", objUser.get("p_counter"));
                        SAAvhievement.put("user_id", objUser.getObjectId());
                        SAAvhievement.put("p_counter", objUser.get("p_counter"));
                        SAAvhievement.put("username", objUser.get("username"));
                        SAAvhievement.put("isDead", false);
                        SAAvhievement.put("killings", 0);
                        SAAvhievement.put("money", 100);
                        SAAvhievement.put("qrCode", file);
                        stayAliveObj.saveEventually();
                        SAAvhievement.saveInBackground();
                    }
                    Toast.makeText(ShowEvents.this, "User info transferred", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowEvents.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}