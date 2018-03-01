package com.hm.org.voicepay.core;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hm.org.voicepay.models.User;
import com.hm.org.voicepay.models.UserDao;

/**
 * Developer: efe.kocabas
 * Date: 19/07/2017.
 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
