package com.hm.org.voicepay.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.org.voicepay.models.User;
import com.hm.org.voicepay.ui.adapters.RecyclerAdapter;
import com.hm.org.voicepay.ui.listener.RecyclerViewClickInterface;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hm.org.voicepay.R;
import com.hm.org.voicepay.core.AppDefine;
import com.hm.org.voicepay.core.PPApplication;
import com.hm.org.voicepay.custom.CircleTransform;
import com.hm.org.voicepay.managers.DBManager;
import com.hm.org.voicepay.managers.SchedulersManager;
import com.hm.org.voicepay.managers.VoiceManager;
import com.hm.org.voicepay.managers.VoiceManagerImpl;
import com.hm.org.voicepay.ui.contracts.PaymentContract;
import com.hm.org.voicepay.ui.presenters.PaymentPresenter;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements PaymentContract.View,RecyclerViewClickInterface<User> {


    @Inject
    DBManager dbManager;

    @BindView(R.id.txt_assitance)
    TextView myTxt;
    @Inject
    SchedulersManager schedulersManager;
    private PaymentPresenter presenter;
    private VoiceManager voiceManager;

    @BindView(R.id.myListView)
    RecyclerView mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        PPApplication.getAppComponent().inject(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mUserList.setLayoutManager(manager);


        voiceManager = new VoiceManagerImpl(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    100);

        }else {
            presenter = new PaymentPresenter(this, voiceManager, schedulersManager, dbManager);
            presenter.onCreate();
        }

    }



    @Override
    public void setSpeechText(int speechText) {

        myTxt.setText(speechText);
    }

    @Override
    public void setSpeechText(int speechText, Object... args) {
        myTxt.setText(getString(speechText,args));
    }

    @Override
    public void setList(ArrayList<User> users) {

        RecyclerAdapter<User> adapter = new RecyclerAdapter<User>(this,users,R.layout.list_item,this);
        mUserList.setAdapter(adapter);
    }


    @Override
    public void finishPayment(int speetchText, Object... args) {

        new AlertDialog.Builder(this)
                .setTitle("CONFIRMATION?")
                .setMessage(getString(speetchText,args))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();
                    }
                })

                .show();
    }



    private void setAvatar(ImageView imageView, String filename) {

        String url = AppDefine.BASE_URL + filename;
        Picasso.with(this).load(url)
                .transform(new CircleTransform())
                .into(imageView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(presenter != null)
        presenter.onFinish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == 100)
       {
           if (grantResults.length > 0
                   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               // permission was granted, yay! Do the
               // contacts-related task you need to do.
               presenter = new PaymentPresenter(this, voiceManager, schedulersManager, dbManager);
               presenter.onCreate();

           } else {

               Toast.makeText(this, "Please allow permission to pay using voice command", Toast.LENGTH_SHORT).show();

           }
       }
    }

    @Override
    public void onClickItem(User item) {

    }
}
