package no.hiof.anl.machineclient;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by An on 11/19/2015.
 */
public class BookingInfo implements Serializable
{
    public transient GregorianCalendar date;

    public long time;
    public double duration; //in hours
    public int userPin;
    public boolean isDone;
    public long id;
    public int machine_id;

    public boolean isBuyByCash;
    public int points_earn;

    public BookingInfo()
    {
        date = new GregorianCalendar();
        if(time > 0)
            date.setTimeInMillis(time);
    }

    public BookingInfo(double duration, int userPin, int machine_id)
    {
        date = new GregorianCalendar();
        time = date.getTimeInMillis();
        this.duration = duration;
        this.userPin = userPin;
        this.isDone = false;
        this.id = time;
        this.machine_id = machine_id;

        isBuyByCash = true;
        points_earn = 0;
    }

    public void updateTime(long time)
    {
        this.time = time;
        date.setTimeInMillis(time);
    }

    public String getEndTimeFormat(long start)
    {
        //Calendar now = Calendar.getInstance();
        //Locale locale = Locale.getDefault();

        long end = getEndTime(start);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        //Log.i("end", String.valueOf(end));
        //Log.i("now", String.valueOf(gregorianCalendar.getTimeInMillis()));
        //Log.i("end - now", String.valueOf(end -  gregorianCalendar.getTimeInMillis()));


        gregorianCalendar.setTimeInMillis(end);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(gregorianCalendar.getTime());

        //gregorianCalendar.getDisplayName(Calendar.)
    }

    public long getEndTime(long start)
    {
        long end = time + (long)(duration*60*60*1000);
        long timeahead = getTimeAhead(start);
        if(timeahead > 0)
        {
            end = (long)(duration*60*60*1000) + start;
        }


        return end;
    }

    public long getTimeAhead(long start)
    {
        return time - start;
    }

    public long getRemainingTime(long now)
    {
        return time + (long) (duration*60*60*1000) - now;
    }

    public static String getToDate()
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(gregorianCalendar.getTime());
    }

    public static String getTommorrow()
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        long time = gregorianCalendar.getTimeInMillis();
        gregorianCalendar.setTimeInMillis(time + 24*60*60*1000 + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(gregorianCalendar.getTime());
    }


}
