package com.hm.org.voicepay.managers;

import java.util.List;

import io.reactivex.Observable;
import com.hm.org.voicepay.core.AppDatabase;
import com.hm.org.voicepay.models.User;

/**
 * Author: efe.cem.kocabas
 * Date: 05/12/2016.
 */

public class DBManagerImpl implements DBManager {

    private AppDatabase db;

    public DBManagerImpl(AppDatabase db) {

        this.db = db;
    }



    @Override
    public void insertUser(List<User> userList) {
        for (User player : userList) {

            int affectedRows = db.userDao().updatePlayerNameAndAvatar(player.getId(), player.getName(), player.getAvatarName());
            if(affectedRows == 0) {
                db.userDao().insert(player);
            }
        }

    }

    @Override
    public Observable<List<User>> getUsers() {
        return Observable.create(subscriber -> subscriber.onNext(db.userDao().getAll()));
    }

    @Override
    public void updateUserAmount(User user) {
        db.userDao().updatePlayerAmount(user.getId(), user.getAmount());

    }


    @Override
    public void deleteTable() {

        db.userDao().nukeTable();
    }
}
