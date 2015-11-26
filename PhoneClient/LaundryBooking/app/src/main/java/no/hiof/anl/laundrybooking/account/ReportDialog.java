package no.hiof.anl.laundrybooking.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/26/2015.
 */
public class ReportDialog extends DialogFragment
{

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(ReportDialog dialog);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;
    EditText machine_id_edittext_report_dialog;
    EditText booking_id_edittext_report_dialog;
    TextView status_textbox_report_idalog;

    public int machine_id;
    public int booking_id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.report_dialog, null);


        machine_id_edittext_report_dialog = (EditText) view.findViewById(R.id.machine_id_edittext_report_dialog);
        booking_id_edittext_report_dialog = (EditText) view.findViewById(R.id.machine_id_edittext_report_dialog);
        status_textbox_report_idalog = (TextView) view.findViewById(R.id.status_textbox_report_idalog);
        status_textbox_report_idalog.setText("");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       ReportDialog.this.dismiss();
                   }
               });



        return builder.create();
    }

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

                    String machine_id_text = machine_id_edittext_report_dialog.getText().toString();
                    String booking_id_text = booking_id_edittext_report_dialog.getText().toString();

                    if (machine_id_text.isEmpty())
                        status_textbox_report_idalog.setText("Please enter machine id");
                    else if (booking_id_text.isEmpty())
                        status_textbox_report_idalog.setText("Please enter empty id");
                    else {
                        machine_id = Integer.parseInt(machine_id_text);
                        booking_id = Integer.parseInt(booking_id_text);
                        mListener.onDialogPositiveClick(ReportDialog.this);
                    }
                }
            });
        }
    }

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



}
