package com.alisher.android.stayalive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.alisher.android.stayalive.event.EventActivity;
import com.alisher.android.stayalive.news.NewsActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaPlayer mPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int i = 0;
    private Toolbar toolbar;
    private Drawer result;
    private AccountHeader accountHeader;
    private Bitmap photo;
    private ProfileDrawerItem profile = new ProfileDrawerItem();
    private ParseUser user = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFloat();
        initSurface();
        createAccountHeader();
        initializeNavigationDrawer();
        initIcon();
        setAnimationNews();
    }

    private void setAnimationNews() {
        Animation mAnimation = new TranslateAnimation(760, -520,
                0, 0);
        mAnimation.setDuration(10000);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(Animation.INFINITE);

        TextView tv = (TextView) findViewById(R.id.newsText);
        tv.setAnimation(mAnimation);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "HOT NEWS!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeNavigationDrawer() {
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Balance")
                                .withBadge("100")
                                .withIcon(FontAwesome.Icon.faw_money)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.BLACK).withColorRes(R.color.colorPrimary)),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Target")
                                .withIdentifier(1)
                                .withIcon(FontAwesome.Icon.faw_crosshairs),
                        new SecondaryDrawerItem()
                                .withName("My Hunter")
                                .withIdentifier(2)
                                .withIcon(FontAwesome.Icon.faw_user_secret),
                        new SecondaryDrawerItem()
                                .withName("News")
                                .withIdentifier(3)
                                .withIcon(FontAwesome.Icon.faw_bullhorn),
                        new SecondaryDrawerItem()
                                .withName("Events")
                                .withIdentifier(8)
                                .withIcon(FontAwesome.Icon.faw_hacker_news),
                        new SecondaryDrawerItem()
                                .withName("Configuration")
                                .withIdentifier(4)
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withName("I was killed")
                                .withIdentifier(5)
                                .withIcon(FontAwesome.Icon.faw_frown_o))
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("About").withIdentifier(6),
                        new SecondaryDrawerItem().withName("Sign out").withIdentifier(7)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 1:
                                Intent intent = new Intent(MainActivity.this, TargetActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Target", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, "My Hunter", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HunterActivity.class));
                                break;
                            case 3:
                                Toast.makeText(MainActivity.this, "News", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                                break;
                            case 4:
                                Toast.makeText(MainActivity.this, "Configuration", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                Toast.makeText(MainActivity.this, "I was killed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), DeadActivity.class));
                                break;
                            case 6:
                                Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                                break;
                            case 7:
                                Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
                                if (user != null) {
                                    ParseUser.logOut();
                                    user = ParseUser.getCurrentUser();
                                }
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                break;
                            case 8:
                                Toast.makeText(MainActivity.this, "Events", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), EventActivity.class));
                                break;
                        }
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private AccountHeader createAccountHeader() {
        if (user != null) {
            profile = new ProfileDrawerItem()
                    .withName(user.getUsername())
                    .withEmail(user.getEmail());
        } else {
            profile = new ProfileDrawerItem()
                    .withName("Alisher")
                    .withEmail("alikdemon@gmail.com")
                    .withIcon(photo);
        }

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withSelectionListEnabled(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        Drawable d = new BitmapDrawable(getResources(), photo);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Выберите картинку")
                                .setIcon(d)
                                .setPositiveButton("Из камеры",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_SHORT).show();
                                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                startActivityForResult(camera, 188);
                                            }
                                        })
                                .setNegativeButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Toast.makeText(MainActivity.this, "NO", Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return false;
                    }
                })
                .build();

        return accountHeader;
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void initSurface() {
        getWindow().setFormat(PixelFormat.UNKNOWN);
        //make a reference to the SurfaceVeiw
        surfaceView = (SurfaceView) findViewById(R.id.firstSurface);
        //get surface holder
        surfaceHolder = surfaceView.getHolder();
        //set fixed surface size
        surfaceHolder.setFixedSize(176, 144);
        //set Callback interface to the surface holder
        surfaceHolder.addCallback(this);
        //create a MediaPlayer instance
        mPlayer = new MediaPlayer();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
    }

    private void initFloat() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {
                    case 0:
                        fab.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                        mPlayer.setVolume(0, 1);
                        i = 1;
                        break;
                    case 1:
                        fab.setImageResource(android.R.drawable.ic_lock_silent_mode);
                        mPlayer.setVolume(0, 0);
                        i = 0;
                        break;
                }
            }
        });
    }

    private void initIcon() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    ParseFile fileObject = (ParseFile) user.get("icon");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profile.withIcon(photo);
                                accountHeader.addProfiles(profile);
                            } else {

                            }
                        }
                    });
                } else {

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 188 && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            profile.withIcon(photo);
            accountHeader.addProfiles(profile);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageForUpload = stream.toByteArray();
            ParseFile file = new ParseFile("default.png", imageForUpload);
            file.saveInBackground();
            user.put("icon", file);
            user.saveInBackground();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(surfaceHolder);
        try {
            //set data source to the video file
            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.stay));
            mPlayer.setVolume(0, 0);
            //prepare the MediaPlayer
            mPlayer.prepare();
            mPlayer.setLooping(true);
            //start playing the video
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem event_item) {
//        // Handle action bar event_item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = event_item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(event_item);
//    }
}
