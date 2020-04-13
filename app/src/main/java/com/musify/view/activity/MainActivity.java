package com.musify.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.musify.R;
import com.musify.controller.MainController;
import com.musify.model.entity.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private MainController mainController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainController = new MainController(getApplicationContext());
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        loginButton.setOnClickListener((v) -> {
            ExecutorService userExecutor = Executors.newSingleThreadExecutor();
            User user = null;
            try {
                user = userExecutor.submit(() -> mainController.onLogin(((EditText)findViewById(R.id.usernameInput)).getText().toString(),
                        ((EditText) findViewById(R.id.passwordInput)).getText().toString())).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (user == null) {
                Toast loginError = Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT);
                loginError.show();
            }
            else {
                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra("username", user.userName);
                startActivity(i);
            }
        });
        registerButton.setOnClickListener((v) -> {
            ExecutorService userExecutor = Executors.newSingleThreadExecutor();
            User user = null;
            try {
                user = userExecutor.submit(() -> mainController.onRegister(((EditText)findViewById(R.id.usernameInput)).getText().toString(),
                        ((EditText) findViewById(R.id.passwordInput)).getText().toString())).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (user == null) {
                Toast registerError = Toast.makeText(this, R.string.register_error, Toast.LENGTH_SHORT);
                registerError.show();
            }
            else {
                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra("username", user.userName);
                startActivity(i);
            }
        });
        EditText userInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mainController.handleLoginRegisterButtons(((EditText)findViewById(R.id.usernameInput)).getText().toString(),
                        ((EditText) findViewById(R.id.passwordInput)).getText().toString())){
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
