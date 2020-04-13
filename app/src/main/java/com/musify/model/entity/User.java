package com.musify.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_name")
    public String userName;
    @ColumnInfo(name = "password")
    @NonNull
    public String password;
}
