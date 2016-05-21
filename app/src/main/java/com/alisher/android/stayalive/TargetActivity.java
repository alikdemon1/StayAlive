package com.alisher.android.stayalive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisher.android.stayalive.bar.SimpleScannerActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TargetActivity extends MainActivity {
    private static final String TAG = "TARGET_ACTIVITY";
    ParseUser currentUser = ParseUser.getCurrentUser();
    private TextView nameTV, groupTV, genderTV;
    private EditText codeToKill;
    private String targetUsername, targetGroup, targetGender;
    public String targetObjectId;
    private ImageView imageIV;

    private ParseQuery<ParseObject> parseObjectParseQuery, parseObjectParseQuerySecond, parseStayAliveAchievement;
    private String usersCurId;
    private int killingsToAdd;
    public String targetsHunterId, curUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initContent();
        getTarget();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(TargetActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        nameTV = (TextView) findViewById(R.id.textName);
        groupTV = (TextView) findViewById(R.id.textGroup);
        genderTV = (TextView) findViewById(R.id.textGender);
        codeToKill = (EditText) findViewById(R.id.editTextTarget);
        imageIV = (ImageView) findViewById(R.id.imageTarget);
    }

    private void getTarget() {
        final ParseQuery<ParseUser> queryUsername = ParseUser.getQuery();
        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("StayAliveUsers");
        queryUser.whereEqualTo("user_id", currentUser.getObjectId());
        ParseQuery<ParseObject> queryTarget = ParseQuery.getQuery("StayAliveUsers");
        queryTarget.whereMatchesKeyInQuery("hunter_id", "objectId", queryUser);
        queryTarget.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        try {
                            targetUsername = queryUsername.whereEqualTo("objectId", o.getString("user_id")).getFirst().getUsername();
                            targetGroup = o.getString("group");
                            targetGender = o.getString("gender");
                            targetObjectId = o.getObjectId();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (targetUsername != null){
                        nameTV.setText(targetUsername);
                        groupTV.setText(targetGroup);
                        genderTV.setText(targetGender);
                    } else {
                        nameTV.setText("You don't have target yet:)");
                        groupTV.setText("You don't have target yet:)");
                        genderTV.setText("You don't have target yet:)");
                    }


                } else
                    Log.d("exeptionName", e.getMessage());
            }
        });
    }

    public void imageTargetShow(View view) {
        Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
    }

    public void buttonKilledClicked(View v) {
        Intent intent = new Intent(TargetActivity.this, SimpleScannerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String value = data.getStringExtra("qrCode");
            Log.d(TAG, "" + value);
            if(value == null){
                Toast.makeText(TargetActivity.this, "Увы вы никого не убили", Toast.LENGTH_SHORT).show();
            } else  {
                initNewTarget(value);
            }
        } else {
            Log.d("HELLO", "HELLO");
        }
    }

    private void initNewTarget(final String idToKill) {
        parseObjectParseQuery = ParseQuery.getQuery("StayAliveUsers");
        parseObjectParseQuerySecond = ParseQuery.getQuery("StayAliveUsers");
        parseStayAliveAchievement = ParseQuery.getQuery("UserAchievements");
        usersCurId = ParseUser.getCurrentUser().getObjectId().trim();
        Toast.makeText(TargetActivity.this, "ID: " + idToKill, Toast.LENGTH_LONG).show();
        parseObjectParseQuery.whereEqualTo("user_id", idToKill);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            public ParseQuery parseObjectParseQueryThird = ParseQuery.getQuery("StayAliveUsers");

            @Override
            public void done(List<ParseObject> list, ParseException exp) {
                if (exp == null) {
                    final ParseObject parseObject = list.get(0);
                    targetsHunterId = parseObject.get("hunter_id").toString().trim();
                    Toast.makeText(TargetActivity.this, "targetsHunterId:" + targetsHunterId, Toast.LENGTH_SHORT).show();
                    parseObjectParseQuerySecond.whereEqualTo("user_id", usersCurId);
                    parseObjectParseQuerySecond.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (ParseObject myObj : list) {
                                    curUserId = myObj.getObjectId();
                                }
                                Toast.makeText(TargetActivity.this, "curUserID:" + curUserId + " | " + targetsHunterId + " | " + usersCurId, Toast.LENGTH_LONG).show();
                                if (targetsHunterId.equals(curUserId)) {
                                    Toast.makeText(TargetActivity.this, "Right person to slay", Toast.LENGTH_SHORT).show();
                                    killingsToAdd = 0;
                                    parseStayAliveAchievement.whereEqualTo("user_id", parseObject.get("user_id").toString().trim());
                                    parseStayAliveAchievement.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (e == null) {
                                                for (ParseObject myParse : list) {
                                                    killingsToAdd = myParse.getNumber("killings").intValue();
                                                    myParse.put("isDead", true);
                                                    myParse.saveInBackground();
                                                }
                                            } else {
                                            }
                                        }
                                    });
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            parseStayAliveAchievement.whereEqualTo("user_id", usersCurId);
                                            /*myColection.add(usersCurId);*/
                                            parseStayAliveAchievement.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject myParse : list) {
                                                            int toAdd = killingsToAdd + 1 + myParse.getNumber("killings").intValue();
                                                            int moneyToAdd = myParse.getNumber("money").intValue() + 50;
                                                            myParse.put("killings", toAdd);
                                                            myParse.put("money", moneyToAdd);
                                                            myParse.saveInBackground();
                                                        }
                                                        Toast.makeText(TargetActivity.this, "1111", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(TargetActivity.this, e.getMessage() + " 1111", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }, 2000);
                                    parseObjectParseQueryThird.whereEqualTo("user_id", idToKill);
                                    parseObjectParseQueryThird.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (e == null) {
                                                for (ParseObject myParse : list) {
                                                    String deadTargetId = myParse.getObjectId();
                                                    Log.d("NEW TARGET", deadTargetId);
                                                    ParseQuery<ParseObject> deadTargetQuery = ParseQuery.getQuery("StayAliveUsers");
                                                    deadTargetQuery.whereEqualTo("hunter_id", deadTargetId);
                                                    deadTargetQuery.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> list, ParseException e) {
                                                            if (e == null) {
                                                                for (ParseObject p : list) {
                                                                    p.put("hunter_id", curUserId);
                                                                    p.saveEventually();
                                                                }
                                                            } else {

                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                Toast.makeText(TargetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    ParseQuery<ParseObject> parseObjectParseQueryFourth = ParseQuery.getQuery("StayAliveUsers");
                                    parseObjectParseQueryFourth.whereEqualTo("user_id", idToKill);
                                    parseObjectParseQueryFourth.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (e == null) {
                                                for (ParseObject myParse : list) {
                                                    myParse.deleteEventually();
                                                }
                                                Toast.makeText(TargetActivity.this, "New P_counters are SET", Toast.LENGTH_SHORT).show();
                                            } else {

                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(TargetActivity.this, exp.getMessage() + " 2", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}