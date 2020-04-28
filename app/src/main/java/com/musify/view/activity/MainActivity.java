package com.musify.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.musify.R;
import com.musify.model.MusifyAPIRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private MusifyAPIRequestQueue requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requests = MusifyAPIRequestQueue.getInstance(this);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        EditText userInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        loginButton.setOnClickListener((v) -> {
            try {
                JSONObject login_body = new JSONObject();
                login_body.put("user_name", userInput.getText().toString());
                login_body.put("password", passwordInput.getText().toString());
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_host) + "/login/", login_body, response ->
                {
                    Intent i = new Intent(this, ChatActivity.class);
                    i.putExtra("username", userInput.getText().toString());
                    startActivity(i);
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
                });
                requests.addToRequestQueue(loginRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        registerButton.setOnClickListener((v) -> {
            try {
                JSONObject login_body = new JSONObject();
                login_body.put("user_name", userInput.getText().toString());
                login_body.put("password", passwordInput.getText().toString());
                JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_host) + "/register/", login_body, response ->
                {
                    Intent i = new Intent(this, ChatActivity.class);
                    i.putExtra("username", userInput.getText().toString());
                    startActivity(i);
                }, error ->
                {
                    if (error.networkResponse.statusCode == 400) {
                        String errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast registerError = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        registerError.show();
                    }
                });
                requests.addToRequestQueue(registerRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!userInput.getText().toString().isEmpty() && !passwordInput.getText().toString().isEmpty()){
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                }
                else {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        userInput.addTextChangedListener(inputWatcher);
        passwordInput.addTextChangedListener(inputWatcher);
    }
}
