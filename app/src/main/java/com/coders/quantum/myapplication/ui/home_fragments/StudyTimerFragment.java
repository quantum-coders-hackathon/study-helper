package com.coders.quantum.myapplication.ui.home_fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.ui.game.J2048View;

import java.util.Random;

public class StudyTimerFragment extends Fragment {


    public StudyTimerFragment() {
        // Required empty public constructor
    }

    TextView txtStudyTimerStudy,txtStudyTimerBreak,txtStudyTimerTime,txtStudyTimerMotivationalQuote;
    Switch switchStudyTimerLockApps, switchStudyTimerMusic;
    ImageView imgStudyTimerStart, imgStudyTimerPause;
    J2048View j2048View;
    LinearLayout linearLayoutStudyTimerMotivationalQuote;
    MediaPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private int[] musicFiles = {R.raw.carol_of_the_bells, R.raw.monoman, R.raw.relaxing_piano};
    public int selectedTimeInMinutes = 25;
    public int selectedBreakTimeInMinutes = 5;
    CountDownTimer countDownTimer;
    long selectedTimeInMillis = selectedTimeInMinutes * 60 * 1000;
    long remainingTimeInMillis = selectedTimeInMinutes * 60 * 1000;
    boolean isPaused = false, isBreak = false;

    //the quotes will be in Firebase later
    private String[] quotes = {
            "The future belongs to <b>those</b> who believe in the beauty of their dreams.<br>- Eleanor Roosevelt",
            "Success is not the key to happiness.<br><b>Happiness</b> is the key to success.<br>If you love what you are doing, you will be successful.<br>- Albert Schweitzer",
            "It does not matter how <b>slowly</b> you go as long as you do not <b>stop</b>.<br>- Confucius",
            "The only way to do <b>great work</b> is to love what you do.<br>- Steve Jobs",
            "You are never too old to set another <b>goal</b> or to dream a new dream.<br>- C.S. Lewis",
            "<b>Believe</b> you can and you're halfway there.<br>- Theodore Roosevelt",
            "Your limitation—it's only your <b>imagination</b>.",
            "Push yourself, because no one else is going to do it for you.",
            "Great things never come from <b>comfort zones</b>.",
            "<b>Dream</b> it. <b>Wish</b> it. <b>Do</b> it."
    };

