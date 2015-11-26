package no.hiof.anl.laundrybooking.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;

import java.util.ArrayList;

import no.hiof.anl.laundrybooking.R;


/**
 * Created by An on 10/6/2015.
 */
public class ChartAdapter extends BaseAdapter
{

    private ArrayList<Chart> charts;
    private LayoutInflater chartInf;


    public ChartAdapter(Context c, ArrayList<Chart> theCharts)
    {
        charts = theCharts;
        chartInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return charts.size();
    }

    @Override
    public Object getItem(int position) {
        return charts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //map to song layout
        LinearLayout chartLay = (LinearLayout)chartInf.inflate(R.layout.chart_item, parent, false);
        //get title and artist views
        //TextView moodName = (TextView)moodLay.findViewById(R.id.mood_name);
       // ImageView moodImage = (ImageView)moodLay.findViewById(R.id.mood_img);

       // convertView.setMinimumHeight(parent.getMeasuredHeight());
       // chartLay.setMinimumHeight(parent.getMeasuredHeight());

        //int parentHeight = View.MeasureSpec.getSize(parent.getMeasuredHeight());


        Chart currChart = charts.get(position);
        currChart.setMinimumHeight(3 * parent.getMeasuredHeight() / 4);

        if(currChart.getParent() != null) {
            LinearLayout prt = (LinearLayout) currChart.getParent();
            prt.removeView(currChart);
        }
        currChart.animateY(1500);
        chartLay.addView(currChart);


        //moodName.setText(currMood.getName());

        //Picasso.with(parent.getContext()).load(currMood.getResrc()).transform(new CircleTransform()).into(moodImage);

        return chartLay;
    }
}
