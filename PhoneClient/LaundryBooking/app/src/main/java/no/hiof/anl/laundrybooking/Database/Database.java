package no.hiof.anl.laundrybooking.Database;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import no.hiof.anl.laundrybooking.Database.BookingInfo;
import no.hiof.anl.laundrybooking.Database.UserInfo;

/**
 * Created by An on 11/18/2015.
 */
public class Database
{

    public enum ACTIVITY_STATE {LOGIN , BOOKING, READY, USING}
    public enum MACHINE_STATE {AVAILABLE, RUNNING, NOT_AVALABLE}

    public static  TreeMap<Integer, UserInfo> PinToUser = new TreeMap<>();
    public static  TreeMap<Integer, UserInfo> RFIDToUser = new TreeMap<>();

    public TreeMap<String, ArrayList<BookingInfo>> allBookings = new TreeMap<>();


    public static int max_hours = 2;
    public static int min_hours = 1;

    public static int booking_cash_per_hour = 20;
    public static int booking_points_per_hour = 200;

    public static void getAllUsers()
    {
        UserInfo user = new UserInfo("Thomas", 107761285, 11111, 450, 7000, "https://s-media-cache-ak0.pinimg.com/736x/84/00/5c/84005cfa9fd840b18dbf04f600f0aee0.jpg", 4, 50);
        addUser(user);

        user = new UserInfo("Alby", 22222, 22222, 350, 5000, "http://images6.fanpop.com/image/photos/37600000/Alby-the-maze-runner-37638950-1200-1000.png", 5, 70);
        addUser(user);

        user = new UserInfo("Chuck", 33333, 33333, 150, 1000, "http://img2.wikia.nocookie.net/__cb20141018073627/mazerunner/images/8/8d/Chuck-the-maze-runner-37624809-1200-1000.png", 8, 70);
        addUser(user);

        user = new UserInfo("Minho", 44444, 44444, 850, 4000, "http://vignette2.wikia.nocookie.net/mazerunner/images/c/c9/TMRposter-minho.jpg/revision/latest/top-crop/width/240/height/240?cb=20140916191235", 1, 20);
        addUser(user);

        user = new UserInfo("Teresa", 55555, 55555, 850, 5000, "http://www.ehiyo.com/wp-content/uploads/2014/11/teresa-the-maze-runner-10866.jpg", 3, 60);
        addUser(user);

        user = new UserInfo("Newt", 66666, 66666, 550, 2000, "http://vignette3.wikia.nocookie.net/mazerunner/images/7/78/Glader_Newt.png/revision/latest?cb=20140731183732", 9, 80);
        addUser(user);

        user = new UserInfo("An Lam", 77777, 77777, 550, 2000, null, 2, 25);
        addUser(user);

    }

    public static ArrayList<UserInfo> getUserList()
    {
        return new ArrayList<>(PinToUser.values());
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

    public static void updateMachineState(int machine_id, MACHINE_STATE state)
    {

    }

    public static BookingInfo getNextBooking(int machine_id, String date)
    {
        return null;

    }

    public static void UpdateUserInfo(UserInfo userInfo)
    {

    }

    public static LinkedHashMap<String, LinkedHashMap<String, Integer>> getStatistics()
    {
        LinkedHashMap<String, LinkedHashMap<String, Integer>> ret = new LinkedHashMap<>();

        //Monday
        LinkedHashMap<String, Integer> day = new LinkedHashMap<>();
        day.put("0-2", 5);
        day.put("2-4", 2);
        day.put("4-6", 1);
        day.put("6-8", 10);
        day.put("8-10", 10);
        day.put("10-12", 13);
        day.put("12-14", 13);
        day.put("14-16", 15);
        day.put("16-18", 20);
        day.put("18-20", 25);
        day.put("20-22", 30);
        day.put("22-24", 35);
        ret.put("Mon", day);

        //Tuesday
        day = new LinkedHashMap<>();
        day.put("0-2", 2);
        day.put("2-4", 0);
        day.put("4-6", 1);
        day.put("6-8", 8);
        day.put("8-10", 8);
        day.put("10-12", 13);
        day.put("12-14", 15);
        day.put("14-16", 15);
        day.put("16-18", 20);
        day.put("18-20", 25);
        day.put("20-22", 40);
        day.put("22-24", 20);
        ret.put("Tue", day);

        //Wednesday
        day = new LinkedHashMap<>();
        day.put("0-2", 1);
        day.put("2-4", 0);
        day.put("4-6", 1);
        day.put("6-8", 8);
        day.put("8-10", 8);
        day.put("10-12", 12);
        day.put("12-14", 10);
        day.put("14-16", 10);
        day.put("16-18", 16);
        day.put("18-20", 25);
        day.put("20-22", 30);
        day.put("22-24", 20);
        ret.put("Wed", day);

        //Thursday
        day = new LinkedHashMap<>();
        day.put("0-2", 2);
        day.put("2-4", 1);
        day.put("4-6", 0);
        day.put("6-8", 5);
        day.put("8-10", 10);
        day.put("10-12", 11);
        day.put("12-14", 12);
        day.put("14-16", 14);
        day.put("16-18", 16);
        day.put("18-20", 21);
        day.put("20-22", 25);
        day.put("22-24", 12);
        ret.put("Thu", day);

        //Friday
        day = new LinkedHashMap<>();
        day.put("0-2", 4);
        day.put("2-4", 0);
        day.put("4-6", 1);
        day.put("6-8", 5);
        day.put("8-10", 16);
        day.put("10-12", 10);
        day.put("12-14", 12);
        day.put("14-16", 15);
        day.put("16-18", 16);
        day.put("18-20", 20);
        day.put("20-22", 26);
        day.put("22-24", 12);
        ret.put("Fri", day);

        //Saturday
        day = new LinkedHashMap<>();
        day.put("0-2", 15);
        day.put("2-4", 10);
        day.put("4-6", 7);
        day.put("6-8", 2);
        day.put("8-10", 20);
        day.put("10-12", 30);
        day.put("12-14", 30);
        day.put("14-16", 25);
        day.put("16-18", 35);
        day.put("18-20", 36);
        day.put("20-22", 40);
        day.put("22-24", 45);
        ret.put("Sat", day);

        //Sunday
        day = new LinkedHashMap<>();
        day.put("0-2", 20);
        day.put("2-4", 15);
        day.put("4-6", 8);
        day.put("6-8", 10);
        day.put("8-10", 25);
        day.put("10-12", 20);
        day.put("12-14", 25);
        day.put("14-16", 26);
        day.put("16-18", 30);
        day.put("18-20", 30);
        day.put("20-22", 25);
        day.put("22-24", 20);
        ret.put("Sun", day);

        return ret;
    }

}
