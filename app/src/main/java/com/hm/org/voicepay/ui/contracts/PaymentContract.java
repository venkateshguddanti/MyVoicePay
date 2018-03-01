package com.hm.org.voicepay.ui.contracts;

import com.hm.org.voicepay.models.User;
import com.hm.org.voicepay.ui.common.BasePresenter;

import java.util.ArrayList;

/**
 * Developer: efe.kocabas
 * Date: 18/07/2017.
 */

public interface PaymentContract {

    interface View {

        void finishPayment(int speetchText,Object... args);
        void setSpeechText(int speechText);
        void setSpeechText(int speechText ,Object... args);
        void setList(ArrayList<User> users);
    }

    interface Presenter extends BasePresenter {

    }
}
