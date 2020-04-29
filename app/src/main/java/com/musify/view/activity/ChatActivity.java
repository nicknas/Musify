package com.musify.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.musify.R;
import com.musify.model.MusifyAPIRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class ChatActivity extends AppCompatActivity {
    private String user;
    private MusifyAPIRequestQueue requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user = getIntent().getStringExtra("username");
        requests = MusifyAPIRequestQueue.getInstance(this);
        FloatingActionButton sendMessageButton = findViewById(R.id.send_message_button);
        TextView chatText = findViewById(R.id.chat_text);
        EditText userMessageInput = findViewById(R.id.user_message_input);
        TextView titulo = findViewById(R.id.chatbot_title);

        sendMessageButton.setOnClickListener((view) -> {
            if (!userMessageInput.getText().toString().isEmpty()){
                SpannableString userText = new SpannableString("You: " + userMessageInput.getText().toString() + "\n\n");
                userText.setSpan(new ForegroundColorSpan(Color.CYAN), 0, userText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                chatText.append(userText);
                JSONObject requestSongBody = new JSONObject();
                try {
                    requestSongBody.put("user_input", userMessageInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest requestSongsRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_host) + "/" + user + "/request_songs", requestSongBody, response ->
                {
                    try {
                        SpannableString chatbotText = new SpannableString("Chatbot: " + response.getString("response") + "\n\n");
                        chatbotText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, chatbotText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        chatText.append(chatbotText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    titulo.setText(R.string.chatbot);
                }, error ->
                {
                    if (error.networkResponse.statusCode == 400) {
                        String errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast loginError = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        loginError.show();
                    }
                    titulo.setText(R.string.chatbot);
                });
                titulo.setText(getString(R.string.chatbot) + " está contestando");
                requests.addToRequestQueue(requestSongsRequest);
                userMessageInput.setText("");
            }
        });
    }
}
