package com.coders.quantum.myapplication.ui.home_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.ui.study_methods.HomeworkActivity;
import com.coders.quantum.myapplication.ui.study_methods.QuizzesActivity;
import com.coders.quantum.myapplication.ui.study_methods.StudyMethodActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyMethodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyMethodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudyMethodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyMethodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyMethodFragment newInstance(String param1, String param2) {
        StudyMethodFragment fragment = new StudyMethodFragment();
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

    Button btnHomework, btnQuizzes, btnPomodoro, btnActiveRecall, btnBlurtingMethod,btnSpacedRepitition,btnMindMaps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_study_method, container, false);

        btnQuizzes = v.findViewById(R.id.btnStudyMethodQuizzes);
        btnHomework = v.findViewById(R.id.btnStudyMethodHomework);
        btnPomodoro = v.findViewById(R.id.btnPomodoro);
        btnActiveRecall = v.findViewById(R.id.btnAciveRecall);
        btnBlurtingMethod = v.findViewById(R.id.btnBlurtingMethods);
        btnSpacedRepitition = v.findViewById(R.id.btnSpacedRepitition);
        btnMindMaps = v.findViewById(R.id.btnMindMaps);

        btnHomework.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), HomeworkActivity.class);
            startActivity(intent);
        });

        btnQuizzes.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), QuizzesActivity.class);
            startActivity(intent);
        });

        btnPomodoro.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
            intent.putExtra("name","pomodoro");
            intent.putExtra("study_method","<h1>Pomodoro Technique:</h1>" +
                    "      <ul>\n" +
                    "        <li>Work for 25 minutes without distraction.</li>\n" +
                    "        <li>Then take a short 5-minute break.</li>\n" +
                    "        <li>After four work periods, take a longer break (15-30 minutes).</li>\n" +
                    "        <li>This method helps you stay focused and avoid burnout.</li>\n" +
                    "      </ul>");
            startActivity(intent);
        });

        btnActiveRecall.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
            intent.putExtra("name","active_recall");
            intent.putExtra("study_method","<h1>Active Recall:</h1>\n" +
                    "      <ul>\n" +
                    "        <li>Test yourself on what you’re studying instead of just reading notes.</li>\n" +
                    "        <li>Ask questions and try to remember the answers from memory.</li>\n" +
                    "        <li>This helps strengthen your memory and shows you what you still need to review.</li>\n" +
                    "      </ul>\n" +
                    "    </li>");
            startActivity(intent);
        });

        btnBlurtingMethod.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
            intent.putExtra("name","blurting_method");
            intent.putExtra("study_method","<h1>Blurting:</h1>\n" +
                    "      <ul>\n" +
                    "        <li>Write down everything you know about a topic without checking your notes.</li>\n" +
                    "        <li>Afterward, look at what you missed and compare it with your notes.</li>\n" +
                    "        <li>This shows where your knowledge is h1 and where you need more practice.</li>\n" +
                    "      </ul>");
            startActivity(intent);
        });

        btnSpacedRepitition.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
            intent.putExtra("name","spaced_repetition");
            intent.putExtra("study_method","<h1>Spaced Repetition:</h1>\n" +
                    "      <ul>\n" +
                    "        <li>Review information at different times: right after learning, after a day, after a week, etc.</li>\n" +
                    "        <li>This method helps you remember things for a long time by using the brain’s natural forgetting process.</li>\n" +
                    "      </ul>");
            startActivity(intent);
        });

        btnMindMaps.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), StudyMethodActivity.class);
            intent.putExtra("name","mind_maps");
            intent.putExtra("study_method","<h1>Mind Maps:</h1>\n" +
                    "      <ul>\n" +
                    "        <li>Create a visual diagram starting with one main idea.</li>\n" +
                    "        <li>Branch out into related subtopics using keywords, pictures, or symbols.</li>\n" +
                    "        <li>This helps you understand complex subjects and connect ideas easily.</li>\n" +
                    "      </ul>");
            startActivity(intent);
        });

        return v;
    }
}