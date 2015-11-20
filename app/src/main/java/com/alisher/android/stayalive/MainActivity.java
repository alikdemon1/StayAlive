package com.alisher.android.stayalive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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
    private Bitmap photo;
    private ProfileDrawerItem profile = new ProfileDrawerItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFloat();
        initCamera();
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
                                .withName("Target")
                                .withIdentifier(1)
                                .withIcon(FontAwesome.Icon.faw_crosshairs),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("My Hunter")
                                .withIdentifier(2)
                                .withIcon(FontAwesome.Icon.faw_user_secret),
                        new SecondaryDrawerItem()
                                .withName("News")
                                .withIdentifier(3)
                                .withIcon(FontAwesome.Icon.faw_bullhorn),
                        new SecondaryDrawerItem()
                                .withName("Configuration")
                                .withIdentifier(4)
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withName("I was killed")
                                .withIdentifier(5)
                                .withIcon(FontAwesome.Icon.faw_frown_o))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 1:
                                Intent intent= new Intent(MainActivity.this,TargetActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Target", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, "My Hunter", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(MainActivity.this, "News", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(MainActivity.this, "Configuration", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                Toast.makeText(MainActivity.this, "I was killed", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                })
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private AccountHeader createAccountHeader() {
        profile = new ProfileDrawerItem()
                .withName("Alisher")
                .withEmail("alikdemon@gmail.com")
                .withIcon(photo);

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
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
    }

    private void initFloat(){
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

    private void initCamera(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,188);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==188 && resultCode==RESULT_OK){
            photo = (Bitmap)data.getExtras().get("data");
            profile.withIcon(photo);
            accountHeader.addProfiles(profile);
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
