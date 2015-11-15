package no.hiof.anl.laundrybooking.chart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

import no.hiof.anl.laundrybooking.R;

public class BarChartItem extends ChartItem {

   // private Typeface mTf;
    private String des="";

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);

        //mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    public BarChartItem(ChartData<?> cd, Context c, String description) {
        super(cd);

        des = description;

        //mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }


    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setDescription(des);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        XAxis xAxis = holder.chart.getXAxis();
        //xAxis.setPosition(XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //YAxis leftAxis = holder.chart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        //leftAxis.setLabelCount(5, false);
        //leftAxis.setSpaceTop(20f);

        //YAxis rightAxis = holder.chart.getAxisRight();
        //rightAxis.draw
        //rightAxis.setTypeface(mTf);
       // rightAxis.setLabelCount(5, false);
        //rightAxis.setSpaceTop(20f);

       // mChartData.setValueTypeface(mTf);

        // set data
        holder.chart.setData((BarData) mChartData);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }
}