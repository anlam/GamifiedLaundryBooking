package no.hiof.anl.laundrybooking.booking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import no.hiof.anl.laundrybooking.Database.BookingInfo;
import no.hiof.anl.laundrybooking.R;
import no.hiof.anl.laundrybooking.account.Info;

/**
 * Created by An on 11/20/2015.
 */
public class BookingActivity extends AppCompatActivity
{
    static final int DIALOG_ID = 1500;
    static final int ROW_ID = 1000;

    int numberOfHourSelected = 0;
    int year_x, month_x, day_x = 0;
    int currentSelectedTime = -1;
    int hourStart = -1;
    int hourEnd = -1;
    TableLayout ll;
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    private static ArrayList<BookingInfo> bookingInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        final Calendar calendar = Calendar.getInstance();

        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        setupUIEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            //    Intent intent = NavUtils.getParentActivityIntent(this);
              //  NavUtils.navigateUpTo(this, intent);
          //      String caller     = getIntent().getStringExtra("caller");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void AddBookingInfoToDB(BookingInfo bookingInfo) {
        String json = settings.getString("bookingInfos", null);
        //Log.i("Messages", json);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<BookingInfo>>(){}.getType();
        bookingInfos = gson.fromJson(json, collectionType);
        if(bookingInfos == null){
            bookingInfos = new ArrayList<BookingInfo>();
        }

        bookingInfos.add(bookingInfo);

        String jsonBookingInfo = gson.toJson(bookingInfos);
        editor.putString("bookingInfos", jsonBookingInfo);
        editor.commit();
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
                        if(numberOfHourSelected > 0){
                            if(numberOfHourSelected == 2){
                                hourStart--;
                            }

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year_x);
                            cal.set(Calendar.MONTH, month_x - 1);
                            cal.set(Calendar.DAY_OF_MONTH, day_x);
                            cal.set(Calendar.HOUR_OF_DAY, hourStart);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);



                            AddBookingInfoToDB(new BookingInfo(cal, numberOfHourSelected, 0, 1));
                            Toast.makeText(getApplicationContext(),
                                    "The booking is done. You can view it in My Booking", Toast.LENGTH_LONG).show();
                            numberOfHourSelected = 0;
                        }

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
            row.setId(ROW_ID + i);
            // row.setFocusable(true);

            //      row.setFocusableInTouchMode(true);

            TextView tv = new TextView(this);


            tv.setText(i + "h - " + (i + 1) + "h");
            tv.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 90, 1) );

            //   tv.setBackground(getResources().getDrawable(R.drawable.cell_shape));


            TextView tvPoint = new TextView(this);



            if(points[i] == 0) {
                row.setBackgroundColor(Color.rgb(244, 114, 114));

            }
            else {
                tvPoint.setText("+" + points[i] + " points");
                tvPoint.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            }
            tvPoint.setLayoutParams( new TableRow.LayoutParams( android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 90, 5 ) );

            // ViewGroup.LayoutParams paramsPoint = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, pixels);
            //  tvPoint.setLayoutParams(paramsPoint);

            //  tvPoint.getLayoutParams().height = 30;
            tvPoint.setBackground(getResources().getDrawable(R.drawable.cell_point));

            // add checkbox

            row.addView(tv);
            row.addView(tvPoint);

            if(points[i] != 0){
                CheckBox cb = new CheckBox(getApplicationContext());
                cb.setId(i);

                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CheckBox currentCheckbox = (CheckBox) buttonView;

                        currentSelectedTime = currentCheckbox.getId();
                        for (int i = 0; i < ll.getChildCount(); i++) {
                            if (points[i] != 0) {

                                View child = ll.getChildAt(i);
                                if (child instanceof TableRow) {
                                    TableRow row = (TableRow) child;
                                    CheckBox cbox = (CheckBox) ((TableRow) child).getChildAt(2);

                                    if (isChecked) {

                                        if (child.getId() == ROW_ID + currentCheckbox.getId()) {
                                            SetColorForSelectedRow(row);
                                            if (currentCheckbox.getId() > hourStart) {
                                                hourStart = currentCheckbox.getId();
                                            }
                                        } else if (child.getId() == ROW_ID + currentCheckbox.getId() + 1) {
                                            if (numberOfHourSelected < 1) {
                                                cbox.setEnabled(true);
                                            }


                                        } else {
                                            if (numberOfHourSelected < 1) {
                                                cbox.setEnabled(false);
                                            }


                                        }
                                    } else {
                                        if (i == currentCheckbox.getId()) {
                                            SetColorForUnSelectedRow(row);

                                            if (numberOfHourSelected == 2) {

                                                View previousChild = ll.getChildAt(i - 1);
                                                CheckBox previousCheckbox = (CheckBox) ((TableRow) previousChild).getChildAt(2);
                                                if (previousCheckbox == null || !previousCheckbox.isChecked()) {
                                                    View nextChild = ll.getChildAt(i + 2);

                                                    if (nextChild != null) {
                                                        if (((TableRow) nextChild).getChildCount() == 3) {

                                                            CheckBox nextCheckbox = (CheckBox) ((TableRow) nextChild).getChildAt(2);
                                                            nextCheckbox.setEnabled(true);
                                                        }
                                                        cbox.setEnabled(false);
                                                        i += 2;
                                                    }

                                                }
                                            }
                                        } else {
                                            if (numberOfHourSelected == 1) {
                                                cbox.setEnabled(true);
                                            }


                                        }
                                    }
                                }
                            }

                        }

                        if (isChecked) {
                            numberOfHourSelected++;
                        } else {
                            numberOfHourSelected--;
                        }
                    }
                });
                row.addView(cb);
            }

            ll.addView(row, i);
        }
    }

    private void SetColorForSelectedRow( TableRow row){
        // Highlight the row
        row.setBackgroundColor(Color.rgb(244, 142, 32));
    }

    private void SetColorForUnSelectedRow( TableRow row){
        // UnHighlight the row
        row.setBackgroundColor(Color.rgb(224, 241, 255));
    }

    private int[] CalculatePoints(int year, int month, int date){
        int[] points = new int[24];
        points[0] = date/10 + 6;
        points[1] = date%10 + 6;
        points[2] = month/10 + 7;
        points[3] = month%10 + 6;

        points[4] = year/1000 + 6;
        points[5] = (year/100)%10 + 4;
        points[6] = (year/10)%100 + 4;;
        points[7] = (year%1000)%10 + 4;

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
