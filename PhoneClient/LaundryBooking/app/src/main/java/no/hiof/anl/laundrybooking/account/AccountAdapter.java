package no.hiof.anl.laundrybooking.account;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.R;
import no.hiof.anl.laundrybooking.picasso.CircleTransform;

/**
 * Created by An on 11/26/2015.
 */
public class AccountAdapter extends BaseAdapter
{
    ArrayList<UserInfo> infos = new ArrayList<>();
    private LayoutInflater mLayoutInf;
    Handler mHandler = new Handler();
    private int currentUser;

    public AccountAdapter(Context c, ArrayList<UserInfo> infos, int currentUser)
    {
        this.infos = infos;
        mLayoutInf = LayoutInflater.from(c);
        this.currentUser = currentUser;
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
        final UserInfo info = (UserInfo) getItem(position);
        LinearLayout view = (LinearLayout) mLayoutInf.inflate(R.layout.account_list_item, parent, false);

        TextView ranking_textbox_list_item = (TextView) view.findViewById(R.id.ranking_textbox_list_item);
        ranking_textbox_list_item.setText(String.valueOf(position + 1));

        TextView username_textbox_list_item = (TextView) view.findViewById(R.id.username_textbox_list_item);
        username_textbox_list_item.setText(info.name);

        TextView level_textbox_list_item = (TextView) view.findViewById(R.id.level_textbox_list_item);
        level_textbox_list_item.setText("Level " + info.level);

        RoundCornerProgressBar progress_bar_list_item = (RoundCornerProgressBar) view.findViewById(R.id.progress_bar_list_item);
        progress_bar_list_item.setProgress((float) info.progress_percent);

        final ImageView avatar_list_item = (ImageView) view.findViewById(R.id.avatar_list_item);

        if(info.avatar == null)
            Picasso.with(mLayoutInf.getContext()).load(R.drawable.avatar).transform(new CircleTransform()).into(avatar_list_item);
        else
            mHandler.post(new Runnable() {
                String str = info.avatar;
                @Override
                public void run() {
                    Picasso.with(mLayoutInf.getContext()).load(str).transform(new CircleTransform()).into(avatar_list_item);
                }
            });

        if(currentUser == info.pin)
            view.setBackgroundColor(Color.YELLOW);
        return view;
    }
}
