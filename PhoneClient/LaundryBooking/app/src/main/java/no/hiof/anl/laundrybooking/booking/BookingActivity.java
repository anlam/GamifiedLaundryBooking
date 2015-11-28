package no.hiof.anl.laundrybooking.booking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;

import java.util.Calendar;

import no.hiof.anl.laundrybooking.R;

/**
 * Created by An on 11/20/2015.
 */
public class BookingActivity extends AppCompatActivity
{
    static final int DIALOG_ID = 1500;
    int year_x, month_x, day_x = 0;
    int currentSelectedTime = -1;
    int hourStart = -1;
    int hourEnd = -1;
    TableLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar calendar = Calendar.getInstance();

        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        setupUIEvents();
    }

    private void setupUIEvents() {
        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);

            }
        });

    }

    private View GetSelectHourPopup(){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        return layoutInflater.inflate(R.layout.booking_selecthour_popup, null);
    }

    private void callDurationPopup(final int startHour){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.booking_selectduration_popup, null);



        //   ((EditText)popupView.findViewById(R.id.txtStart)).setText(startHour);



        final PopupWindow popupWindow = new PopupWindow(popupView,
                LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT,
                true);

        popupWindow .showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ((EditText)popupView.findViewById(R.id.txtStart)).setText(Integer.toString(startHour));
        // ((EditText) popupView.findViewById(R.id.txtStart)).setText(startHour);

        ((Button) popupView.findViewById(R.id.btnOkDuration))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        String hourEndString = ((EditText) popupView.findViewById(R.id.txtEnd)).getText().toString().trim();
                        TextView tvError = (TextView)popupView.findViewById(R.id.tvError);
                        hourStart = Integer.parseInt(((EditText)popupView.findViewById(R.id.txtStart)).getText().toString());


                        if(hourEndString.equals("")){

                            tvError.setText("Please specify the End hour");
                        }
                        else {
                            hourEnd = Integer.parseInt(hourEndString);

                            if(hourEnd < hourStart){
                                tvError.setText("The End hour cannot be less than the Start hour");
                            }
                            else if(hourEnd - hourStart > 1){
                                tvError.setText("You cannot book the machine for more than 2 hours");
                            }
                            else {
                                tvError.setText("");


                                for (int i = 0; i < ll.getChildCount(); i++) {
                                    if(i >= hourStart && i <= hourEnd){
                                        View child = ll.getChildAt(i);
                                        if (child instanceof TableRow) {
                                            TableRow row = (TableRow) child;
                                            row.setBackgroundColor(Color.rgb(244, 142, 32));
                                        }
                                    }


                                }
                                popupWindow.dismiss();
                            }
                        }

                    }

                });



    }

    private void callPopup(int hour) {


        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.booking_selecthour_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT,
                true);

        popupWindow .setTouchable(true);
        popupWindow .setFocusable(true);

        popupWindow .showAtLocation(popupView, Gravity.CENTER, 0, 0);

        TextView tvSelectedDate = (TextView) popupView.findViewById(R.id.tvSelectedDate);

        tvSelectedDate.setText(month_x + "/" + day_x + "/" + year_x);
        //    Name = (EditText) popupView.findViewById(R.id.edtimageName);

        ((Button) popupView.findViewById(R.id.saveBtn))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        Toast.makeText(getApplicationContext(),
                                "The booking is done", Toast.LENGTH_LONG).show();

                        popupWindow.dismiss();

                    }

                });

        ll = (TableLayout) popupView.findViewById(R.id.tableHours);

        final int[] points = CalculatePoints(year_x, month_x, day_x);

        for (int i = 0; i < 24; i++){
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            //row.setPadding(0, 30, 0, 30);
            row.setId(1000 + i);
            // row.setFocusable(true);

            //      row.setFocusableInTouchMode(true);
            row.setOnClickListener( new View.OnClickListener() {

                                        public void onClick(View v) {
                                            int mClickedPosition = v.getId() - 1000;

                                            for (int i = 0; i < ll.getChildCount(); i++) {
                                                if(points[i] !=0){

                                                    View child = ll.getChildAt(i);
                                                    if (child instanceof TableRow) {
                                                        TableRow row = (TableRow) child;

                                                        if(i >= hourStart && i <= hourEnd){

                                                            row.setBackgroundColor(Color.rgb(244, 142, 32));
                                                        }
                                                        else {

                                                            row.setBackgroundColor(Color.rgb(224, 241, 255));
                                                        }

                                                    }



                                                }

                                            }

                                            if(points[mClickedPosition] != 0){
                                                v.setBackgroundColor(Color.rgb(141, 231, 215));
                                                if(currentSelectedTime == mClickedPosition){
                                                    callDurationPopup(mClickedPosition);
                                                }


                                                currentSelectedTime = mClickedPosition;
                                            }



                                        }
                                    }
            );
            TextView tv = new TextView(this);


            tv.setText(i + ":00");
            tv.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 90, 1) );

            //   tv.setBackground(getResources().getDrawable(R.drawable.cell_shape));


            TextView tvPoint = new TextView(this);



            if(points[i] == 0) {
                row.setBackgroundColor(Color.rgb(244, 114, 114));

            }
            else {
                tvPoint.setText("+" + points[i]);
            }
            tvPoint.setLayoutParams( new TableRow.LayoutParams( android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 90, 5 ) );

            // ViewGroup.LayoutParams paramsPoint = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, pixels);
            //  tvPoint.setLayoutParams(paramsPoint);

            //  tvPoint.getLayoutParams().height = 30;
            tvPoint.setBackground(getResources().getDrawable(R.drawable.cell_point));


            row.addView(tv);
            row.addView(tvPoint);

            ll.addView(row, i);
        }
    }

    private int[] CalculatePoints(int year, int month, int date){
        int[] points = new int[24];
        points[0] = date/10;
        points[1] = date%10;
        points[2] = month/10;
        points[3] = month%10;

        points[4] = year/1000;
        points[5] = (year/100)%10;
        points[6] = (year/10)%100;
        points[7] = (year%1000)%10;

        points[8] = year/1000;
        points[9] = (year/100)%10;
        points[10] = (year/10)%100;
        points[11] = (year%1000)%10;

        points[12] = month/10;
        points[13] = month%10;
        points[14] = date/10;
        points[15] = date%10;

        points[16] = year/1000;
        points[17] = 0;
        points[18] = 0;
        points[19] = 0;

        points[20] = date/10 + 1;
        points[21] = date%10 + 1;
        points[22] = month/10 + 1;
        points[23] = month%10 + 1;

        return points;
    }

    private void handleBtnDateClicked(Button button) {
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID){
            DatePickerDialog dp = new DatePickerDialog(this, dPickerListener, year_x, month_x, day_x);

            dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dp;
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;

            //  EditText txtDate = (EditText) findViewById(R.id.txtBookingDate);

            //   txtDate.setText(month_x + "/" + day_x + "/" + year_x);

            callPopup(1);
        }
    };
}
