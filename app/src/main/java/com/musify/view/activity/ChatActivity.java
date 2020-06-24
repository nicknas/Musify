package com.musify.view.activity;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.musify.R;
import com.musify.model.MusifyAPIRequestQueue;

import com.musify.model.message.Message;
import com.musify.view.message.MessageAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class ChatActivity extends AppCompatActivity {
    private String user;
    private MusifyAPIRequestQueue requests;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user = getIntent().getStringExtra("username");
        requests = MusifyAPIRequestQueue.getInstance(this);
        FloatingActionButton sendMessageButton = findViewById(R.id.send_message_button);
        EditText userMessageInput = findViewById(R.id.user_message_input);
        TextView titulo = findViewById(R.id.chatbot_title);
        ListView messagesView = findViewById(R.id.messages_view);
        messageAdapter = new MessageAdapter(this);
        messagesView.setAdapter(messageAdapter);
        sendMessageButton.setOnClickListener((view) -> {
            if (!userMessageInput.getText().toString().isEmpty()){
                messageAdapter.add(new Message(userMessageInput.getText().toString(), true));
                JSONObject requestSongBody = new JSONObject();
                try {
                    requestSongBody.put("user_input", userMessageInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest requestRecommendationsRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.api_host) + "/" + user + "/request_recommendations", null, response ->
                {
                    try {
                        JSONArray songs = response.getJSONArray("songs");
                        String chatbotResponse = "Estas son las canciones que te recomiendo:";
                        for (int i = 0; i < songs.length(); i++) {
                            chatbotResponse += "\n -" + songs.getJSONObject(i).getString("song") + " del artista " + songs.getJSONObject(i).getString("artist");
                        }
                        messageAdapter.add(new Message(chatbotResponse, false));

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
                JsonObjectRequest requestSongsRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_host) + "/" + user + "/request_songs", requestSongBody, response ->
                {
                    try {
                        messageAdapter.add(new Message(response.getString("response"), false));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    titulo.setText(R.string.chatbot);
                    try {
                        if (response.getString("intent").equals("Recoger canción") || response.getString("intent").equals("Recoger artista") || response.getString("intent").equals("Recoger album")) {
                            titulo.setText(getString(R.string.chatbot) + " está contestando");
                            requestRecommendationsRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            requests.addToRequestQueue(requestRecommendationsRequest);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                requestSongsRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requests.addToRequestQueue(requestSongsRequest);
                userMessageInput.setText("");
            }
        });
    }
}
