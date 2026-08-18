package com.example.itrproject_001;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itrproject_001.network.GeminiApi;

public class AIAssistantActivity extends AppCompatActivity {

    // XML components
    EditText etQuestion;
    Button btnSend;

    LinearLayout chatContainer;
    ScrollView chatScrollView;

    // Gemini API object
    GeminiApi geminiApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect XML layout
        setContentView(R.layout.activity_ai_assistant);

        // Find views
        etQuestion = findViewById(R.id.etQuestion);

        btnSend = findViewById(R.id.btnSend);

        chatContainer = findViewById(
                R.id.chatContainer
        );

        chatScrollView = findViewById(
                R.id.chatScrollView
        );

        // Create Gemini API object
        geminiApi = new GeminiApi();

        // SEND BUTTON
        btnSend.setOnClickListener(v -> sendQuestion());
    }


    // ==========================================
    // SEND QUESTION TO GEMINI
    // ==========================================

    private void sendQuestion() {

        // Get question from EditText
        String question = etQuestion
                .getText()
                .toString()
                .trim();


        // Check empty question
        if (question.isEmpty()) {

            etQuestion.setError(
                    "Ask something"
            );

            etQuestion.requestFocus();

            return;
        }


        // Display user's question
        addMessage(
                "You: " + question
        );


        // Disable button while AI is responding
        btnSend.setEnabled(false);


        // Show loading message
        TextView loadingMessage =
                addMessage(
                        "AI Assistant: Thinking..."
                );


        // ==========================================
        // CALL GEMINI API
        // ==========================================

        geminiApi.askQuestion(
                question,
                new GeminiApi.GeminiCallback() {

                    @Override
                    public void onSuccess(
                            String answer) {

                        // Replace Thinking...
                        // with actual AI answer
                        loadingMessage.setText(
                                "AI Assistant: " + answer
                        );


                        // Enable Send button
                        btnSend.setEnabled(true);


                        // Clear EditText
                        etQuestion.setText("");


                        // Scroll down
                        scrollToBottom();
                    }


                    @Override
                    public void onError(
                            String error) {

                        // Show error
                        loadingMessage.setText(
                                "AI Assistant: " + error
                        );


                        // Enable button again
                        btnSend.setEnabled(true);


                        // Scroll down
                        scrollToBottom();
                    }
                }
        );
    }


    // ==========================================
    // ADD MESSAGE TO CHAT
    // ==========================================

    private TextView addMessage(
            String message) {

        TextView textView =
                new TextView(this);


        // Set message
        textView.setText(message);


        // Text size
        textView.setTextSize(16);


        // Text color
        textView.setTextColor(
                getResources().getColor(
                        android.R.color.black
                )
        );


        // Padding
        textView.setPadding(
                15,
                15,
                15,
                15
        );


        // Layout parameters
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        // Space between messages
        params.setMargins(
                0,
                8,
                0,
                8
        );


        textView.setLayoutParams(params);


        // Add message to chat
        chatContainer.addView(
                textView
        );


        // Scroll to latest message
        scrollToBottom();


        return textView;
    }


    // ==========================================
    // SCROLL CHAT TO BOTTOM
    // ==========================================

    private void scrollToBottom() {

        chatScrollView.post(() ->
                chatScrollView.fullScroll(
                        ScrollView.FOCUS_DOWN
                )
        );
    }
}