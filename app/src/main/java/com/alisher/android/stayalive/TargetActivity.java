package com.alisher.android.stayalive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class TargetActivity extends MainActivity {
    ParseUser currentUser= ParseUser.getCurrentUser();
    private TextView nameTV,groupTV,genderTV;
    private EditText codeToKill;
    private String targetUsername,targetGroup,targetGender;
    public String targetObjectId,currentUserGameId;
    private ImageView imageIV;

    String name, pass, targetId;
    ParseObject myCurrentUser;
    private String newTarget;
    private String curUserID;

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
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(TargetActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        nameTV = (TextView) findViewById(R.id.textName);
        groupTV = (TextView) findViewById(R.id.textGroup);
        genderTV = (TextView) findViewById(R.id.textGender);
        codeToKill=(EditText)findViewById(R.id.editTextTarget);
        imageIV=(ImageView)findViewById(R.id.imageTarget);
    }

    private void getTarget(){
        ParseQuery<ParseObject> queryUser=ParseQuery.getQuery("StayAliveUsers");
        queryUser.whereEqualTo("user_id", currentUser.getObjectId());
        ParseQuery<ParseObject> queryTarget = ParseQuery.getQuery("StayAliveUsers");
        queryTarget.whereMatchesKeyInQuery("pointer", "objectId", queryUser);
        queryTarget.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        targetUsername = o.getString("username");
                        targetGroup = o.getString("group");
                        targetGender =o.getString("gender");
                        targetObjectId=o.getObjectId();
                    }

                    nameTV.setText(targetUsername);
                    groupTV.setText(targetGroup);
                    genderTV.setText(targetGender);
                } else
                    Log.d("exeptionName", e.getMessage());

            }
        });
    }

    public void imageTargetShow(View view) {
        Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
    }

    public void buttonKilledClicked(View v){
        if(codeToKill.getText().toString().equals(targetObjectId)){
            initNewTarget();
            Toast.makeText(this, "Congargulation, You have new Target!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Wrong code from Target's app!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initNewTarget(){
        ParseQuery<ParseObject> parseUserParseQuery = new ParseQuery<ParseObject>("StayAliveUsers");
        parseUserParseQuery.whereEqualTo("user_id",currentUser.getObjectId());
        parseUserParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject myParseObj : list) {
                        myCurrentUser = myParseObj;
                        curUserID = myCurrentUser.getObjectId();
                        //Toast.makeText(Registration.this, "Current user ID:" + myCurrentUser.getObjectId(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        final ParseQuery<ParseObject> userStayAlive = new ParseQuery<ParseObject>("StayAliveUsers");
        final ParseQuery<ParseObject> parseNewTargetQuery = new ParseQuery<ParseObject>("StayAliveUsers");
        Log.d("TARGERID,", targetObjectId);
        userStayAlive.whereEqualTo("objectId", targetObjectId);
        userStayAlive.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        parseObject.put("isDead", true);
                        parseObject.saveInBackground();
                    }
                    //Toast.makeText(Registration.this, "Target is Dead", Toast.LENGTH_SHORT).show();
                            /*ParseQuery<ParseObject> newParseQuery = new ParseQuery<ParseObject>("StayAliveUsers");
                            newParseQuery.whereEqualTo("objectId",targetId);
                            parseNewTargetQuery.whereMatchesKeyInQuery("hunter_id","objectId", newParseQuery);*/
                    parseNewTargetQuery.whereEqualTo("pointer", targetId);
                    parseNewTargetQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (ParseObject parseObject : list) {
                                    //Toast.makeText(Registration.this, "My Current User:" + curUserID, Toast.LENGTH_SHORT).show();
                                    //parseObject.remove("hunter_id");
                                    //parseObject.put("hunter_id", ParseObject.createWithoutData("StayAliveUsers", curUserID));
                                    parseObject.put("pointer", curUserID);
                                    parseObject.saveInBackground();
                                }
                                Toast.makeText(TargetActivity.this, "new Target is Set", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TargetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(TargetActivity.this, "Target is NOT Dead", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
