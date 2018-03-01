package com.hm.org.voicepay.core;

import javax.inject.Singleton;

import dagger.Component;
import com.hm.org.voicepay.ui.activities.PaymentActivity;

/**
 * Developer: efe.kocabas
 * Date: 18/07/2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {


    void inject(PaymentActivity activity);

}
