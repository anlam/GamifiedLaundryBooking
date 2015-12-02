package no.hiof.anl.laundrybooking.mybooking;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import no.hiof.anl.laundrybooking.Database.BookingInfo;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.R;

public class MyBookingActivity extends AppCompatActivity {

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        ListView listView1 = (ListView) findViewById(R.id.listView1);

        String json = settings.getString("bookingInfos", null);
        //Log.i("Messages", json);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<BookingInfo>>(){}.getType();
        ArrayList<BookingInfo> bookingInfos = gson.fromJson(json, collectionType);

        if(bookingInfos != null){
            Collections.sort(bookingInfos, new Comparator<BookingInfo>() {
                @Override
                public int compare(BookingInfo lhs, BookingInfo rhs) {
                    return rhs.getDate().compareTo(rhs.getDate());
                }
            });
        }
        else {
            bookingInfos = new ArrayList<BookingInfo>();
        }
        MyBookingAdapter adapter = new MyBookingAdapter(this, bookingInfos);

        listView1.setAdapter(adapter);
    }
}
