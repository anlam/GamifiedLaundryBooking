package no.hiof.anl.laundrybooking.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class NotificationActivity extends AppCompatActivity
{

    EditText email_edittext;
    EditText sms_edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email_edittext = (EditText) findViewById(R.id.email_edittext);
        sms_edittext = (EditText) findViewById(R.id.sms_edittext);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.email_checkbox:
                if (checked)
                    email_edittext.setEnabled(true);
                else
                    email_edittext.setEnabled(false);
                break;
            case R.id.sms_checkbox:
                if (checked)
                    sms_edittext.setEnabled(true);
                else
                    sms_edittext.setEnabled(false);
                break;
        }
    }
}
