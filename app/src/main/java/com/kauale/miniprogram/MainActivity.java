package com.kauale.miniprogram;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import  android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.core.app.NotificationCompat.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnA,btnB,btnC,btn5,btnContact,btn7;
    Button buttonSearch;
    TextView textView,tempText;
    EditText editText;
    String pasteData;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private Toast toast;
    private long lastBackPressTime = 0;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=1337;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnA = (Button) findViewById(R.id.btn1);
        btnB = (Button) findViewById(R.id.btn2);
        btnC = (Button) findViewById(R.id.btn3);
        textView = (TextView) findViewById(R.id.txt);
        tempText = (TextView) findViewById(R.id.tempTxt);
        editText = (EditText) findViewById(R.id.editTxt);
        buttonSearch = (Button) findViewById(R.id.search);
        btn5 = (Button) findViewById(R.id.button5) ;
        btnContact = (Button)findViewById(R.id.btnContact);
        btn7 = (Button)findViewById(R.id.button7);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        //Clipboard snippet //https://developer.android.com/guide/topics/text/copy-paste#ClipboardClasses
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        try{
        pasteData =(String) item.getText();
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
        if(v.getId() == R.id.search){
            //String url = parseFun(pasteData);
            String url = "http://www.google.com/search?q=KauaLe";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        if(v.getId() == R.id.button5){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
           // startActivity(intent);
           // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } // Ref: https://stackoverflow.com/questions/13977245
        if(v.getId() == R.id.btnContact){
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        }



    }
    /*
    public String parseFun(String str){
        String regex = "\\s+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        str= matcher.replaceAll("+");
        str = "\"http://www.google.com/search?q="+str+"\""; // Error in parsing text into Uri
        return  str;
    }
    */

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", 4000);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    } // Ref: https://stackoverflow.com/a/21157420/13976917

}