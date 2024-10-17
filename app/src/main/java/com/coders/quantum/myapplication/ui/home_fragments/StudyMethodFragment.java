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

    Button btnHomework, btnQuizzes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_study_method, container, false);

        btnQuizzes = v.findViewById(R.id.btnStudyMethodQuizzes);
        btnHomework = v.findViewById(R.id.btnStudyMethodHomework);

        btnHomework.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), HomeworkActivity.class);
            startActivity(intent);
        });

        btnQuizzes.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), QuizzesActivity.class);
            startActivity(intent);
        });

        return v;
    }
}