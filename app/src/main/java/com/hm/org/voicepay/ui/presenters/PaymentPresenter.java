package com.hm.org.voicepay.ui.presenters;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.Nullable;
import com.hm.org.voicepay.R;
import com.hm.org.voicepay.core.AppDefine;
import com.hm.org.voicepay.managers.DBManager;
import com.hm.org.voicepay.managers.SchedulersManager;
import com.hm.org.voicepay.managers.VoiceManager;
import com.hm.org.voicepay.managers.VoiceManagerImpl;
import com.hm.org.voicepay.models.User;
import com.hm.org.voicepay.ui.contracts.PaymentContract;

import static com.hm.org.voicepay.core.AppDefine.MAXIMUM_POINT_FOR_MATCH;
import static com.hm.org.voicepay.core.AppDefine.MAXIMUM_POINT_FOR_SET;
import static com.hm.org.voicepay.core.AppDefine.REQUEST_CODE_END_PAYMENT;
import static com.hm.org.voicepay.core.AppDefine.REQUEST_CODE_MULTIPLE_USERS;
import static com.hm.org.voicepay.core.AppDefine.REQUEST_CODE_PAYMENT_AMOUNT;
import static com.hm.org.voicepay.core.AppDefine.REQUEST_CODE_SPEAK;
import static com.hm.org.voicepay.core.AppDefine.REQUEST_CODE_WHOME_TO_PAY;

/**
 * Developer: efe.kocabas
 * Date: 18/07/2017.
 */

public class PaymentPresenter implements PaymentContract.Presenter, VoiceManagerImpl.VoiceManagerListener {

    private final PaymentContract.View view;
    private final VoiceManager voiceManager;
    private final SchedulersManager schedulersManager;
    private final DBManager dbManager;


    private List<User> users = new ArrayList<>();
    private String selectedUser = "";


    public PaymentPresenter(PaymentContract.View view,
                            VoiceManager voiceManager,
                            SchedulersManager schedulersManager,
                            DBManager dbManager) {

        this.view = view;
        this.voiceManager = voiceManager;
        voiceManager.setVoiceManagerListener(this);
        this.schedulersManager = schedulersManager;
        this.dbManager = dbManager;
    }

    @Override
    public void onCreate() {

        dbManager.getUsers()
                .subscribeOn(schedulersManager.background())
                .observeOn(schedulersManager.ui())
                .subscribe(users -> {

                    this.users = users;
                    voiceManager.speak(REQUEST_CODE_WHOME_TO_PAY, R.string.speech_select_user);
                    view.setSpeechText(R.string.speech_select_user);
                });
    }

    @Override
    public void onFinish() {
        voiceManager.shutdown();
    }


