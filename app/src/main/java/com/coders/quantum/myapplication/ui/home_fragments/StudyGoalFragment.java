package com.coders.quantum.myapplication.ui.home_fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_goal, container, false);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView streakTextView = view.findViewById(R.id.streakTextView);
        TextView timeLeftTextView = view.findViewById(R.id.timeLeftTextView);

        updateProgressBar(progressBar);
        updateTimeLeft(timeLeftTextView);

        EditText timeInput = view.findViewById(R.id.timeInput);
        Button addTimeButton = view.findViewById(R.id.addTimeButton);

        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                int currentStreak = getActivity().getSharedPreferences("StudyPrefs",Context.MODE_PRIVATE).getInt("streak", 0);
                streakTextView.setText("Current Streak: "+currentStreak);

                updateTimeLeft(timeLeftTextView);
            }

        });

        EditText sundayGoal = view.findViewById(R.id.sundayGoal);
        EditText mondayGoal = view.findViewById(R.id.mondayGoal);
        EditText tuesdayGoal = view.findViewById(R.id.tuesdayGoal);
        EditText wednesdayGoal = view.findViewById(R.id.wednesdayGoal);
        EditText thursdayGoal = view.findViewById(R.id.thursdayGoal);
        EditText fridayGoal = view.findViewById(R.id.fridayGoal);
        EditText saturdayGoal = view.findViewById(R.id.saturdayGoal);
        Button setGoalsButton = view.findViewById(R.id.setGoalsButton);

        setGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sundayGoalValue = Integer.parseInt(sundayGoal.getText().toString());
                int mondayGoalValue = Integer.parseInt(mondayGoal.getText().toString());
                int tuesdayGoalValue = Integer.parseInt(tuesdayGoal.getText().toString());
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
            }
        });

        return view;
    }

    private void updateTimeLeft(TextView timeLeftTextView){
        int dailyGoal = getDailyGoal();
        int minutesStudied = getMinutesStudiedToday();

        int minutesLeft = dailyGoal - minutesStudied;
        if (minutesLeft < 0){
            minutesLeft = 0;
        }

        timeLeftTextView.setText("Minutes Left: " + minutesLeft);

    }

    private void storeNewStudyTime(int newTime){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("TimeStudiedToday" , newTime);
        editor.apply();
    }

    private void updateProgressBar(ProgressBar progressBar){
        int dailyGoal = getDailyGoal(); //goal will be set in minutes
        int minutesStudied = getMinutesStudiedToday();
        int progress = (minutesStudied * 100)/ dailyGoal; //calc percent of goal complete

        if (progress > 100){
            progress = 100;
        }

        progressBar.setProgress(progress);
    }

    private int getMinutesStudiedToday() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        return sharedPref.getInt("TimeStudiedToday", 0);
    }

    private void setDailyGoal(int goalMinutes) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("daily_goal", goalMinutes);
        editor.apply();
    }

    private int getDailyGoal(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        return sharedPref.getInt("daily_goal", 120); //default of 120 mins
    }

    //streak counter and updater
    private void updateStreak(boolean goalMet) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs",Context.MODE_PRIVATE);
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

    private void setReminder(int hour, int minute, boolean[] days){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        //make alarms for the days user choose
        for (int i = 0; i < days.length ; i++){
            if (days[i]){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);

                Intent intent = new Intent(getContext(), ReminderReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i , intent, 0);


                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 7 , pendingIntent);
            }
        }

    }

    public class ReminderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "StudyReminderChannel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Study Reminder")
                    .setContentText("Time to study!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0 , builder.build());
        }


    }

    private void setDailyGoalForDay(String day, int goalMinutes){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(day + "_goal", goalMinutes);
        editor.apply();
    }

    private int getDailyGoalForDay(String day){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("StudyPrefs", Context.MODE_PRIVATE);
        return sharedPref.getInt(day + "_goal", 120);
    }

}