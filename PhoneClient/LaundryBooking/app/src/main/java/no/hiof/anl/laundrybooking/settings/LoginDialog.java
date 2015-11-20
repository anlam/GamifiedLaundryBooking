package no.hiof.anl.laundrybooking.settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import no.hiof.anl.laundrybooking.Database.Database;
import no.hiof.anl.laundrybooking.Database.UserInfo;
import no.hiof.anl.laundrybooking.R;


/**
 * Created by An on 10/4/2015.
 */
public class LoginDialog extends DialogFragment
{
    NoticeDialogListener mListener;
    //private String fullname = "";
   // private String email = "";
    private EditText pinEditext;
    private TextView infoTextbox;
    private int pinID;
    private UserInfo userInfo;
    //private EditText emailEditor;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_login, null);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
           /*     .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                mListener.onDialogNegativeClick(LoginDiaglog.this);
            }
        });*/

        pinEditext = (EditText) view.findViewById(R.id.pin_editext);
        infoTextbox = (TextView) view.findViewById(R.id.info_textbox);

        return builder.create();
    }

   /* @Override
    public void onCancel(DialogInterface dialog)
    {
        mListener.onDialogNegativeClick(LoginDiaglog.this);
        super.onCancel(dialog);
    }*/

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    String tString = pinEditext.getText().toString();
                    if(tString != null && !tString.isEmpty())
                    {
                        pinID = Integer.parseInt(tString);
                        userInfo = Database.getUserByPin(pinID);
                        if(userInfo != null)
                            mListener.onDialogPositiveClick(LoginDialog.this);
                        else {
                            infoTextbox.setTextColor(Color.RED);
                            infoTextbox.setText("Invalid Pin ID");
                        }

                    }
                }
            });
        }
    }

    // Use this instance of the interface to deliver action events


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public int getPinID()
    {
        return pinID;
    }

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

}
