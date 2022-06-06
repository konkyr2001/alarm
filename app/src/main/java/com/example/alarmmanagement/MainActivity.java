package com.example.alarmmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmmanagement.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> alarms;
    private int counterAlarms;

    private ActivityMainBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ImageView bin;
    private RelativeLayout relative1;
    private RelativeLayout relative2;
    private RelativeLayout relative3;
    private RelativeLayout relative4;
    private RelativeLayout relative5;

    private SwitchCompat switchCompat;
    private SharedPreferences sharedPreferences = null;

    private Toast toastMessage;

    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();

        /*
        // DARK MODE
        switchCompat = findViewById(R.id.switchCompat);
        sharedPreferences = getSharedPreferences("night", 0);
        boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", true);
                    editor.commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", false);
                    editor.commit();
                }
            }
        });
*/
        dbHandler = new DBHandler(getApplicationContext());
        alarms = new ArrayList();
        counterAlarms = 0;
        relative1 = (RelativeLayout) findViewById(R.id.relative1);
        initializeRelativeLayouts(1); // settings all childs of layout 1
        relative2 = (RelativeLayout) findViewById(R.id.relative2);
        initializeRelativeLayouts(2); // settings all childs of layout 2
        relative3 = (RelativeLayout) findViewById(R.id.relative3);
        initializeRelativeLayouts(3); // settings all childs of layout 3
        relative4 = (RelativeLayout) findViewById(R.id.relative4);
        initializeRelativeLayouts(4); // settings all childs of layout 4
        relative5 = (RelativeLayout) findViewById(R.id.relative5);
        initializeRelativeLayouts(5); // settings all childs of layout 5

        binding.selectTimeBtn.setOnClickListener(new View.OnClickListener() { // select time
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


/*
        // Set timer correctly
        Calendar currentTime = Calendar.getInstance();
        String currentTimer;
        String minutes;
        if (currentTime.get(Calendar.MINUTE) < 10) {
            minutes = "0" + currentTime.get(Calendar.MINUTE);
        } else {
            minutes = "" + currentTime.get(Calendar.MINUTE);
        }
        if (currentTime.get(Calendar.HOUR_OF_DAY) <= 12) {
            if (currentTime.get(Calendar.HOUR_OF_DAY) == 12)
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) + " : " + minutes + " PM";
            else
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) + " : " + minutes + " AM";
        } else {
            if (currentTime.get(Calendar.HOUR_OF_DAY) == 24)
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) - 12+ " : " + minutes + " AM";
            else
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) - 12+ " : " + minutes + " PM";
        }
        binding.selectedTime1.setText(currentTimer);
*/


/*
        binding.setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlarm();

            }
        });

        binding.cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAlarm();

            }
        });
*/
    }

    private void initializeRelativeLayouts(int indexLayout) { // setting up every object before getting clicked
        if (indexLayout == 1) {
            TextView alarmTime = (TextView) relative1.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative1.getChildAt(1);
            switchAlarm.setChecked(true);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(1);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm(1);
                    }
                }
            });

            ImageView bin = (ImageView) relative1.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative1.setVisibility(View.GONE);
                    cancelAlarm(1);
                }
            });
        } else if (indexLayout == 2) {
            TextView alarmTime = (TextView) relative2.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative2.getChildAt(1);
            switchAlarm.setChecked(true);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(2);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm(2);
                    }
                }
            });

            ImageView bin = (ImageView) relative2.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative2.setVisibility(View.GONE);
                    cancelAlarm(2);
                }
            });
        } else if (indexLayout == 3) {
            TextView alarmTime = (TextView) relative3.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative3.getChildAt(1);
            switchAlarm.setChecked(true);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(3);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm(3);
                    }
                }
            });

            ImageView bin = (ImageView) relative3.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative3.setVisibility(View.GONE);
                    switchAlarm.setChecked(false);
                    cancelAlarm(3);
                }
            });
        } else if (indexLayout == 4) {
            TextView alarmTime = (TextView) relative4.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative4.getChildAt(1);
            switchAlarm.setChecked(true);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(4);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm(4);
                    }
                }
            });

            ImageView bin = (ImageView) relative4.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative4.setVisibility(View.GONE);
                    switchAlarm.setChecked(false);
                    cancelAlarm(4);
                }
            });
        } else if (indexLayout == 5) {
            TextView alarmTime = (TextView) relative5.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative5.getChildAt(1);
            switchAlarm.setChecked(true);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(5);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm(5);
                    }
                }
            });

            ImageView bin = (ImageView) relative5.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative5.setVisibility(View.GONE);
                    switchAlarm.setChecked(false);
                    cancelAlarm(5);
                }
            });
        }
    }

    private void cancelAlarm(int indexLayout) {

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }

        alarmManager.cancel(pendingIntent);

        if (toastMessage != null) {
            toastMessage.cancel();
        }
        toastMessage = Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    private void setAlarm(int indexLayout) {
        TextView alarmTime;
        if (indexLayout == 1)
            alarmTime = (TextView) relative1.getChildAt(0);
        else if (indexLayout == 2)
            alarmTime = (TextView) relative2.getChildAt(0);
        else if (indexLayout == 3)
            alarmTime = (TextView) relative3.getChildAt(0);
        else if (indexLayout == 4)
            alarmTime = (TextView) relative4.getChildAt(0);
        else
            alarmTime = (TextView) relative5.getChildAt(0);


        String alarm = (String) alarmTime.getText();
        int hour;
        int minute;
        if (alarm.contains("PM")) { // turns AM to PM
            int separate1 = alarm.indexOf(" : ");
            int separate2 = alarm.indexOf(" PM");
            hour = Integer.parseInt(alarm.substring(0, separate1));
            minute = Integer.parseInt(alarm.substring(separate1 + 3, separate2));
            if (hour != 12) // 12.00 PM == 12.00 PM BUT 1 PM == 13 PM
                hour += 12;
        } else { // turns PM to AM
            int separate1 = alarm.indexOf(" : ");
            int separate2 = alarm.indexOf(" AM");
            hour = Integer.parseInt(alarm.substring(0, separate1));
            minute = Integer.parseInt(alarm.substring(separate1 + 3, separate2));
            if (hour == 12) // 12.00 AM == 00.00 AM
                hour -= 12;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // find oclocks distance
        int distanceHour;
        int distanceMinute;
        if (hour > currentHour) { // alarm is in the same day
            if (minute >= currentMinute) { // normal distance between 2 hours
                distanceHour = hour - currentHour;
                distanceMinute = minute - currentMinute;
            } else { // hour distance depends on the minutes too
                distanceHour = (int) (((hour - currentHour) * 60 - (currentMinute - minute)) / 60);
                distanceMinute = ((hour - currentHour) * 60 - (currentMinute - minute)) % 60;
            }
        } else if (hour < currentHour) { // alarm is in the next day
            if (minute >= currentMinute) {
                distanceHour = 24 - (currentHour - hour);
                distanceMinute = minute - currentMinute;
            } else {
                minute += 60;
                distanceHour = 22 - (int) (((currentHour - hour) * 60 - (minute - currentMinute)) / 60);
                distanceMinute = minute - currentMinute;
            }
        } else {
            if (minute >= currentMinute) {
                distanceHour = 0;
                distanceMinute = minute - currentMinute;
            } else {
                distanceHour = 23;
                distanceMinute = 60 - (currentMinute - minute);
            }
        }

        if (toastMessage != null)
            toastMessage.cancel();
        if (distanceHour != 0 && distanceMinute != 0)
            toastMessage = Toast.makeText(this, "Alarm set in: " + distanceHour + " hours and " + distanceMinute + " minutes from now", Toast.LENGTH_SHORT);
        if (distanceHour == 0)
            toastMessage = Toast.makeText(this, "Alarm set in: " + distanceMinute + " minutes from now", Toast.LENGTH_SHORT);
        if (distanceMinute == 0)
            toastMessage = Toast.makeText(this, "Alarm set in: " + distanceHour + " hours from now", Toast.LENGTH_SHORT);
        if (distanceHour == 0 && distanceMinute == 0)
            toastMessage = Toast.makeText(this, "Alarm will start any time soon", Toast.LENGTH_SHORT);

        toastMessage.show();

        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, );
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);

        long timeAtButtonClick = System.currentTimeMillis();
        long requestedAlarmTime = 1000L * 60 * distanceMinute + 1000L * 60 * 60 * distanceHour;
        long timediff = timeAtButtonClick - requestedAlarmTime;
        System.out.println(timediff);
