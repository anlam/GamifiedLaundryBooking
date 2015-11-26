package no.hiof.anl.laundrybooking.Database;

/**
 * Created by An on 11/18/2015.
 */
public class UserInfo
{
    public String name;
    public int id;
    public int pin;
    public int points;
    public int balance;

    public int level;
    public int progress_points;
    public String avatar;

    public static int scale = 30;

    public transient int progress_percent;

    public UserInfo(String name, int id, int pin, int points, int balance)
    {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.points =  points;
        this.balance = balance;

        this.level = 1;
        this.progress_percent = 0;
        this.progress_percent = 0;
    }

    public UserInfo(String name, int id, int pin, int points, int balance, String avatar)
    {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.points =  points;
        this.balance = balance;
        this.avatar = avatar;
        this.level = 1;

        this.progress_percent = 0;
        this.progress_percent = 0;
    }

    public UserInfo(String name, int id, int pin, int points, int balance, String avatar, int level, int progress_points)
    {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.points =  points;
        this.balance = balance;
        this.avatar = avatar;

        this.level = level;

        this.progress_points = progress_points;

        this.progress_percent = progress_points * 100 /(scale * level);

    }
}
