package com.example.easymechproject;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class Appointments extends AppCompatActivity {

    EditText  choose_date, address, car_plate, choose_time;
    AutoCompleteTextView autoCompleteTextView;
    Button book_now, reset;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    public String Car_Model, Choose_Date, Add_Address, Zip_Code , Plate_No, OIL_FILT;
    private final String CHANNEL_ID = "inbox_style_notification";
    private final int NOTIFICATION_ID = 04;
    RadioButton home_service, center_service;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        toolbar = (Toolbar) findViewById(R.id.tool_Bar);
        toolbar.setTitle("Set an Appointment");
        setSupportActionBar(toolbar);

        final String[] select_services = {
                "Choose Services", "Air Filter", "Oil Filter", "Engine Oil", "Wiper Fluid",
                "Cabin Filter / AC Filter", "Car Wash","Interior Vacuuming","Throttle Body Cleaning","Lost Keys"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_services.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_services[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        AppointmentSpinner_Adapter myAdapter = new  AppointmentSpinner_Adapter(Appointments.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_car_model);

        final ArrayList<String> myCarModels = new ArrayList<>();
        myCarModels.add("TOYOTA");
        myCarModels.add("Suzuki");
        myCarModels.add("Hyndai");
        myCarModels.add("Mercedes");
        myCarModels.add("TATA");
        myCarModels.add("Honda");
        myCarModels.add("Mistubishi");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, myCarModels);
        autoCompleteTextView.setAdapter(adapter);

        choose_date = (EditText)findViewById(R.id.choose_date);
        choose_time = (EditText)findViewById(R.id.choose_time);
        address = (EditText)findViewById(R.id.Address_Field);
        car_plate = (EditText)findViewById(R.id.Car_Plate_Field);
        home_service = (RadioButton)findViewById(R.id.HomeService);

        center_service = (RadioButton)findViewById(R.id.ServicesCenter);


        home_service.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(home_service.isChecked()){
                    center_service.setChecked(false);
                    address.setVisibility(View.VISIBLE);
                }
            }
        });

        center_service.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(center_service.isChecked()){
                    home_service.setChecked(false);
                    address.setVisibility(View.GONE);
                }
            }
        });


        book_now = (Button)findViewById(R.id.bookButton);

        //Date Picker Dialog
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Appointments.this,onDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                month = month+1;
                String date = day +"/"+month+"/"+year;
                choose_date.setText(date);
            }
        };

        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car_Model = autoCompleteTextView.getText().toString();
                Choose_Date = choose_date.getText().toString();
                Add_Address = address.getText().toString();
                Plate_No = car_plate.getText().toString();


                createNotificationChannel();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                builder.setSmallIcon(R.drawable.easy_mech);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.easy_mech));
                builder.setContentTitle("Appointment Set! More Details Below");
                builder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(Car_Model+" "+Choose_Date +" "+Add_Address+" "+Zip_Code+" "+Plate_No)
                        .setSummaryText("+2 More")
                );
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Appointments.this);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

            }
        });

    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "InboxStyleNotification Notification";
            String description = "Include all the InboxStyleNotification notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

