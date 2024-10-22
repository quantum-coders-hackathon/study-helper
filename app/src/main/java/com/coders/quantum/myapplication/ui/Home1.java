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
        // Set the layout to activity_progressbar.xml
        setContentView(R.layout.activity_progressbar);

        // Initialize UI components from the activity_progressbar.xml layout
        progressBar = findViewById(R.id.horizontal_progress_bar);
        motivationalQuote = findViewById(R.id.txtStudyTimerMotivationalQuote);

        // Set initial values for components (e.g., progress and quote)
        progressBar.setProgress(70); // Set any progress you need
        motivationalQuote.setText("Stay focused and keep studying!"); // Update the quote
    }
}


