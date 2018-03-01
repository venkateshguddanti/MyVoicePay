package com.hm.org.voicepay.managers;

import java.util.List;

import io.reactivex.Observable;
import com.hm.org.voicepay.models.User;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Author: efe.cem.kocabas
 * Date: 29/11/2016.
 */

public interface RestManager {

    PingpongApi getPingpongApi();

    interface PingpongApi {

        @GET
        Observable<List<User>> getPlayers(@Url String url);
    }

}
