package com.coders.quantum.myapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.coders.quantum.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.MyViewHolder> {

    Context context;

    ArrayList<HashMap<String, String>> contacts;
    private RecyclerView recyclerView;

    //constructor
    public QuizzesAdapter(Context context, ArrayList<HashMap<String, String>> contacts, RecyclerView recyclerView) {
        this.context = context;
        this.contacts = contacts;
        this.recyclerView = recyclerView;
    }

    @Override
    public QuizzesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quizzes, parent, false);
        return new QuizzesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizzesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        HashMap<String, String> questionData = contacts.get(position);

        // Set question text
        holder.txtQuestion.setText(questionData.get("question"));

        // Clear previous options in RadioGroup
        holder.radioGroup.removeAllViews();

        // Parse the options from JSON and create RadioButtons
        try {
            JSONArray options = new JSONArray(questionData.get("options"));
            for (int i = 0; i < options.length(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(options.getString(i));
                holder.radioGroup.addView(radioButton);

                // Set a listener to show the answer and auto-scroll
                radioButton.setOnClickListener(v -> {
                    showCorrectAnswer(holder, questionData.get("correctOption"));

                    // Use a Handler to scroll to the next item after 5 seconds
                    new Handler().postDelayed(() -> {
                        if (position < contacts.size() - 1) {
                            recyclerView.smoothScrollToPosition(position + 1);
                        }
                    }, 5000);
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void showCorrectAnswer(MyViewHolder holder, String correctOption) {
        // Here you can display the correct answer in a Toast, Snackbar, or any other method
        // For example, you could use a Toast:
        Toast.makeText(context, "Correct Answer: " + correctOption, Toast.LENGTH_SHORT).show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        RadioGroup radioGroup;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtItemmQuizQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroupItemQuiz);
        }
    }

}