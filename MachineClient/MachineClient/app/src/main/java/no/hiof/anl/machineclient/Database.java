package no.hiof.anl.machineclient;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TreeMap;

/**
 * Created by An on 11/18/2015.
 */
public class Database
{
    public static  TreeMap<Integer, UserInfo> PinToUser = new TreeMap<>();
    public static  TreeMap<Integer, UserInfo> RFIDToUser = new TreeMap<>();

    public TreeMap<String, ArrayList<BookingInfo>> allBookings = new TreeMap<>();


    private static BookingInfo booking;


    public static int max_hours = 2;
    public static int min_hours = 1;

    public static int booking_cash_per_hour = 20;
    public static int booking_points_per_hour = 200;

    public static void getAllUsers()
    {
        UserInfo user = new UserInfo("Thomas", 107761285, 11111, 450, 7000);
        addUser(user);

        user = new UserInfo("Alby", 22222, 22222, 350, 5000);
        addUser(user);

        user = new UserInfo("Chuck", 33333, 33333, 150, 1000);
        addUser(user);

        user = new UserInfo("Minho", 44444, 44444, 850, 4000);
        addUser(user);

        user = new UserInfo("Teresa", 55555, 55555, 850, 5000);
        addUser(user);

        user = new UserInfo("Newt", 66666, 66666, 550, 2000);
        addUser(user);

        user = new UserInfo("An Tran", 1462221659, 66666, 550, 2000);
        addUser(user);

        //booking = new BookingInfo(1, 22222, 1);
        //GregorianCalendar gregorianCalendar = new GregorianCalendar();
       // booking.updateTime(gregorianCalendar.getTimeInMillis() + 115 * 60 * 1000);

    }

    public static void updateAwardsPoints(BookingInfo bookinginfo)
    {
    }

    public static void addUser(UserInfo user)
    {
        RFIDToUser.put(user.id, user);
        PinToUser.put(user.pin, user);
    }

    public static UserInfo getUserByPin(int pin)
    {
        if(PinToUser.containsKey(pin))
            return  PinToUser.get(pin);
        return null;
    }

    public static UserInfo getUserByRFID(int id)
    {
        if(RFIDToUser.containsKey(id))
            return RFIDToUser.get(id);
        return null;
    }

    public static void updateBookingInfo(int machine_id, BookingInfo bookingInfo)
    {
        //booking.updateTime(bookingInfo.time);
        //booking = bookingInfo;
    }

    public static void addBookingInfo(int machine_id, BookingInfo bookingInfo)
    {

    }

    public static void updateMachineState(int machine_id, MainActivity.MACHINE_STATE state)
    {

    }

    public static BookingInfo getNextBooking(int machine_id, String date)
    {
        //return null;
        return null;
        //return booking;
    }

}