    Handler handler = new Handler();
    Random random = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_study_timer, container, false);
        
        Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        txtStudyTimerStudy = v.findViewById(R.id.txtStudyTimerStudy);
        txtStudyTimerBreak = v.findViewById(R.id.txtStudyTimerBreak);
        txtStudyTimerTime = v.findViewById(R.id.txtStudyTimerTime);
        txtStudyTimerMotivationalQuote = v.findViewById(R.id.txtStudyTimerMotivationalQuote);
        switchStudyTimerLockApps = v.findViewById(R.id.switchStudyTimerLockApps);
        imgStudyTimerStart = v.findViewById(R.id.imgStudyTimerStart);
        imgStudyTimerPause = v.findViewById(R.id.imgStudyTimerPause);
        j2048View = v.findViewById(R.id.puzzle);
        linearLayoutStudyTimerMotivationalQuote = v.findViewById(R.id.linearLayoutStudyTimerMotivationalQuote);
        switchStudyTimerMusic = v.findViewById(R.id.switchStudyTimerMusic);

        j2048View.readData();

        txtStudyTimerStudy.setOnClickListener(view->{
            txtStudyTimerStudy.setBackgroundResource(R.drawable.selector_with_default_state);
            txtStudyTimerBreak.setBackgroundResource(R.drawable.selector);
            isBreak = false;
            selectedTimeInMillis = selectedTimeInMinutes * 60 * 1000;
            remainingTimeInMillis = selectedTimeInMinutes * 60 * 1000;

            updateTimeDisplay(remainingTimeInMillis);
        });

        txtStudyTimerBreak.setOnClickListener(view->{
            txtStudyTimerBreak.setBackgroundResource(R.drawable.selector_with_default_state);
            txtStudyTimerStudy.setBackgroundResource(R.drawable.selector);
            isBreak = true;
            selectedTimeInMillis = selectedBreakTimeInMinutes * 60 * 1000;
            remainingTimeInMillis = selectedBreakTimeInMinutes * 60 * 1000;

            updateTimeDisplay(remainingTimeInMillis);
        });

        txtStudyTimerTime.setOnClickListener(view->{
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    v.getContext(),
                    (view1, hourOfDay, minute) -> {
                        if (!isBreak){
                            selectedTimeInMinutes = (hourOfDay * 60) + minute;
                            selectedTimeInMillis = selectedTimeInMinutes * 60 * 1000;
                        } else {
                            selectedBreakTimeInMinutes = (hourOfDay * 60) + minute;
                            selectedTimeInMillis = selectedBreakTimeInMinutes * 60 * 1000;
                        }
                        remainingTimeInMillis = selectedTimeInMillis;
                        txtStudyTimerTime.setText(String.format("%02d:%02d:00", hourOfDay, minute));
                    },
                    0,
                    !isBreak ? selectedTimeInMinutes : selectedBreakTimeInMinutes,
                    true
            );
            timePickerDialog.show();
        });

        imgStudyTimerStart.setOnClickListener(view->{
            txtStudyTimerStudy.setClickable(false);
            txtStudyTimerBreak.setClickable(false);
            txtStudyTimerTime.setClickable(false);
            switchStudyTimerLockApps.setClickable(false);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            switchStudyTimerLockApps.setChecked(true);

            imgStudyTimerStart.setVisibility(View.INVISIBLE);
            imgStudyTimerPause.setVisibility(View.VISIBLE);

            long timeToStart = isPaused ? remainingTimeInMillis : selectedTimeInMillis;

            countDownTimer = new CountDownTimer(timeToStart, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeInMillis = millisUntilFinished;
                    updateTimeDisplay(remainingTimeInMillis);
                }

                @Override
                public void onFinish() {
                    if (!isBreak) {
                        isBreak = true;
                        txtStudyTimerBreak.setBackgroundResource(R.drawable.selector_with_default_state);
                        txtStudyTimerStudy.setBackgroundResource(R.drawable.selector);

                        selectedTimeInMillis = selectedBreakTimeInMinutes * 60 * 1000;
                        remainingTimeInMillis = selectedBreakTimeInMinutes * 60 * 1000;

                        txtStudyTimerStudy.setClickable(true);
                        txtStudyTimerBreak.setClickable(true);
                        txtStudyTimerTime.setClickable(true);
                        switchStudyTimerLockApps.setClickable(true);

                        j2048View.setVisibility(View.VISIBLE);
                        linearLayoutStudyTimerMotivationalQuote.setVisibility(View.GONE);

//                        Toast.makeText(view.getContext(), " "+selectedBreakTimeInMinutes+" "+remainingTimeInMillis, Toast.LENGTH_SHORT).show();
                    } else {
                        isBreak = false;
                        txtStudyTimerStudy.setBackgroundResource(R.drawable.selector_with_default_state);
                        txtStudyTimerBreak.setBackgroundResource(R.drawable.selector);

                        selectedTimeInMillis = selectedTimeInMinutes * 60 * 1000;
                        remainingTimeInMillis = selectedTimeInMinutes * 60 * 1000;

                        j2048View.setVisibility(View.GONE);
                        j2048View.saveData();
                        linearLayoutStudyTimerMotivationalQuote.setVisibility(View.VISIBLE);
                    }
                    updateTimeDisplay(remainingTimeInMillis);
//                    txtStudyTimerTime.setText("00:05:00");

                    triggerVibration(vibrator,1000);

                    isPaused = true;
                    imgStudyTimerPause.setVisibility(View.INVISIBLE);
                    imgStudyTimerStart.setVisibility(View.VISIBLE);
                }
            }.start();
            isPaused = false;
        });

        imgStudyTimerPause.setOnClickListener(view->{
            imgStudyTimerStart.setVisibility(View.VISIBLE);
            imgStudyTimerPause.setVisibility(View.INVISIBLE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
                isPaused = true;
            }
            txtStudyTimerStudy.setClickable(true);
            txtStudyTimerBreak.setClickable(true);
            txtStudyTimerTime.setClickable(true);
            switchStudyTimerLockApps.setClickable(true);
        });

        switchStudyTimerLockApps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isDeviceOwner()) {
                    if (getActivity() != null) {
                        getActivity().startLockTask();
//                        Toast.makeText(getActivity(), "Lock Task Mode Enabled", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    switchStudyTimerLockApps.setChecked(false);
//                    Toast.makeText(getActivity(), "App is not the device owner", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (getActivity() != null) {
                    getActivity().stopLockTask();
//                    Toast.makeText(getActivity(), "Lock Task Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchStudyTimerMusic.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                int randomIndex = new Random().nextInt(musicFiles.length);
                player = MediaPlayer.create(getActivity(), musicFiles[randomIndex]);
                player.setLooping(true);
                player.start();
            } else {
                player.release();
                player = null;
            }
        }));

        updateQuote();
        startQuoteUpdater();

        return v;
    }

    private void updateTimeDisplay(long timeInMillis) {
        int hours = (int) (timeInMillis / (1000 * 60 * 60));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int seconds = (int) (timeInMillis / 1000) % 60;

        txtStudyTimerTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void triggerVibration(Vibrator vibrator, long duration) {
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(duration);
            }
        } else {
            Log.d("Vibration", "Vibrator is null.");
        }
    }


    private boolean isDeviceOwner() {
        return true; // For testing purposes
    }

    private void updateQuote() {
        int randomIndex = random.nextInt(quotes.length);
        String randomQuote = quotes[randomIndex];
        txtStudyTimerMotivationalQuote.setText(Html.fromHtml(randomQuote, Html.FROM_HTML_MODE_COMPACT));
        txtStudyTimerMotivationalQuote.setTextColor(getRandomColor());
    }

    private void startQuoteUpdater() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateQuote();
                handler.postDelayed(this, 30000); // Change quote every 30 seconds
            }
        }, 30000); // Initial delay of 30 seconds
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop the handler when the activity is destroyed
    }
}