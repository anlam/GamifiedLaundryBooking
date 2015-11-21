package no.hiof.anl.laundrybooking.Database;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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
        UserInfo user = new UserInfo("Thomas", 107761285, 11111, 450, 7000, "https://s-media-cache-ak0.pinimg.com/736x/84/00/5c/84005cfa9fd840b18dbf04f600f0aee0.jpg");
        addUser(user);

        user = new UserInfo("Alby", 22222, 22222, 350, 5000, "http://images6.fanpop.com/image/photos/37600000/Alby-the-maze-runner-37638950-1200-1000.png");
        addUser(user);

        user = new UserInfo("Chuck", 33333, 33333, 150, 1000, "http://img2.wikia.nocookie.net/__cb20141018073627/mazerunner/images/8/8d/Chuck-the-maze-runner-37624809-1200-1000.png");
        addUser(user);

        user = new UserInfo("Minho", 44444, 44444, 850, 4000, "http://vignette2.wikia.nocookie.net/mazerunner/images/c/c9/TMRposter-minho.jpg/revision/latest/top-crop/width/240/height/240?cb=20140916191235");
        addUser(user);

        user = new UserInfo("Teresa", 55555, 55555, 850, 5000, "http://www.ehiyo.com/wp-content/uploads/2014/11/teresa-the-maze-runner-10866.jpg");
        addUser(user);

        user = new UserInfo("Newt", 66666, 66666, 550, 2000, "http://vignette3.wikia.nocookie.net/mazerunner/images/7/78/Glader_Newt.png/revision/latest?cb=20140731183732");
        addUser(user);

        user = new UserInfo("An Lam", 77777, 77777, 550, 2000, null);
        addUser(user);

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

}
