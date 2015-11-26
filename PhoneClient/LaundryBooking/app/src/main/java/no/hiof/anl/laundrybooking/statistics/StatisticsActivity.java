package no.hiof.anl.laundrybooking.statistics;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import no.hiof.anl.laundrybooking.Database.Database;
import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class StatisticsActivity extends AppCompatActivity implements View.OnLongClickListener
{
    ArrayList<Chart> charts = new ArrayList<>();

    public static LinkedHashMap<String, LinkedHashMap<String, Integer>> data;

    private BarChart hoursBarChart;
    private BarChart dayBarChart;

    public static final int[] COLORS = {
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 140),
            Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157),


            Color.rgb(217, 80, 138),
            Color.rgb(254, 149, 7),
            //
            Color.rgb(106, 167, 134),
            Color.rgb(53, 194, 209),
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80),
            Color.rgb(193, 37, 82),
            Color.rgb(255, 102, 0),
            Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53),

            Color.rgb(207, 248, 246),
            Color.rgb(148, 212, 212),
            Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175),
            Color.rgb(42, 109, 130),

            Color.rgb(254, 247, 120),

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hoursBarChart = new BarChart(this);
        dayBarChart = new BarChart(this);

        data = Database.getStatistics();

        processingData();
        drawHoursChart();
        drawDaysChart();

        charts.add(hoursBarChart);
        charts.add(dayBarChart);

        final ListView lv = (ListView) findViewById(R.id.list_chart);
        ChartAdapter chartAdt = new ChartAdapter(this, charts);
        lv.setAdapter(chartAdt);
    }

    LinkedHashMap<String, Integer> days = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> hours = new LinkedHashMap<>();

    private void processingData()
    {
        for(String day : data.keySet())
        {
            LinkedHashMap<String, Integer> dayToHours = data.get(day);
            for(String hour : dayToHours.keySet())
            {
                int value = dayToHours.get(hour);

                int count = value;
                if(days.containsKey(day))
                    count += days.get(day);

                days.put(day, count);

                count = value;
                if(hours.containsKey(hour))
                    count += hours.get(hour);
                hours.put(hour, count);
            }
        }
    }

    private void drawHoursChart()
    {
        ArrayList<String> xVals = new ArrayList<>(hours.keySet());
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        int i = 0;
        for(String hour : hours.keySet())
        {
            int value = hours.get(hour);

            yVals.add(new BarEntry(value, i++));
        }
        BarDataSet dataSet = new BarDataSet(yVals, "");
        dataSet.setColors(COLORS);
        dataSets.add(dataSet);


        //moodBarChart.setOnChartValueSelectedListener(this);
        hoursBarChart.setDoubleTapToZoomEnabled(false);
        hoursBarChart.setDrawGridBackground(false);
        hoursBarChart.getAxisRight().setEnabled(false);
        hoursBarChart.getLegend().setEnabled(false);
        if(dataSets.isEmpty())
            hoursBarChart.setTouchEnabled(false);

        //hoursBarChart.setLongClickable(true);
       // moodBarChart.setOnLongClickListener(this);

        // moodBarChart.animateY(2000);
        BarData barData = new BarData(xVals, dataSets);

        hoursBarChart.setDescription("Number of Bookings by Hours");
        hoursBarChart.setData(barData);
        hoursBarChart.invalidate();

    }

    private void drawDaysChart()
    {
        ArrayList<String> xVals = new ArrayList<>(days.keySet());
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        int i = 0;
        for(String day : days.keySet())
        {
            int value = days.get(day);

            yVals.add(new BarEntry(value, i++));
        }
        BarDataSet dataSet = new BarDataSet(yVals, "");
        dataSet.setColors(COLORS);
        dataSets.add(dataSet);


        //moodBarChart.setOnChartValueSelectedListener(this);
        dayBarChart.setDoubleTapToZoomEnabled(false);
        dayBarChart.setDrawGridBackground(false);
        dayBarChart.getAxisRight().setEnabled(false);
        dayBarChart.getLegend().setEnabled(false);

        dayBarChart.setLongClickable(true);
        dayBarChart.setOnLongClickListener(this);

        if(dataSets.isEmpty())
            dayBarChart.setTouchEnabled(false);

        // moodBarChart.animateY(2000);
        BarData barData = new BarData(xVals, dataSets);

        dayBarChart.setDescription("Number of Bookings by Days");
        dayBarChart.setData(barData);
        dayBarChart.invalidate();

    }

    @Override
    public boolean onLongClick(View v)
    {
        BarChart chart =(BarChart)v;
        ArrayList<String> xVals = new ArrayList<>(days.keySet());


        Highlight[] highlights = chart.getHighlighted();
        int index = -1;
        if(highlights != null)
        {
            index = highlights[0].getXIndex();
            String day = xVals.get(index);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            BarChart tBarChart = drawDetailChartData(alertDialog.getContext(), day);

            tBarChart.setDescription(day);

            alertDialog.setView(tBarChart);
            alertDialog.show();

            //Log.e("onLongClick", "Index " + highlights[0].getXIndex());

        }

        return false;
    }

    private BarChart drawDetailChartData(Context context, String day)
    {
        ArrayList<String> xVals = new ArrayList<>(hours.keySet());

        BarChart barChart = new BarChart(context);


        LinkedHashMap<String, Integer> daysToHours = data.get(day);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        int i=0;
        for(String hour : daysToHours.keySet())
        {
            int value = daysToHours.get(hour);
            yVals1.add(new BarEntry(value, i++));
        }
        BarDataSet set = new BarDataSet(yVals1, "");
        set.setColors(COLORS);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(xVals, dataSets);
        barChart.setData(data);


        barChart.setMinimumHeight(this.getWindow().getDecorView().getHeight() / 2);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1500);


        return barChart;
    }
}
