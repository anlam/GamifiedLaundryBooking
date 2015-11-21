package no.hiof.anl.laundrybooking.account;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class InfoAdapter extends BaseAdapter
{

    ArrayList<Info> infos = new ArrayList<>();
    private LayoutInflater mLayoutInf;

    public InfoAdapter(Context c, ArrayList<Info> infos)
    {
        this.infos = infos;
        mLayoutInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return infos.size();

    }

    @Override
    public Object getItem(int position)
    {
        if(position >= 0 && position < infos.size())
            return infos.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout view = (LinearLayout) mLayoutInf.inflate(R.layout.info_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.info_item);
        Info info = infos.get(position);
        textView.setText(info.message);
        textView.setTextColor(Color.GREEN);
        if(!info.isGoodNews)
            textView.setTextColor(Color.RED);

        return view;
    }
}
