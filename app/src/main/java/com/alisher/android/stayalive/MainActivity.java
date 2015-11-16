package com.alisher.android.stayalive;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaPlayer mPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int i = 0;
    private Toolbar toolbar;
    private Drawer result;
    private AccountHeader accountHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFloat();
        initSurface();
        createAccountHeader();
        initializeNavigationDrawer();
    }

    private void initializeNavigationDrawer() {

        result  = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Home")
                                .withIdentifier(1)
                                .withIcon(FontAwesome.Icon.faw_home),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Contacts")
                                .withIdentifier(2)
                                .withIcon(FontAwesome.Icon.faw_calendar),
                        new SecondaryDrawerItem()
                                .withName("Registration")
                                .withIdentifier(3)
                                .withIcon(FontAwesome.Icon.faw_user_plus),
                        new SecondaryDrawerItem()
                                .withName("Login")
                                .withIdentifier(4)
                                .withIcon(FontAwesome.Icon.faw_user))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 1:
                                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, "Contacts", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(MainActivity.this, "Registration", Toast.LENGTH_SHORT).show();
                            case 4:
                                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    private AccountHeader createAccountHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withName("Alisher")
                .withEmail("alikdemon@gmail.com")
                .withIcon(FontAwesome.Icon.faw_user);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header)
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

    private void initSurface(){
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
        mPlayer=new MediaPlayer();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFloat(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {
                    case 0:
                        mPlayer.setVolume(0, 1);
                        i = 1;
                        break;
                    case 1:
                        mPlayer.setVolume(0, 0);
                        i = 0;
                        break;
                }
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
