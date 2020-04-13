package com.musify.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.musify.model.dao.UserDao;
import com.musify.model.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class MusifyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    private static MusifyDatabase db = null;
    public static MusifyDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    MusifyDatabase.class, "musify").build();
        }
        return db;
    }
}
