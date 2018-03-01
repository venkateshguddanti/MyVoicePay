package com.hm.org.voicepay.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Developer: efe.kocabas
 * Date: 19/07/2017.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM User ORDER BY amount DESC")
    List<User> getAll();

    @Query("SELECT COUNT(*) from User")
    int countPlayers();

    @Insert
    long insert(User player);

    @Delete
    void delete(User player);

    @Query("UPDATE User set name = :name, avatar_name = :avatarName WHERE id = :id")
    int updatePlayerNameAndAvatar(int id, String name, String avatarName);

    @Query("UPDATE User set amount = :amount WHERE id = :id")
    int updatePlayerAmount(int id, int amount);

    @Query("DELETE FROM User")
    public void nukeTable();
}
