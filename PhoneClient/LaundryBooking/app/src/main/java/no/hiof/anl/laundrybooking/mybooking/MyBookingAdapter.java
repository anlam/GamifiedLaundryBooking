package no.hiof.anl.laundrybooking.mybooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import no.hiof.anl.laundrybooking.Database.BookingInfo;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.R;

/**
 * Created by Stone on 12/1/2015.
 */
public class MyBookingAdapter  extends BaseAdapter {
    private LayoutInflater mLayoutInf;
    ArrayList<BookingInfo> infos = new ArrayList<>();

    public MyBookingAdapter(Context c, ArrayList<BookingInfo> infos)
    {
        this.infos = infos;
        mLayoutInf = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= 0 && position < infos.size())
            return infos.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BookingInfo info = (BookingInfo) getItem(position);
        LinearLayout view = (LinearLayout) mLayoutInf.inflate(R.layout.booking_list_item, parent, false);

        TextView orderNumber = (TextView) view.findViewById(R.id.order_number);
        orderNumber.setText(String.valueOf(position + 1));

        TextView tvMachine = (TextView) view.findViewById(R.id.tvMachine);
        tvMachine.setText("Machine " + info.machine_id);

        TextView bookingTime = (TextView) view.findViewById(R.id.tvBookingDate);
        bookingTime.setText(info.getBookingDateInString());
        return view;
    }
}
