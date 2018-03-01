package com.hm.org.voicepay.managers;

import java.util.List;

import io.reactivex.Observable;

import com.hm.org.voicepay.models.User;

/**
 * Author: efe.cem.kocabas
 * Date: 05/12/2016.
 */

public interface DBManager {

    void insertUser(List<User>userList);

    Observable<List<User>> getUsers();

    void updateUserAmount(User user);

    void deleteTable();
}
