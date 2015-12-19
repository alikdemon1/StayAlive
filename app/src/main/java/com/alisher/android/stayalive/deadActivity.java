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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DeadActivity extends AppCompatActivity {
    private TextView codeToKillTV;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String currentUserGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        codeToKillTV=(TextView)findViewById(R.id.objectIdText);
        getCurrentGameUserId();
    }

    private void getCurrentGameUserId(){
        ParseQuery<ParseObject> queryUser=ParseQuery.getQuery("StayAliveUsers");
        queryUser.whereEqualTo("user_id", currentUser.getObjectId());
        queryUser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject p : list) {
                        Log.d("CURRENT USER GAME ID", p.getObjectId());
                        currentUserGameId = p.getObjectId();
                    }
                    codeToKillTV.setText(currentUserGameId);
                } else {

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
}
