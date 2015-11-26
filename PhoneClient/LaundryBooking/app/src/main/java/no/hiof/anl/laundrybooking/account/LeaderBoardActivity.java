package no.hiof.anl.laundrybooking.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import no.hiof.anl.laundrybooking.Database.Database;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class LeaderBoardActivity extends AppCompatActivity
{

    private ListView leaderboard_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView leaderboard_listview = (ListView) findViewById(R.id.leaderboard_listview);
        ArrayList<UserInfo> userInfos = Database.getUserList();

        Bundle b = getIntent().getExtras();
        int id = b.getInt("current_user_id");

        Collections.sort(userInfos, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo lhs, UserInfo rhs)
            {
                if(lhs.level == rhs.level)
                    return Integer.compare(rhs.progress_percent, lhs.progress_percent);
                return Integer.compare(rhs.level, lhs.level);
            }
        });

        AccountAdapter adapter = new AccountAdapter(this, userInfos, id);
        leaderboard_listview.setAdapter(adapter);

    }
}