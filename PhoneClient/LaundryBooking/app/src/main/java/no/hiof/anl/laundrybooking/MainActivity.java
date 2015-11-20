/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.hiof.anl.laundrybooking;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.webkit.MimeTypeMap;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import no.hiof.anl.laundrybooking.Database.Database;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.booking.BookingActivity;
import no.hiof.anl.laundrybooking.observation.ObservationActivity;
import no.hiof.anl.laundrybooking.picasso.CircleTransform;
import no.hiof.anl.laundrybooking.settings.LoginDialog;
import no.hiof.anl.laundrybooking.settings.SettingsActivity;
import no.hiof.anl.laundrybooking.statistics.StatisticsActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements LoginDialog.NoticeDialogListener
{
    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";

    private DrawerLayout drawerLayout;
    private TextView drawerName;
    private UserInfo current_user;
    private ImageView avatar;

    private View content;

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        setupDrawerLayout();


        content = findViewById(R.id.content);
        avatar = (ImageView) findViewById(R.id.avatar);
        Picasso.with(this).load(R.drawable.avatar).transform(new CircleTransform()).into(avatar);

        settings = this.getPreferences(MODE_PRIVATE);
        editor = settings.edit();

        //setDrawerUserInfo();
        Database.getAllUsers();

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        boolean isFirstTimeOpenApp = settings.getBoolean("isFirstTimeOpenApp", true);

        if(isFirstTimeOpenApp)
        {
            DialogFragment loginDialog = new LoginDialog();
            loginDialog.show(getFragmentManager(), "Fisrt Time Using");
            loginDialog.setCancelable(false);
        }
        else
        {
            String json = settings.getString("userInfo", null);
            Gson gson = new Gson();
            current_user = gson.fromJson(json, UserInfo.class);
            setDrawerUserInfo();
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        editor.putBoolean("isFirstTimeOpenApp", false);


        LoginDialog loginDialog = (LoginDialog) dialog;

        current_user = loginDialog.getUserInfo();

        Gson gson = new Gson();

        editor.putString("userInfo", gson.toJson(current_user));
        editor.commit();

        setDrawerUserInfo();

        loginDialog.dismiss();
    }

    private void setDrawerUserInfo()
    {
        if(current_user != null) {
            drawerName.setText(current_user.name);
            if(current_user.avatar != null)
            {
                mHandler.post(mUpdateAvatar);
            }
        }
        //drawerEmail.setText(userEmail);
    }

    Runnable mUpdateAvatar = new Runnable() {
        @Override
        public void run() {
            Picasso.with(getBaseContext()).load(current_user.avatar).transform(new CircleTransform()).into(avatar);
        }
    };


    /**
     * Receiving song index from playlist view
     * and play the song
     * */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                menuItem.setChecked(false);
                switch (menuItem.getItemId()) {
                    case R.id.drawer_home:
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.drawer_statistics:
                        Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.drawer_booking:
                        i = new Intent(getApplicationContext(), BookingActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.drawer_observation:
                        i = new Intent(getApplicationContext(), ObservationActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.drawer_setting:
                        i = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.drawer_about:
                        Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return true;

                }
            }
        });

        drawerName = (TextView) drawerLayout.findViewById(R.id.drawer_name);
        //drawerEmail = (TextView) drawerLayout.findViewById(R.id.drawer_email);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