    @Nullable
    private void findBestMatchResults(ArrayList<String> results, int requestCode) {


        int bestScore = 2;
        String name = "";
        boolean multipleEntries = false;
        User bestMatchedPlayer = null;
        ArrayList<User> players = new ArrayList<>();
        User p = new User();
        p.setAvatarName("Venky E");
        p.setName("Venky E");
        p.setAmount(100);
        p.setPronunciations("venky E");
        User p1 = new User();
        p1.setAvatarName("Venky G");
        p1.setName("Venky G");
        p1.setAmount(100);
        p1.setPronunciations("venky G");
        User p2 = new User();
        p2.setAvatarName("Venky");
        p2.setName("Venky");
        p2.setAmount(100);
        p2.setPronunciations("venky");
        players.add(p);
        players.add(p1);
        players.add(p2);

        int totalScore = getNameMatchCount(results, players);
        name = getAccurateNameFromSpeech(results,players);
        for (User player : players) {

            ArrayList<String> pronunciations = new ArrayList<>(Arrays.asList(player.getPronunciations().split(",")));
            pronunciations.add(player.getName());

            if (totalScore > bestScore && requestCode != REQUEST_CODE_MULTIPLE_USERS) {

               multipleEntries = true;
               name = getResultNameFromSpeech(results,players);
            }
            if(!name.equalsIgnoreCase(""))
            {
                break;
            }
        }

        if(requestCode == REQUEST_CODE_PAYMENT_AMOUNT)
        {
            bestScore = getAmountFromResult(results);
        }

        switch (requestCode) {
            case REQUEST_CODE_WHOME_TO_PAY:

                if(multipleEntries) {
                    view.setList(players);
                    view.setSpeechText(R.string.speech_multiple_users,name);
                    voiceManager.speak(REQUEST_CODE_MULTIPLE_USERS, R.string.speech_multiple_users,name);
                }
                else if(name.equalsIgnoreCase(""))
                {
                    view.setList(new ArrayList<User>());
                    view.setSpeechText(R.string.speech_select_user);
                    voiceManager.speak(REQUEST_CODE_WHOME_TO_PAY,R.string.speech_no_matches);

                }else
                {
                    view.setList(new ArrayList<User>());
                    selectedUser = name;
                    view.setSpeechText(R.string.speech_payment_amount,name);
                    voiceManager.speak(REQUEST_CODE_PAYMENT_AMOUNT,R.string.speech_payment_amount,name);
                }
                break;
            case REQUEST_CODE_MULTIPLE_USERS:

                if(name.equalsIgnoreCase(""))
                {
                    view.setList(new ArrayList<User>());
                    view.setSpeechText(R.string.speech_select_user);
                    voiceManager.speak(REQUEST_CODE_WHOME_TO_PAY,R.string.speech_no_matches);

                }else
                {
                    view.setList(new ArrayList<User>());
                    view.setSpeechText(R.string.speech_payment_amount,name);
                    selectedUser = name;
                    voiceManager.speak(REQUEST_CODE_PAYMENT_AMOUNT,R.string.speech_payment_amount,name);
                }
                break;
            case REQUEST_CODE_PAYMENT_AMOUNT:
                view.setSpeechText(R.string.txt_empty);
                voiceManager.speak(REQUEST_CODE_END_PAYMENT,R.string.speech_payment_success,selectedUser,bestScore);
                view.finishPayment(R.string.speech_payment_success,selectedUser,bestScore);
                break;
        }

    }



    private int getNameMatchCount(ArrayList<String> results, ArrayList<User> pronunciations) {


        int totalScore = 0;
        for (String result : results) {

            result = result.toLowerCase();

            for (User user : pronunciations) {

                if (user.getName().toLowerCase().contains(result.toLowerCase())) {

                    totalScore = totalScore+1;
                }
            }
        }

        Log.i("VOICE", String.format("%s: %d - %s", pronunciations, totalScore, results));
        return totalScore;
    }
    private int getAmountFromResult(ArrayList<String> results) {


        int result = 0;
        for (String res : results) {

            try {
                result = Integer.parseInt(res);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }
    private String getResultNameFromSpeech(ArrayList<String> results, ArrayList<User> pronunciations) {


        String name = "";
        for (String result : results) {

            result = result.toLowerCase();

            for (User mUser : pronunciations) {

                if (mUser.getName().toLowerCase().contains(result)) {

                    name = result;
                    break;
                }
            }
        }

        return name;
    }
    private String getAccurateNameFromSpeech(ArrayList<String> results, ArrayList<User> pronunciations) {


        String name = "";
        for (String result : results) {

            result = result.toLowerCase();

            for (User mUser : pronunciations) {

                if (mUser.getName().toLowerCase().equalsIgnoreCase(result)) {

                    name = result;
                    break;
                }
            }
        }

        return name;
    }



//    private boolean containsAnyKeyword(ArrayList<String> results, String[] keywords) {
//
//        for (String word : results) {
//
//            for (String keyword : keywords) {
//
//                if (word.contains(keyword)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    @Override
    public void onResults(ArrayList<String> results, int requestCode) {

        if ((requestCode == REQUEST_CODE_WHOME_TO_PAY) ||
                (requestCode == REQUEST_CODE_MULTIPLE_USERS) || (requestCode == REQUEST_CODE_PAYMENT_AMOUNT)) {

            findBestMatchResults(results, requestCode);
        }

    }

    @Override
    public void onSpeechCompleted(int requestCode) {

        if ((requestCode == REQUEST_CODE_WHOME_TO_PAY) ||
                (requestCode == REQUEST_CODE_MULTIPLE_USERS) || (requestCode == REQUEST_CODE_PAYMENT_AMOUNT)) {

            voiceManager.listen(requestCode);
        }
    }


}