//        System.out.println(timediff);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, timediff, pendingIntent);

        alarmManager.set(AlarmManager.RTC_WAKEUP, requestedAlarmTime, pendingIntent);
    }

    private void showTimePicker() {

        picker = new MaterialTimePicker.Builder() // setting the o'clock for new alarm timer
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(), "foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() { // clicked OK
            @Override
            public void onClick(View v) {
                int layout = getCorrectRelativeLayout(); // setting visible the layout
                setTextOfLayout(layout); // setting the text to the correct time and the switch to on
            }
        });
    }

    private int getCorrectRelativeLayout() {
        if (relative1.getVisibility() == View.GONE) {
            alarms.add(1);
            counterAlarms++;
            relative1.setVisibility(View.VISIBLE);
            // setAlarm(1) but set text is in setTextOfLayout so it moves there
            return 1;
        } else if (relative2.getVisibility() == View.GONE) {
            alarms.add(2);
            counterAlarms++;
            relative2.setVisibility(View.VISIBLE);
            // setAlarm(2) but set text is in setTextOfLayout so it moves there
            return 2;
        } else if (relative3.getVisibility() == View.GONE) {
            alarms.add(3);
            counterAlarms++;
            relative3.setVisibility(View.VISIBLE);
            // setAlarm(3) but set text is in setTextOfLayout so it moves there
            return 3;
        } else if (relative4.getVisibility() == View.GONE) {
            alarms.add(4);
            counterAlarms++;
            relative4.setVisibility(View.VISIBLE);
            // setAlarm(3) but set text is in setTextOfLayout so it moves there
            return 4;
        } else {
            alarms.add(5);
            counterAlarms++;
            relative5.setVisibility(View.VISIBLE);
            // setAlarm(3) but set text is in setTextOfLayout so it moves there
            return 5;
        }
    }

    private void setTextOfLayout(int indexLayout) {
        TextView alarmTime;
        if (indexLayout == 1) {
            alarmTime = (TextView) relative1.getChildAt(0);
        } else if (indexLayout == 2) {
            alarmTime = (TextView) relative2.getChildAt(0);
        } else if (indexLayout == 3) {
            alarmTime = (TextView) relative3.getChildAt(0);
        } else if (indexLayout == 4) {
            alarmTime = (TextView) relative4.getChildAt(0);
        } else {
            alarmTime = (TextView) relative5.getChildAt(0);
        }

        String minutes;
        if (picker.getMinute() < 10) {
            minutes = "0" + picker.getMinute();
        } else {
            minutes = "" + picker.getMinute();
        }
        if (picker.getHour() <= 12) {
            if (picker.getHour() == 12)
                alarmTime.setText(picker.getHour() + " : " + minutes + " PM");
            else if (picker.getHour() < 10)
                alarmTime.setText("0" + picker.getHour() + " : " + minutes + " AM");
            else
                alarmTime.setText(picker.getHour() + " : " + minutes + " AM");
        } else {
            if (picker.getHour() == 24)
                alarmTime.setText(picker.getHour() - 12 + " : " + minutes + " AM");
            else if (picker.getHour() - 12 < 10)
                alarmTime.setText("0" + (picker.getHour() - 12) + " : " + minutes + " PM");
            else
                alarmTime.setText((picker.getHour() - 12) + " : " + minutes + " PM");
        }

        if (indexLayout == 1) { // after text is set then setAlarm
            SwitchCompat switchCompat = (SwitchCompat) relative1.getChildAt(1);
            switchCompat.setChecked(true);
            setAlarm(1);
        } else if (indexLayout == 2) {
            SwitchCompat switchCompat = (SwitchCompat) relative2.getChildAt(1);
            switchCompat.setChecked(true);
            setAlarm(2);
        } else if (indexLayout == 3) {
            SwitchCompat switchCompat = (SwitchCompat) relative3.getChildAt(1);
            switchCompat.setChecked(true);
            setAlarm(3);
        } else if (indexLayout == 4) {
            SwitchCompat switchCompat = (SwitchCompat) relative4.getChildAt(1);
            switchCompat.setChecked(true);
            setAlarm(4);
        } else {
            SwitchCompat switchCompat = (SwitchCompat) relative5.getChildAt(1);
            switchCompat.setChecked(true);
            setAlarm(5);
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }



}