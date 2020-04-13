package com.musify.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.musify.model.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE user_name = :userName")
    User getUserByName(String userName);
    @Insert
    void insert(User user);
    @Delete
    void delete(User user);
}
