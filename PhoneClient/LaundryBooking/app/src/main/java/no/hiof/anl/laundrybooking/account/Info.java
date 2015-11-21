package no.hiof.anl.laundrybooking.account;

/**
 * Created by An on 11/20/2015.
 */
public class Info
{
    public String message;
    public boolean isGoodNews;

    public Info(String msg, boolean isGood)
    {
        this.message = msg;
        this.isGoodNews = isGood;
    }

}
