package no.hiof.anl.machineclient;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class MainActivity extends Activity
{

    int machine_id = 1;

    public enum ACTIVITY_STATE {LOGIN , BOOKING, READY, USING}
    private ACTIVITY_STATE activity_state = ACTIVITY_STATE.LOGIN;

    public enum MACHINE_STATE {AVAILABLE, RUNNING, NOT_AVALABLE}
    private MACHINE_STATE machine_state = MACHINE_STATE.AVAILABLE;

    View mDecorView;
    TextView name_textbox;
    TextView status_textBox;
    TextView time_textbox;
    TextView instruction_textbox;
    TextView info_textbox;
    TextView hello_textbox;
    TextView detail_instruction_textbox;
    TextView amount_textbox;
    TextView unit_textbox;
    //TextView duration_textbox;
    Spinner duration_spiner;
    RadioGroup payment_radiogroup;

    TextView hello_ready_textbox;
    TextView detail_ready_instruction_textbox;
    //TextView paymentmethod_textbox;

    LinearLayout booking_layout;
    LinearLayout  login_layout;
    LinearLayout ready_layout;
    LinearLayout using_layout;


    TextView rfid_textBox;

    //Button back_button;
    Button start_button;
    Button cancel_button;

    EditText pin_edittext;


    private  static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static InputStreamReader inputStreamReader;
    private  static BufferedReader bufferedReader;
    //private static PrintWriter printWriter;
    private ReceiveMessage receiveMessageTask;

    private  String message = "";
    private static Handler mHandler = new Handler();
    private int serverPort = 4444;

    private UserInfo currentUser;
    private BookingInfo current_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDecorView = getWindow().getDecorView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        rfid_textBox = (TextView) findViewById(R.id.rfid_textbox);
        name_textbox = (TextView) findViewById(R.id.name_textbox);
        status_textBox = (TextView) findViewById(R.id.status_textbox);
        time_textbox = (TextView) findViewById(R.id.time_textbox);
        instruction_textbox = (TextView) findViewById(R.id.instruction_textbox);
        info_textbox = (TextView) findViewById(R.id.info_textbox);
        amount_textbox = (TextView) findViewById(R.id.amount_textbox);
        unit_textbox = (TextView) findViewById(R.id.unit_textbox);

        hello_textbox  = (TextView) findViewById(R.id.hello_textbox);
        detail_instruction_textbox = (TextView) findViewById(R.id.detail_instruction_textbox);
       // duration_textbox = (TextView) findViewById(R.id.duration_textbox);
        duration_spiner = (Spinner) findViewById(R.id.duration_spinner);
        payment_radiogroup = (RadioGroup)findViewById(R.id.payment_radiogroup);
        //paymentmethod_textbox = (TextView) findViewById(R.id.paymentmethod_textbox);

        hello_ready_textbox  = (TextView) findViewById(R.id.hello_ready_textbox);
        detail_ready_instruction_textbox = (TextView) findViewById(R.id.detail_ready_instruction_textbox);

        booking_layout = (LinearLayout) findViewById(R.id.booking_layout);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        ready_layout = (LinearLayout) findViewById(R.id.ready_layout);
        using_layout = (LinearLayout) findViewById(R.id.using_layout);

        start_button = (Button) findViewById(R.id.start_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);

        name_textbox.setText("Machine " + machine_id);


        pin_edittext = (EditText)findViewById(R.id.pin_editext);
        pin_edittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getWindow().setLocalFocus(true, true);

                    String code = pin_edittext.getText().toString();
                    if (code != null && !code.isEmpty())
                        authentication(0, Integer.parseInt(code));
                }

                return false;
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_state != ACTIVITY_STATE.LOGIN)
                    backToLoginState();
            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(activity_state == ACTIVITY_STATE.BOOKING)
                {
                    mHandler.removeCallbacks(mBackToLoginState);
                    toReadyState();
                }
                else if(activity_state == ACTIVITY_STATE.READY)
                {
                    mHandler.removeCallbacks(mBackToLoginState);
                    toUsingState();
                }
                else if(activity_state == ACTIVITY_STATE.USING)
                {
                    machine_state = MACHINE_STATE.AVAILABLE;
                    backToLoginState();
                }
            }
        });

        duration_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payment_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateAmount();
            }
        });

        Database.getAllUsers();
        mHandler.post(mUpdateTimeRunnable);
        startServer();
    }

    private void updateAmount()
    {
        double duration = Double.valueOf(duration_spiner.getSelectedItem().toString());
        int amount = 0;
        String unit = "NOK.";
        if(payment_radiogroup.getCheckedRadioButtonId() == R.id.cash_button)
        {
            amount = (int)(Database.booking_cash_per_hour*duration);
        }
        else
        {
            amount = (int)(Database.booking_points_per_hour*duration);
            unit = "Points.";
        }
        amount_textbox.setText(String.valueOf(amount));
        unit_textbox.setText(unit);
    }

    private void backToLoginState()
    {
        currentUser = null;
        activity_state = ACTIVITY_STATE.LOGIN;

        info_textbox.setText("");
        pin_edittext.setText("");

        login_layout.setVisibility(View.VISIBLE);

        start_button.setVisibility(View.INVISIBLE);
        cancel_button.setVisibility(View.INVISIBLE);
        ready_layout.setVisibility(View.INVISIBLE);
        using_layout.setVisibility(View.INVISIBLE);

        booking_layout.setVisibility(View.INVISIBLE);

        mHandler.removeCallbacks(mBackToLoginState);

        mHandler.postDelayed(mWaitingFromRFID, 100);

        updateTime();
    }

    private void toBookingState()
    {
        activity_state = ACTIVITY_STATE.BOOKING;

        login_layout.setVisibility(View.INVISIBLE);


        hello_textbox.setText("Hello " + currentUser.name);
        detail_instruction_textbox.setText("Please enter how long you want to use this machine and payment method.");

        start_button.setText("OK");
        start_button.setVisibility(View.VISIBLE);
        start_button.setEnabled(true);
        cancel_button.setVisibility(View.VISIBLE);
        cancel_button.setEnabled(true);

        booking_layout.setVisibility(View.VISIBLE);

        ArrayList<String> durations = new ArrayList<>();
        double min = Database.min_hours;
        double max = Database.max_hours;

        if(current_booking != null)
        {
            GregorianCalendar current_date = new GregorianCalendar();
            long time_ahead = current_booking.getTimeAhead(current_date.getTimeInMillis());
            double hrs =  time_ahead/(1000.0*60.0*60.0);
            if(hrs < max)
            {
                max = hrs;
            }

        }
        for(double i = min; i <= max; i += .25)
        {
            durations.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration_spiner.setAdapter(adapter);

        mHandler.postDelayed(mBackToLoginState, 2*60*1000);
    }



    private void toReadyState()
    {
       if(activity_state == ACTIVITY_STATE.BOOKING)
       {
           double duration = Double.valueOf(duration_spiner.getSelectedItem().toString());
           current_booking = new BookingInfo(duration,  currentUser.pin, machine_id);
           if(payment_radiogroup.getCheckedRadioButtonId() == R.id.cash_button)
               current_booking.isBuyByCash = true;
           Database.updateAwardsPoints(current_booking);
           Database.addBookingInfo(machine_id, current_booking);
       }

        activity_state = ACTIVITY_STATE.READY;

        login_layout.setVisibility(View.INVISIBLE);
        booking_layout.setVisibility(View.INVISIBLE);
        ready_layout.setVisibility(View.VISIBLE);

        start_button.setText("START");
        start_button.setVisibility(View.VISIBLE);
        cancel_button.setVisibility(View.VISIBLE);
        start_button.setEnabled(true);
        cancel_button.setEnabled(true);

        hello_ready_textbox.setText("Hello " + currentUser.name);

        GregorianCalendar current_date = new GregorianCalendar();

        if(current_booking.userPin == currentUser.pin) {

            String text = "You have reserved this machine for " + current_booking.duration + " hour(s).\n";
            text += "Please try to finish before " + current_booking.getEndTimeFormat(current_date.getTimeInMillis()) + ".\n";
            text += "Press Start to use the machine.";
            detail_ready_instruction_textbox.setText(text);
        }
        else
        {
            //long time = current_booking.getTimeAhead(current_date.getTimeInMillis());

            String text = "This machine has been reserved and will be used in a moment.\n";
            text += "You cannot start it now.";
            detail_ready_instruction_textbox.setText(text);

            start_button.setEnabled(false);
        }

        mHandler.postDelayed(mBackToLoginState, 60*1000);
        //toUsingState();
    }

    private void toUsingState()
    {

        status_textBox.setTextColor(Color.RED);
        status_textBox.setText("In Used");

        activity_state = ACTIVITY_STATE.USING;
        machine_state = MACHINE_STATE.RUNNING;

        login_layout.setVisibility(View.INVISIBLE);
        //booking_layout.setVisibility(View.INVISIBLE);
        ready_layout.setVisibility(View.INVISIBLE);
        using_layout.setVisibility(View.VISIBLE);

        start_button.setVisibility(View.VISIBLE);
        cancel_button.setVisibility(View.VISIBLE);

        GregorianCalendar current_date = new GregorianCalendar();
        long ahead = current_booking.getTimeAhead(current_date.getTimeInMillis());
        if(ahead > 0)
        {
            current_booking.updateTime(current_date.getTimeInMillis());
            Database.updateBookingInfo(machine_id, current_booking);
        }

        start_button.setText("FINISH");
        cancel_button.setEnabled(false);

        long remaining = current_booking.getRemainingTime(current_date.getTimeInMillis());
        long mins = remaining/60000;
        long hours = mins/60;
        mins = mins - hours*60;

        time_textbox.setTextColor(Color.RED);
        if(hours > 0)
        {
            time_textbox.setText("In " + hours + " hrs "+ mins + " mins");
        }
        else
        {
            time_textbox.setText("In " + mins + " mins");
        }

        //mHandler.postDelayed(mBackToLoginState, 60*1000);
    }





    private void authentication(int method, int code)
    {

        //receiveMessageTask.cancel(true);

        //pin code
        if(activity_state == ACTIVITY_STATE.LOGIN) {
            if (method == 0) {
                currentUser = Database.getUserByPin(code);
            }
            //RFID
            else if (method == 1) {
                currentUser = Database.getUserByRFID(code);
            }

            if (currentUser == null)
            {
                info_textbox.setTextColor(Color.RED);
                info_textbox.setText("Invalid Code. Please try again.");
                //pin_edittext.setText("");
                mHandler.postDelayed(mWaitingFromRFID, 100);
            }
            else
            {
                info_textbox.setTextColor(Color.BLUE);
                info_textbox.setText("Authentication Successful");


                BookingInfo booking = Database.getNextBooking(machine_id, BookingInfo.getToDate());
                if(booking == null)
                    booking = Database.getNextBooking(machine_id, BookingInfo.getTommorrow());
                //current_booking
                current_booking = booking;
                //if(current_booking)
                if(booking != null) {

                    GregorianCalendar current_date = new GregorianCalendar();

                    long time_ahead = booking.getTimeAhead(current_date.getTimeInMillis());
                    if (booking.userPin == currentUser.pin || time_ahead < Database.min_hours * 60 * 60 * 1000)
                    {

                        mHandler.postDelayed(mGetToReadyState, 1000);
                    }
                    else
                        mHandler.postDelayed(mGetToBookingState, 1000);
                }
                else
                {
                    mHandler.postDelayed(mGetToBookingState, 1000);
                }


            }
        }
    }

    private void startServer()
    {
        InitialzeServer initialzeServer = new InitialzeServer();
        initialzeServer.execute();

    }

    private class InitialzeServer extends AsyncTask<Void, Void, Integer>
    {
        @Override
        protected Integer doInBackground(Void... params)
        {
            try
            {
                if(serverSocket == null || serverSocket.isClosed())
                {
                    serverSocket = new ServerSocket(serverPort); // Server socket
                    Log.i("InitialzeServer", "Server Started");
                    mHandler.postDelayed(mWaitingFromRFID, 100);
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer ret)
        {
            if(ret == 0)
            {
                rfid_textBox.setTextColor(Color.RED);
                rfid_textBox.setText("Cannot start connection with RFID reader. Please use pin code.");
            }
        }

    }

    private class ReceiveMessage extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params)
        {
            Log.i("ReceiveMessage", "doInBackground");
            try
            {
                message = "";
                Log.i("mWaitingFromRFID", "Waiting for new client");
                serverSocket.setSoTimeout(30000);
                clientSocket = serverSocket.accept();
                //clientSocket.setKeepAlive(true);
                clientSocket.setSoTimeout(3000);
                Log.i("mWaitingFromRFID", "New Client Connected");
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);

                Log.i("mWaitingFromClient", "Waiting for message");
                message = bufferedReader.readLine();
                Log.i("mWaitingFromClient", "Receive Message: " + message);
            }
            catch (SocketTimeoutException e)
            {
                try
                {
                    if(clientSocket != null) {
                        bufferedReader.close();
                        inputStreamReader.close();
                        clientSocket.close();
                        clientSocket = null;
                    }

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }

                Log.i("SocketTimeoutException", "");
                //e.printStackTrace();
                return 0;
            }
            catch (IOException e)
            {
                try
                {
                    bufferedReader.close();
                    inputStreamReader.close();
                    clientSocket.close();
                    clientSocket = null;

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }

                e.printStackTrace();
                return -1;
            }

            try
            {
                bufferedReader.close();
                inputStreamReader.close();
                clientSocket.close();
                clientSocket = null;

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if(message != null && !message.isEmpty())
            {
                try
                {
                    return Integer.parseInt(message);
                }
                catch (Exception e)
                {
                    Log.i("Invalid Integer Except", "");
                    return 1111111;
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            Log.i("Result", result.toString());
            if(result > 0)
            {
                pin_edittext.setText(message);
                authentication(1, result);
            }
            else if(activity_state == ACTIVITY_STATE.LOGIN)
            {
                mHandler.postDelayed(mWaitingFromRFID, 100);
            }
            //else if(result < 0)
            //{
                //error occur
                //rfid_textBox.setTextColor(Color.RED);
                //rfid_textBox.setText("Error occured when reading RFID. Please try again or use pin code.");
            //}
        }

    }

    private Runnable mWaitingFromRFID = new Runnable() {
        @Override
        public void run()
        {
            receiveMessageTask = new ReceiveMessage();
            receiveMessageTask.execute();
        }
    };

    Runnable mUpdateTimeRunnable = new Runnable() {
        @Override
        public void run()
        {
            updateTime();
            mHandler.postDelayed(mUpdateTimeRunnable, 60000);
        }
    };

    private void updateTime()
    {
        if(activity_state != ACTIVITY_STATE.USING)
        {
            BookingInfo book = Database.getNextBooking(machine_id, BookingInfo.getToDate());
            if(book == null)
                book = Database.getNextBooking(machine_id, BookingInfo.getTommorrow());

            if (book != null) {

                GregorianCalendar current_date = new GregorianCalendar();
                long ehead = book.getTimeAhead(current_date.getTimeInMillis());
                long mins = ehead/60000;
                //Log.e("mUpdateTimeRunnable", String.valueOf(ehead));
                //Log.e("mUpdateTimeRunnable", String.valueOf(mins));

                if (ehead > Database.min_hours*60*60*1000)
                {


                    if(mins < Database.max_hours*60)
                    {
                        long hours = mins/60;
                        mins = mins - hours*60;
                        if(hours > 0)
                        {
                            time_textbox.setText("In " + hours + " hrs "+ mins + " mins");
                        }
                        else {
                            time_textbox.setText("In " + mins + " mins");
                        }
                    }
                    else
                        time_textbox.setText("In more than " + Database.max_hours + " hours");

                    time_textbox.setTextColor(Color.GREEN);
                    status_textBox.setTextColor(Color.GREEN);
                    status_textBox.setText("AVAILABLE");
                    machine_state = MACHINE_STATE.AVAILABLE;
                }
                else if(ehead > 0)
                {
                    time_textbox.setText("Will be used in " + mins + " mins");
                    time_textbox.setTextColor(Color.BLUE);
                    status_textBox.setTextColor(Color.BLUE);
                    status_textBox.setText("USED IN A MOMENT");
                    machine_state = MACHINE_STATE.NOT_AVALABLE;
                }
                else
                {
                    time_textbox.setTextColor(Color.YELLOW);
                    current_date = new GregorianCalendar();
                    long remaining = book.getRemainingTime(current_date.getTimeInMillis());
                    mins = remaining/60000;
                    long hours = mins/60;
                    mins = mins - hours*60;

                    time_textbox.setTextColor(Color.YELLOW);
                    if(hours > 0)
                    {
                        time_textbox.setText("In " + hours + " hrs "+ mins + " mins");
                    }
                    else
                    {
                        time_textbox.setText("In " + mins + " mins");
                    }

                    status_textBox.setTextColor(Color.YELLOW);
                    status_textBox.setText("RESERVED");
                    machine_state = MACHINE_STATE.NOT_AVALABLE;
                }
            }
            else
            {
                time_textbox.setTextColor(Color.GREEN);
                time_textbox.setText("No Booking In Advance");
                status_textBox.setTextColor(Color.GREEN);
                status_textBox.setText("AVAILABLE");
                machine_state = MACHINE_STATE.AVAILABLE;
            }
        }
        else
        {
            GregorianCalendar current_date = new GregorianCalendar();
            long remaining = current_booking.getRemainingTime(current_date.getTimeInMillis());

            if(remaining > 0) {
                long mins = remaining / 60000;
                long hours = mins / 60;
                mins = mins - hours * 60;

                time_textbox.setTextColor(Color.RED);
                if (hours > 0) {
                    time_textbox.setText("In " + hours + " hrs " + mins + " mins");
                } else {
                    time_textbox.setText("In " + mins + " mins");
                }

                machine_state = MACHINE_STATE.RUNNING;
            }
            else
            {
                time_textbox.setText("");
                status_textBox.setTextColor(Color.GREEN);
                status_textBox.setText("FINISH");
                current_booking.isDone = true;
                Database.updateBookingInfo(machine_id, current_booking);

                machine_state = MACHINE_STATE.AVAILABLE;
                mHandler.postDelayed(mBackToLoginState, 60000);
            }
        }

        Database.updateMachineState(machine_id, machine_state);
    }

    private Runnable mBackToLoginState = new Runnable() {
        @Override
        public void run() {
            backToLoginState();
        }
    };

    private Runnable mGetToBookingState = new Runnable() {
        @Override
        public void run() {
            toBookingState();
        }
    };

    private Runnable mGetToReadyState = new Runnable() {
        @Override
        public void run() {
            toReadyState();
        }
    };




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
