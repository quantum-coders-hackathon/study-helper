package com.coders.quantum.myapplication.ui.study_methods;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.model.Constant;
import com.coders.quantum.myapplication.model.sql_db.QuizzesSQLite;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddQuizActivity extends AppCompatActivity {


    private static String apiKey;
    Button btnSubmit;
    EditText etxtQuizTopicsDetails;
    TextView txtQuizzesDetails;
    private QuizzesSQLite quizzesSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_quiz);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        apiKey = Constant.getApiKey();
        btnSubmit = findViewById(R.id.btnQuizAdd);
        etxtQuizTopicsDetails = findViewById(R.id.etxtQuizTopicDetails);
        txtQuizzesDetails = findViewById(R.id.txtQuizzesDetails);

        quizzesSQLite = new QuizzesSQLite(getApplicationContext());

        btnSubmit.setOnClickListener(v->{
            String details = etxtQuizTopicsDetails.getText().toString();
            geminiReply(details);
        });

    }

    private void geminiReply(String details)
    {
        // Specify a Gemini model appropriate for your use case
        GenerativeModel gm =
                new GenerativeModel(
                        /* modelName */ "gemini-1.5-flash",
                        // Access your API key as a Build Configuration variable (see "Set up your API key"
                        // above)
                        /* apiKey */ apiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content =
                new Content.Builder().addText(details + "\n" +
//                                "Write maximum of 10 questions and at least 1 question. Questions wrapped with <question> tag\n" +
//                                "Write the subject and topic name using <subject> and <topic> respectively at the beginning.\n" +
//                                "Each questions will have maximum of 5 options and at least 3 options and use select option tag.\n" +
//
//                                "The response format must be " +
//                                "<h2><subject>Subject Name</subject></h2>\n" +
//                                "<h3><topic>Topic Name</topic></h3>\n" +
//                                "<h4><question>question 1</question></h4>\n" +
//                                "<select><option>Option 1</option><option>Option 2</option><option>Option 3</option><option>Option 4</option></select>\n" +
//                                "<h4><question>question 2</question></h4>\n" +
//                                "<select><option>Option 1</option><option>Option 2</option><option>Option 3</option><option>Option 4</option></select>\n" +
//                                ".......\n" +
//
//                                "Ensure all content is wrapped in HTML (in-body-style-is-allowed) tags appropriate for the body tag.\n" +
//                                "Avoid using any code blocks, <style>, <!DOCTYPE html>, or JavaScript elements in your response.")
                                "Please write a maximum of 10 questions and a minimum of 1 question. Each question must be wrapped in <question> tags.\n" +
                                "At the beginning, write the subject and topic name using <subject> and <topic> tags, respectively.\n" +
                                "Each question must have a maximum of 5 options and a minimum of 3 options, wrapped in <select> and <option> tags.\n" +

                                "The response format must be:\n" +
                                "<h2><subject>Subject Name</subject></h2>\n" +
                                "<h3><topic>Topic Name</topic></h3>\n" +
                                "<h4><question>Question 1</question></h4>\n" +
                                "<select><option>Option 1</option><br/><option>Option 2</option><br/><option>Option 3</option><br/><option>Option 4</option><br/></select>\n" +
                                "<correct_option>Option x (whichever is correct)</correct_option>\n" +
                                "<h4><question>Question 2</question></h4>\n" +
                                "<select><option>Option 1</option><br/><option>Option 2</option><br/><option>Option 3</option><br/><option>Option 4</option><br/></select>\n" +
                                "<correct_option>Option x (whichever is correct)</correct_option>\n" +
                                "...\n" +

                                "Ensure that all content is wrapped in appropriate HTML tags (body-style tags allowed).\n" +
                                "Avoid using any code blocks, <style>, <!DOCTYPE html>, or JavaScript elements in your response.")
                        .build();

        // For illustrative purposes only. You should use an executor that fits your needs.
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String resultText = result.getText();
                        runOnUiThread(() -> {
                            txtQuizzesDetails.setText(Html.fromHtml(resultText, Html.FROM_HTML_MODE_COMPACT));
                            Log.d("quizzes_html",resultText);

                            SQLiteDatabase db = quizzesSQLite.getWritableDatabase();
                            db.beginTransaction();
                            try {
                                List<QuestionData> questions = parseQuestions(resultText);

                                for (QuestionData question : questions) {
                                    quizzesSQLite.insertQuiz(question.getSubject(), question.getTopic(), question.getQuestion(), question.getOptionsJson(), question.getCorrectOption());
                                }
                                db.setTransactionSuccessful();
                            } catch (Exception e) {
                                Log.e("error", "Error processing questions: " + e.getMessage());
                                Toast.makeText(AddQuizActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                db.endTransaction();
                            } finally {
                                db.endTransaction();
                                db.close();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        Log.e("throwable_error",t.toString());
                    }
                },
                executor);
    }
    private List<QuestionData> parseQuestions(String html) {
        List<QuestionData> questionList = new ArrayList<>();

        String subject = extractTagContent(html, "subject");
        String topic = extractTagContent(html, "topic");

        String[] questions = extractQuestions(html);

        for (String questionHtml : questions) {
            String questionText = extractTagContent(questionHtml, "question");
            String[] options = extractOptions(questionHtml);
            String correctOption = extractTagContent(questionHtml, "correct_option");

            JSONArray optionsJson = new JSONArray();
            for (String option : options) {
                optionsJson.put(option);
            }

            questionList.add(new QuestionData(subject, topic, questionText, optionsJson.toString(), correctOption));
        }

        return questionList;
    }

    private String[] extractQuestions(String html) {
        List<String> questionBlocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("<h4><question>(.*?)</question></h4>(.*?)<correct_option>(.*?)</correct_option>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String questionBlock = matcher.group(0); // Full block including question and correct option
            questionBlocks.add(questionBlock);
        }

        return questionBlocks.toArray(new String[0]);
    }

    private String[] extractOptions(String questionHtml) {
        List<String> optionsList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<option>(.*?)</option>");
        Matcher matcher = pattern.matcher(questionHtml);

        while (matcher.find()) {
            optionsList.add(matcher.group(1)); // Add the option text
        }

        return optionsList.toArray(new String[0]);
    }

    // Helper methods to extract content from the HTML string
    private String extractTagContent(String html, String tag) {
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int start = html.indexOf(startTag) + startTag.length();
        int end = html.indexOf(endTag);
        return html.substring(start, end).trim();
    }

    class QuestionData {
        private String subject;
        private String topic;
        private String question;
        private String optionsJson;
        private String correctOption;

        public QuestionData(String subject, String topic, String question, String optionsJson, String correctOption) {
            this.subject = subject;
            this.topic = topic;
            this.question = question;
            this.optionsJson = optionsJson;
            this.correctOption = correctOption;
        }

        public String getSubject() {
            return subject;
        }

        public String getTopic() {
            return topic;
        }

        public String getQuestion() {
            return question;
        }

        public String getOptionsJson() {
            return optionsJson;
        }

        public String getCorrectOption() {
            return correctOption;
        }
    }
}