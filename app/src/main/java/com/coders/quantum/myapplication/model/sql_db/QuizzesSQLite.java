package com.coders.quantum.myapplication.model.sql_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.coders.quantum.myapplication.model.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizzesSQLite extends SQLiteOpenHelper {

    private static final int DB_VERSION = Constant.getDbVersion();
    private static final String DB_NAME = Constant.getDbName();
    private static final String TABLE = "quizzes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_TOPIC = "topic";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_OPTIONS = "options";
    private static final String COLUMN_CORRECT_OPTION = "correct_option";

    public QuizzesSQLite(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_TOPIC + " TEXT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_OPTIONS + " TEXT," +// Store options as a JSON string
                COLUMN_CORRECT_OPTION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public synchronized long insertQuiz(String subject, String topic, String question, String optionsJson, String correctOption) {
        Log.d("quizzes","Sub : "+subject+". Topic : "+topic+". Question : "+question+". Options : "+optionsJson+". CorrectOption : "+correctOption);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_TOPIC, topic);
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_OPTIONS, optionsJson); //must be in JSON format
        values.put(COLUMN_CORRECT_OPTION, correctOption);

        long id = db.insert(TABLE, null, values);
//        db.close();
        return id;
    }


    public List<String> getAllSubjects() {
        List<String> subjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_SUBJECT + " FROM " + TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                subjects.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subjects;
    }

    public List<String> getTopicsBySubject(String subject) {
        List<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_TOPIC + " FROM " + TABLE + " WHERE " + COLUMN_SUBJECT + " = ?", new String[]{subject});

        if (cursor.moveToFirst()) {
            do {
                topics.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topics;
    }

    public ArrayList<HashMap<String, String>> getQuestionsBySubjectAndTopic(String subject, String topic, int limit, int offset) {
        ArrayList<HashMap<String, String>> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_QUESTION + ", " + COLUMN_OPTIONS + ", " + COLUMN_CORRECT_OPTION + " FROM " + TABLE + " WHERE " +
                        COLUMN_SUBJECT + " = ? AND " + COLUMN_TOPIC + " = ? LIMIT ? OFFSET ?",
                new String[]{subject, topic, String.valueOf(limit), String.valueOf(offset)});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> questionData = new HashMap<>();
                questionData.put("question", cursor.getString(0));
                questionData.put("options", cursor.getString(1));
                questionData.put("correctOption", cursor.getString(2));
                questions.add(questionData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questions;
    }

    public Cursor getAllQuizzes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }

    public int getTotalQuestionsBySubjectAndTopic(String subject, String topic) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE +
                        " WHERE " + COLUMN_SUBJECT + " = ? AND " + COLUMN_TOPIC + " = ?",
                new String[]{subject, topic});

        int totalQuestions = 0;
        if (cursor.moveToFirst()) {
            totalQuestions = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return totalQuestions;
    }

    public int getTotalPages(String subject, String topic, int pageSize) {
        int totalQuestions = getTotalQuestionsBySubjectAndTopic(subject, topic);
        return (int) Math.ceil((double) totalQuestions / pageSize);
    }




    public int updateQuiz(int id, String subject, String topic, String question, String optionsJson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_TOPIC, topic);
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_OPTIONS, optionsJson);

        return db.update(TABLE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteQuiz(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
