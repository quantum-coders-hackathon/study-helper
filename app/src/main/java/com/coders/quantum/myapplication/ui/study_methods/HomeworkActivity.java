package com.coders.quantum.myapplication.ui.study_methods;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.model.Constant;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeworkActivity extends AppCompatActivity {

    private static String apiKey;
    Button btnSubmit;
    EditText etxtHomeworkDetails;
    TextView txtAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homework);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        apiKey = Constant.getApiKey();
        btnSubmit = findViewById(R.id.btnHomeworkSubmit);
        etxtHomeworkDetails = findViewById(R.id.etxtHomeworkDetails);
        txtAnswer = findViewById(R.id.txtHomeworkAnswer);

        btnSubmit.setOnClickListener(v->{
            String details = etxtHomeworkDetails.getText().toString();
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
                        "List the theories necessary to solve the problem (don't provide solution) using bullet points.\n" +
                        "For each theory, include a one-line description describing its relevance to the specific problem.\n" +
                        "Include Wikipedia links related to each theory.\n" +
                        "Ensure all content is wrapped in HTML (in-body-style-is-allowed) tags appropriate for the body tag.\n" +
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
                            txtAnswer.setText(Html.fromHtml(resultText, Html.FROM_HTML_MODE_COMPACT));
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
}