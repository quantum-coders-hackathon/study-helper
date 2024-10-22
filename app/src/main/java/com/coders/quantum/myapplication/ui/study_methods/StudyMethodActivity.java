package com.coders.quantum.myapplication.ui.study_methods;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.coders.quantum.myapplication.R;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class StudyMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_method);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String studyMethod = bundle.getString("study_method");

        TextView txtStudyMethodList = findViewById(R.id.txtStudyMethodList);
        GifImageView gifImageView = findViewById(R.id.gif);

        if (studyMethod != null) {
            txtStudyMethodList.setText(Html.fromHtml(studyMethod, Html.FROM_HTML_MODE_COMPACT));
        }

        try {
            GifDrawable gif1 = new GifDrawable( getResources(), R.drawable.gif1 );
            GifDrawable gif2 = new GifDrawable( getResources(), R.drawable.gif2 );
            GifDrawable gif3 = new GifDrawable( getResources(), R.drawable.gif3 );
            GifDrawable gif4 = new GifDrawable( getResources(), R.drawable.gif4 );

            GifDrawable[] gifDrawables = {gif1, gif2, gif3, gif4};

            Random random = new Random();
            GifDrawable selectedGif = gifDrawables[random.nextInt(gifDrawables.length)];

            gifImageView.setImageDrawable(selectedGif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}