package no.hiof.anl.laundrybooking.statistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class StatisticsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
