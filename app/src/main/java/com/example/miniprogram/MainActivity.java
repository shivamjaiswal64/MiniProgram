package com.example.miniprogram;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonA;
    Button buttonB;
    Button buttonC;
    TextView textView,tempText;
    EditText editText;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonA = (Button) findViewById(R.id.btn1);
        buttonB = (Button) findViewById(R.id.btn2);
        buttonC = (Button) findViewById(R.id.btn3);
        textView = (TextView) findViewById(R.id.txt);
        tempText = (TextView) findViewById(R.id.tempTxt);
        editText = (EditText) findViewById(R.id.editTxt);
        buttonA.setOnClickListener(this);
        buttonB.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        //Clipboard snippet //https://developer.android.com/guide/topics/text/copy-paste#ClipboardClasses
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        try{
        String pasteData =(String) item.getText();
        editText.setText(pasteData);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onClick(View v) {
        tempText.setText(null);
        if(v.getId()==R.id.btn1){
            textView.setText("A is clicked");
        }
        if (v.getId()==R.id.btn2){
            textView.setText("B is clicked");
        }
        if (v.getId()==R.id.btn3){
            textView.setText("C is CLicked");
            Intent notificationIntent = new Intent(MainActivity. this, MainActivity. class ) ;
            notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
            notificationIntent.setAction(Intent. ACTION_MAIN ) ;
            notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP |Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            PendingIntent resultIntent =PendingIntent. getActivity (MainActivity. this, 0 , notificationIntent , 0 ) ;
            Builder mBuilder = new Builder(MainActivity. this, default_notification_channel_id )
                    .setSmallIcon(R.drawable. ic_launcher_foreground )
                    .setContentTitle( "C pressed Push notif." )
                    .setContentIntent(resultIntent)
                    .setStyle( new InboxStyle())
                    .setContentText( "Hello! U clicked C" ) ;
            final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new
                        NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel) ;
            }
            final int notificationId = ( int ) System. currentTimeMillis () ;
            assert mNotificationManager != null;
            mNotificationManager.notify(notificationId , mBuilder.build()) ;
            Handler h = new Handler() ;
            long delayInMilliseconds = 10000 ;
            h.postDelayed( new Runnable() {
                public void run () {
                    mNotificationManager .cancel( notificationId ) ;
                }
            } , delayInMilliseconds) ;
        }




    }


}