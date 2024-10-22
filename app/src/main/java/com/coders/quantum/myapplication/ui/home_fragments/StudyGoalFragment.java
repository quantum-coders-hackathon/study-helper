package com.coders.quantum.myapplication.ui.home_fragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.coders.quantum.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class StudyGoalFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudyGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyGoalFragment newInstance(String param1, String param2) {
        StudyGoalFragment fragment = new StudyGoalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SharedPreferences sharedPref;
    TextView timeLeftTextView;
    EditText sundayGoal, mondayGoal, tuesdayGoal, wednesdayGoal, thursdayGoal, fridayGoal, saturdayGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_goal, container, false);

        sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        TextView streakTextView = view.findViewById(R.id.streakTextView);
        timeLeftTextView = view.findViewById(R.id.timeLeftTextView);

        updateTimeLeft(timeLeftTextView);
        updateProgressBar(progressBar);

        EditText timeInput = view.findViewById(R.id.timeInput);
        Button addTimeButton = view.findViewById(R.id.addTimeButton);

        TimePicker startTimePicker = view.findViewById(R.id.reminderStartTime);
        TimePicker endTimePicker = view.findViewById(R.id.reminderEndTime);

        CheckBox reminderSunday = view.findViewById(R.id.reminderSunday);
        CheckBox reminderMonday = view.findViewById(R.id.reminderMonday);
        CheckBox reminderTuesday = view.findViewById(R.id.reminderTuesday);
        CheckBox reminderWednesday = view.findViewById(R.id.reminderWednesday);
        CheckBox reminderThursday = view.findViewById(R.id.reminderThursday);
        CheckBox reminderFriday = view.findViewById(R.id.reminderFriday);
        CheckBox reminderSaturday = view.findViewById(R.id.reminderSaturday);

        Button saveRemindersButton = view.findViewById(R.id.saveReminderSettingsButton);

        saveRemindersButton.setOnClickListener(v -> {
            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();

            boolean[] selectedDays = new boolean[7];
            selectedDays[0] = reminderSunday.isChecked();
            selectedDays[1] = reminderMonday.isChecked();
            selectedDays[2] = reminderTuesday.isChecked();
            selectedDays[3] = reminderWednesday.isChecked();
            selectedDays[4] = reminderThursday.isChecked();
            selectedDays[5] = reminderFriday.isChecked();
            selectedDays[6] = reminderSaturday.isChecked();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("startHour", startHour);
            editor.putInt("startMinute", startMinute);
            editor.putInt("endHour", endHour);
            editor.putInt("endMinute", endMinute);

            editor.putBoolean("reminderSunday", reminderSunday.isChecked());
            editor.putBoolean("reminderMonday", reminderMonday.isChecked());
            editor.putBoolean("reminderTuesday", reminderTuesday.isChecked());
            editor.putBoolean("reminderWednesday", reminderWednesday.isChecked());
            editor.putBoolean("reminderThursday", reminderThursday.isChecked());
            editor.putBoolean("reminderFriday", reminderFriday.isChecked());
            editor.putBoolean("reminderSaturday", reminderSaturday.isChecked());

            editor.apply();
            setHourlyReminders(startHour, startMinute, endHour, endMinute, selectedDays);
            Toast.makeText(getContext(), "Reminders Set!", Toast.LENGTH_SHORT).show();
        });

        addTimeButton.setOnClickListener(v -> {
            //get time
            int addedTime = Integer.parseInt(timeInput.getText().toString());
            //add the time
            int currentTimeStudied = getMinutesStudiedToday();
            int newTotalTime = currentTimeStudied + addedTime;
            storeNewStudyTime(newTotalTime);

            int dailyGoal = getDailyGoal();
            boolean goalMet = newTotalTime >= dailyGoal;
            updateStreak(goalMet);
            updateProgressBar(progressBar);
            updateTimeLeft(timeLeftTextView);

            int currentStreak = getActivity().getSharedPreferences("StudyPrefs",Context.MODE_PRIVATE).getInt("streak", 0);
            streakTextView.setText("Current Streak: "+currentStreak);

            Toast.makeText(getContext(), "Time Added!", Toast.LENGTH_SHORT).show();

        });

        int currentStreak = getActivity().getSharedPreferences("StudyPrefs",Context.MODE_PRIVATE).getInt("streak", 0);
        streakTextView.setText("Current Streak: "+currentStreak);

        sundayGoal = view.findViewById(R.id.sundayGoal);
        mondayGoal = view.findViewById(R.id.mondayGoal);
        tuesdayGoal = view.findViewById(R.id.tuesdayGoal);
        wednesdayGoal = view.findViewById(R.id.wednesdayGoal);
        thursdayGoal = view.findViewById(R.id.thursdayGoal);
        fridayGoal = view.findViewById(R.id.fridayGoal);
        saturdayGoal = view.findViewById(R.id.saturdayGoal);
        Button setGoalsButton = view.findViewById(R.id.setGoalsButton);

        setGoalsButton.setOnClickListener(v -> {
            int mondayGoalValue = Integer.parseInt(mondayGoal.getText().toString());
            int tuesdayGoalValue = Integer.parseInt(tuesdayGoal.getText().toString());
            int sundayGoalValue = Integer.parseInt(sundayGoal.getText().toString());
            int wednesdayGoalValue = Integer.parseInt(wednesdayGoal.getText().toString());
            int thursdayGoalValue = Integer.parseInt(thursdayGoal.getText().toString());
            int fridayGoalValue = Integer.parseInt(fridayGoal.getText().toString());
            int saturdayGoalValue = Integer.parseInt(saturdayGoal.getText().toString());

            setDailyGoalForDay("sunday", sundayGoalValue);
            setDailyGoalForDay("monday", mondayGoalValue);
            setDailyGoalForDay("tuesday", tuesdayGoalValue);
            setDailyGoalForDay("wednesday", wednesdayGoalValue);
            setDailyGoalForDay("thursday", thursdayGoalValue);
            setDailyGoalForDay("friday", fridayGoalValue);
            setDailyGoalForDay("saturday", saturdayGoalValue);

            Toast.makeText(getContext(), "Daily Goals Set!", Toast.LENGTH_SHORT).show();
        });

        sundayGoal.setText(String.valueOf(getDailyGoalForDay("sunday")));
        mondayGoal.setText(String.valueOf(getDailyGoalForDay("monday")));
        tuesdayGoal.setText(String.valueOf(getDailyGoalForDay("tuesday")));
        wednesdayGoal.setText(String.valueOf(getDailyGoalForDay("wednesday")));
        thursdayGoal.setText(String.valueOf(getDailyGoalForDay("thursday")));
        fridayGoal.setText(String.valueOf(getDailyGoalForDay("friday")));
        saturdayGoal.setText(String.valueOf(getDailyGoalForDay("saturday")));

        return view;
    }

    private void updateTimeLeft(TextView timeLeftTextView){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDay = "";

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                currentDay = "sunday";
                break;
            case Calendar.MONDAY:
                currentDay = "monday";
                break;
            case Calendar.TUESDAY:
                currentDay = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "wednesday";
                break;
            case Calendar.THURSDAY:
                currentDay = "thursday";
                break;
            case Calendar.FRIDAY:
                currentDay = "friday";
                break;
            case Calendar.SATURDAY:
                currentDay = "saturday";
                break;
        }
        int dailyGoal = getDailyGoalForDay(currentDay);
        int minutesStudied = getMinutesStudiedToday();

        int minutesLeft = dailyGoal - minutesStudied;
        if (minutesLeft < 0){
            minutesLeft = 0;
        }

        timeLeftTextView.setText("Minutes Left: " + minutesLeft);

    }

    private void storeNewStudyTime(int newTime){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("TimeStudiedToday" , newTime);
        editor.apply();
    }

    private void updateProgressBar(ProgressBar progressBar){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String currentDay = "";

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                currentDay = "sunday";
                break;
            case Calendar.MONDAY:
                currentDay = "monday";
                break;
            case Calendar.TUESDAY:
                currentDay = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "wednesday";
                break;
            case Calendar.THURSDAY:
                currentDay = "thursday";
                break;
            case Calendar.FRIDAY:
                currentDay = "friday";
                break;
            case Calendar.SATURDAY:
                currentDay = "saturday";
                break;
        }


        int dailyGoal = getDailyGoalForDay(currentDay); //goal will be set in minutes
        int minutesStudied = getMinutesStudiedToday();

        if(dailyGoal>0){
            int progress = (minutesStudied * 100)/ dailyGoal; //calc percent of goal complete

            if (progress > 100){
                progress = 100;
            }

            progressBar.setProgress(progress);

            if (progress == 100) {
                timeLeftTextView.setText("Studied : "+minutesStudied+" minutes");
            }
        }
        else {
            progressBar.setProgress(0);
        }

    }

    private int getMinutesStudiedToday() {
        return sharedPref.getInt("TimeStudiedToday", 0);
    }

    private int getDailyGoal(){
        return sharedPref.getInt("daily_goal", 120); //default of 120 mins
    }

    //streak counter and updater
    private void updateStreak(boolean goalMet) {
        SharedPreferences.Editor editor = sharedPref.edit();

        int currentStreak = sharedPref.getInt("streak",0);
        String lastStudyDate = sharedPref.getString("lastStudyDate","");

        //todays date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String today = sdf.format(new Date());

        if (goalMet){
            if (!today.equals(lastStudyDate)){
                currentStreak++;
                editor.putString("lastStudyDate",today);
            }
        }
        else {
            currentStreak = 0;
        }
        editor.putInt("streak", currentStreak);
        editor.apply();

    }

