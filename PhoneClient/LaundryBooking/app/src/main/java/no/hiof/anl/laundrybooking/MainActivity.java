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

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import no.hiof.anl.laundrybooking.Database.Database;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.account.Info;
import no.hiof.anl.laundrybooking.account.InfoAdapter;
import no.hiof.anl.laundrybooking.account.LeaderBoardActivity;
import no.hiof.anl.laundrybooking.account.ReportDialog;
import no.hiof.anl.laundrybooking.booking.BookingActivity;
import no.hiof.anl.laundrybooking.observation.ObservationActivity;
import no.hiof.anl.laundrybooking.picasso.CircleTransform;
import no.hiof.anl.laundrybooking.notification.NotificationActivity;
import no.hiof.anl.laundrybooking.statistics.StatisticsActivity;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements LoginDialog.NoticeDialogListener,
        ReportDialog.NoticeDialogListener
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

    private ListView info_listView;


    private Button leaderboard_button;
    private Button report_button;
    private ImageButton clear_all_message_button;
    private Button logout_button;

    private static ArrayList<Info> messages;

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

        info_listView  = (ListView) findViewById(R.id.info_listview);
        //TextView textView = new TextView(this);
        //textView.setText("Hello. I'm a header view");
        //info_listView.addHeaderView(textView);
        //updateMessages();



        leaderboard_button = (Button) findViewById(R.id.leaderboard_button);
        leaderboard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                i.putExtra("current_user_id", current_user.id);
                startActivity(i);
            }
        });

        report_button = (Button) findViewById(R.id.report_button);
        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog = new ReportDialog();
                reportDialog.setCancelable(false);
                reportDialog.show(getFragmentManager(), "Report Dialog");

            }
        });

        clear_all_message_button = (ImageButton) findViewById(R.id.clear_all_message_button);
        clear_all_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.clear();
                updateMessages();
            }
        });

        logout_button = (Button) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messages.clear();
                updateMessages();

                editor.putBoolean("isFirstTimeOpenApp", true);
                editor.commit();
                System.exit(0);
            }
        });

        Database.getAllUsers();

    }

    private void updateMessages()
    {
        if(messages.isEmpty() && current_user != null)
        {
            Info info = new Info("You have got to level " + current_user.level, true);
            messages.add(info);

            int awards = UserInfo.scale * (current_user.level - 1) / 2;
            info = new Info("You got " + awards + " points for getting to level " + current_user.level, true);
            messages.add(info);

            info = new Info("You lost 10 points for being reported", false);
            messages.add(info);
        }

        InfoAdapter adapter = new InfoAdapter(this, messages);
        info_listView.setAdapter(adapter);

        Gson gson = new Gson();
        String json = gson.toJson(messages);
        Log.i("updateMessages", json);
        editor.putString("messages", json);
        editor.commit();

    }

    private void displayUserInfo()
    {
        final ImageView home_avatar = (ImageView) findViewById(R.id.home_avatar);

        TextView home_username = (TextView) findViewById((R.id.home_username));
        home_username.setText(current_user.name);

        TextView home_balance = (TextView) findViewById((R.id.home_balance));
        home_balance.setText(current_user.balance + " NOK");

        TextView home_points = (TextView) findViewById((R.id.home_points));
        home_points.setText(current_user.points + " Points");

        TextView home_level = (TextView) findViewById((R.id.home_level));
        home_level.setText("Level " + current_user.level);

        RoundCornerProgressBar progress_bar = (RoundCornerProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setProgress(current_user.progress_percent);

        if(current_user.avatar == null)
             Picasso.with(this).load(R.drawable.avatar).transform(new CircleTransform()).into(avatar);
        else
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getBaseContext()).load(current_user.avatar).transform(new CircleTransform()).into(home_avatar);
                }
            });


    }


    @Override
    protected void onResume()
    {
        super.onResume();
        boolean isFirstTimeOpenApp = settings.getBoolean("isFirstTimeOpenApp", true);

        if(isFirstTimeOpenApp)
        {
            messages = new ArrayList<>();
            updateMessages();
            DialogFragment loginDialog = new LoginDialog();
            loginDialog.show(getFragmentManager(), "Fisrt Time Using");
            loginDialog.setCancelable(false);
        }
        else
        {
            int user_pin = settings.getInt("userPin", 0);
            //String json = settings.getString("userInfo", null);
            //
            current_user = Database.getUserByPin(user_pin);
            setDrawerUserInfo();
            displayUserInfo();

            String json = settings.getString("messages", null);
            //Log.i("Messages", json);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Info>>(){}.getType();
            messages = gson.fromJson(json, collectionType);
            updateMessages();
            displayUserInfo();
        }

    }

    @Override
    public void onDialogPositiveClick(LoginDialog dialog)
    {
        editor.putBoolean("isFirstTimeOpenApp", false);


        LoginDialog loginDialog = dialog;

        current_user = loginDialog.getUserInfo();

        //Gson gson = new Gson();

        editor.putInt("userPin", current_user.pin);
        editor.commit();

        setDrawerUserInfo();

        loginDialog.dismiss();
        displayUserInfo();
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
                        i = new Intent(getApplicationContext(), NotificationActivity.class);
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
        // gson = new Gson();
        //editor.putString("userInfo", gson.toJson(current_user));
        //editor.commit();
    }

    @Override
    public void onDialogPositiveClick(ReportDialog dialog)
    {
        int awards = 10;

        Info info = new Info("You got " + awards + " points for reporting", true);
        messages.add(info);
        addUserPoints(awards);
        updateMessages();
        displayUserInfo();

        Database.UpdateUserInfo(current_user);

        dialog.dismiss();
    }

    private void addUserPoints(int points)
    {
        current_user.points += points;
        current_user.progress_points += points;
        int points_to_new_level = UserInfo.scale * current_user.level;
        if(current_user.progress_points > points_to_new_level)
        {
            current_user.level++;
            current_user.progress_points = current_user.progress_points - points_to_new_level;

            Info info = new Info("You have got to level " + current_user.level, true);
            messages.add(info);

            int awards = UserInfo.scale *(current_user.level - 1)/2;
            info = new Info("You got " + awards + " points for getting to level " + current_user.level, true);
            messages.add(info);

            addUserPoints(awards);
        }

        current_user.progress_percent = current_user.progress_points * 100 /(UserInfo.scale * current_user.level);

    }


}
