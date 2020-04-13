package com.musify.controller;

import android.content.Context;

import com.musify.model.MusifyDatabase;
import com.musify.model.entity.User;

public class MainController {
    private MusifyDatabase db;

    public MainController(Context context){
        db = MusifyDatabase.getDatabase(context);
    }

    /**
     * Method to handle the login request
     * @param userName the name of the user in the app
     * @param password the password of the user in the app
     * @return User if the user in the database matches with the parameters, null if not
     */
    public User onLogin(String userName, String password){
        User user = db.userDao().getUserByName(userName);
        if (user != null) {
            if (!user.password.equals(password)){
                user = null;
            }
        }
        return user;
    }

    /**
     * Method to handle the register request
     * @param userName the name of the user to create in the app
     * @param password the password of the user to create in the app
     * @return User if the user does not exist in the database, null if not
     */
    public User onRegister(String userName, String password){
        User user = db.userDao().getUserByName(userName);
        if (user == null) {
            User newUser = new User(userName, password);
            db.userDao().insert(newUser);
            user = newUser;
        }
        else {
            user = null;
        }
        return user;
    }

    /**
     * Method to check if the user and password inputs are filled
     * @param userName the user introduced in the input
     * @param password the password introduced in the input
     * @return true if userName and password are not empty, otherwise false
     */
    public boolean handleLoginRegisterButtons(String userName, String password){
        return (!userName.isEmpty() && !password.isEmpty()) ? true : false;
    }
}