//    private void setReminder(int hour, int minute, boolean[] days){
//        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//
//        //make alarms for the days user choose
//        for (int i = 0; i < days.length ; i++){
//            if (days[i]){
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
//
//                Intent intent = new Intent(getContext(), ReminderReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i , intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//                        AlarmManager.INTERVAL_DAY * 7 , pendingIntent);
//            }
//        }
//
//    }
    private void setHourlyReminders(int startHour, int startMinute, int endHour, int endMinute, boolean[] selectedDays){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long interval = AlarmManager.INTERVAL_HOUR;

        for (int i = 0 ; i < selectedDays.length; i++){
            if (selectedDays[i]){
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
            }
        }

        cancelReminderAfterEndTime(endHour, endMinute);
    }

    public class ReminderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            createNotificationChannel(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }

            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);

            int startHour = sharedPref.getInt("startHour", 9);
            int endHour = sharedPref.getInt("endHour", 17);

            if (currentHour >= startHour && currentHour < endHour){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "StudyReminderChannel")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Study Reminder")
                            .setContentText("Time to Study!")
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                notificationManager.notify(1, builder.build());
            }




        }

        private void createNotificationChannel(Context context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Study Reminder Channel";
                String description = "Channel for Study Reminders";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("StudyReminderChannel", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

            }

        }


    }


    private void cancelReminderAfterEndTime(int endHour, int endMinute){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, endMinute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private void setDailyGoalForDay(String day, int goalMinutes){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(day + "_goal", goalMinutes);
        editor.apply();
    }

    private int getDailyGoalForDay(String day){
        return sharedPref.getInt(day + "_goal", 120);
    }

}