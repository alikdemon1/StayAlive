package com.alisher.android.stayalive;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class HunterActivity extends AppCompatActivity {

    ParseUser currentUser= ParseUser.getCurrentUser();
    private TextView groupTV;
    private TextView genderTV;
    private String hunterGroup;
    private String hunterGender;
    private ImageView imageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initContent();
        getHunter();
    }

    private void initContent() {
        groupTV = (TextView) findViewById(R.id.textGroupHunter);
        genderTV = (TextView) findViewById(R.id.textGenderHunter);
        imageIV=(ImageView)findViewById(R.id.imageHunter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(HunterActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getHunter(){
        final ParseQuery<ParseUser> queryUsername = ParseUser.getQuery();
        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("StayAliveUsers");
        queryUser.whereEqualTo("user_id", currentUser.getObjectId());
        final ParseQuery<ParseObject> queryTarget = ParseQuery.getQuery("StayAliveUsers");
        queryUser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    for (ParseObject p : list) {
                        String idOfHunter = p.getString("hunter_id");
                        queryTarget.whereEqualTo("objectId", idOfHunter);
                        queryTarget.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null){
                                    for (ParseObject p : list) {
                                        try {
                                            hunterGender = queryUsername.whereEqualTo("objectId", p.getString("user_id")).getFirst().getString("gender");
                                            hunterGroup = queryUsername.whereEqualTo("objectId", p.getString("user_id")).getFirst().getString("group");
//                                            Log.d("hunterGender", hunterGender);
//                                            Log.d("hunterGroup", hunterGroup);
                                            //hunterPhoto = queryUsername.whereEqualTo("objectId", p.getString("user_id")).getFirst().getString("group");
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }

                                } else {

                                }
                            }
                        });
                    }
                } else {

                }
            }
        });
    }

    public void imageHunterShow(View view) {
        Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
    }
    public void clickedBuyGender(View view){
        genderTV.setText(hunterGender);
        Toast.makeText(this, "Buy Gender", Toast.LENGTH_SHORT).show();
    }
    public void clickedBuyGroup(View view){
        groupTV.setText(hunterGroup);
        Toast.makeText(this, "Buy Group", Toast.LENGTH_SHORT).show();
    }
}
