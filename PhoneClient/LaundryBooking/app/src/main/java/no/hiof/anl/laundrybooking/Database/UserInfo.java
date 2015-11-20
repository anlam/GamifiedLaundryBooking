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
    public int progress;
    public String avatar;

    public UserInfo(String name, int id, int pin, int points, int balance)
    {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.points =  points;
        this.balance = balance;
    }

    public UserInfo(String name, int id, int pin, int points, int balance, String avatar)
    {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.points =  points;
        this.balance = balance;
        this.avatar = avatar;
    }
}
