package com.coders.quantum.myapplication.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.coders.quantum.myapplication.R;



public class Home1 extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView motivationalQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        progressBar = findViewById(R.id.horizontal_progress_bar);
        motivationalQuote = findViewById(R.id.txtStudyTimerMotivationalQuote);

        progressBar.setProgress(70);
        motivationalQuote.setText("Stay focused and keep studying!");
    }
}


