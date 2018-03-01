package com.hm.org.voicepay.core;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.hm.org.voicepay.managers.DBManager;
import com.hm.org.voicepay.managers.DBManagerImpl;
import com.hm.org.voicepay.managers.LogManager;
import com.hm.org.voicepay.managers.LogManagerImpl;
import com.hm.org.voicepay.managers.RestManager;
import com.hm.org.voicepay.managers.RestManagerImpl;
import com.hm.org.voicepay.managers.SchedulersManager;
import com.hm.org.voicepay.managers.SchedulersManagerImpl;

/**
 * Developer: efe.kocabas
 * Date: 18/07/2017.
 */

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {

        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    RestManager provideRestManager() {

        return new RestManagerImpl();
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase() {

        return Room.databaseBuilder(context, AppDatabase.class, "pingpongDb").build();
    }

    @Provides
    @Singleton
    DBManager provideDBManager(final AppDatabase db) {
        return new DBManagerImpl(db);
    }

    @Provides
    @Singleton
    SchedulersManager provideSchedulersManager() {
        return new SchedulersManagerImpl();
    }

    @Provides
    @Singleton
    LogManager provideLogManager() {

        return new LogManagerImpl();
    }
}
