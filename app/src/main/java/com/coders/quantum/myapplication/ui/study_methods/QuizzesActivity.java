package com.coders.quantum.myapplication.ui.study_methods;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.model.QuizzesAdapter;
import com.coders.quantum.myapplication.model.sql_db.QuizzesSQLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizzesActivity extends AppCompatActivity {

    Spinner spinnerSubjects, spinnerTopics;
    List<String> arraySubjects, arrayTopics;
    ArrayAdapter<String> adapterSub, adapterTopics = null;
    QuizzesSQLite quizzesSQLite;
    ImageButton imgBtnAddQuizzes;
    RecyclerView recyclerViewQuizzes;
    LinearLayout llPaginationBtnContainer;
    ArrayList<HashMap<String, String>> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quizzes);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        spinnerTopics = findViewById(R.id.spinnerTopics);
        imgBtnAddQuizzes = findViewById(R.id.imgBtnAddQuiz);
        recyclerViewQuizzes = findViewById(R.id.recyclerViewQuizList);
        llPaginationBtnContainer = findViewById(R.id.linearLayoutPaginationBtnContainer);
        quizzesSQLite = new QuizzesSQLite(QuizzesActivity.this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewQuizzes.setLayoutManager(linearLayoutManager);
        recyclerViewQuizzes.setAdapter(new QuizzesAdapter(QuizzesActivity.this, new ArrayList<>(), recyclerViewQuizzes));

        subjectList();

        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectTopics();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgBtnAddQuizzes.setOnClickListener(v->{
            Intent intent = new Intent(QuizzesActivity.this, AddQuizActivity.class);
            startActivity(intent);
        });
        spinnerTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void subjectList() {
        arraySubjects = quizzesSQLite.getAllSubjects();

        adapterSub = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySubjects);

        adapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapterSub);
    }

    private void subjectTopics() {
        arrayTopics = null;
        arrayTopics = quizzesSQLite.getTopicsBySubject(spinnerSubjects.getSelectedItem().toString());

        if (adapterTopics == null) {
            adapterTopics = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, arrayTopics);
            adapterTopics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTopics.setAdapter(adapterTopics);
        }

        adapterTopics.notifyDataSetChanged();
    }

    private void fetchQuestions() {
        questions = quizzesSQLite.getQuestionsBySubjectAndTopic(spinnerSubjects.getSelectedItem().toString(), spinnerTopics.getSelectedItem().toString(), 15, 0);

        QuizzesAdapter quizzesAdapter = new QuizzesAdapter(QuizzesActivity.this,questions,recyclerViewQuizzes);
        recyclerViewQuizzes.setAdapter(quizzesAdapter);
        pagination();
    }

    private void pagination() {
        int page = quizzesSQLite.getTotalPages(spinnerSubjects.getSelectedItem().toString(), spinnerTopics.getSelectedItem().toString(), 15);

        llPaginationBtnContainer.removeAllViews();

        for (int i = 1; i <= page; i++) {
            TextView pageTextView = new TextView(this);

            pageTextView.setText(String.valueOf(i));

            SpannableString spannableString = new SpannableString(pageTextView.getText());
            spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
            pageTextView.setText(spannableString);
//            pageTextView.setTextColor(getResources().getColor(R.color.blue)); // Replace with your blue color resource

            pageTextView.setBackgroundResource(R.drawable.selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8); // Add some margin between buttons
            pageTextView.setLayoutParams(params);

            // Set an OnClickListener if needed
            pageTextView.setOnClickListener(v -> {
                // Handle page click, e.g., load the selected page
//                loadPage(i);
            });

            // Add the TextView to the pagination container
            llPaginationBtnContainer.addView(pageTextView);
        }
    }
}