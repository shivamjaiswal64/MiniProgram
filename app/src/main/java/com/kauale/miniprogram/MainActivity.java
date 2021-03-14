package com.kauale.miniprogram;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import  android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
        initUI();
        setClickListener();
        setDataFromClipBoard();
    }

    private void setDataFromClipBoard() {
        //Clipboard snippet // Ref: https://developer.android.com/guide/topics/text/copy-paste#ClipboardClasses
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        try{
            pasteData =(String) item.getText();
            editText.setText(pasteData);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void setClickListener() {
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btnContact.setOnClickListener(this);
    }

    private void initUI() {
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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        tempText.setText(null);
        switch (v.getId()){
            case R.id.btn1:
                textView.setText(R.string.a_clicked);
                break;
            case R.id.btn2:
                textView.setText(R.string.b_clicked);
                showNotification(this, this.getIntent());
                break;
            case R.id.btn3:
                textView.setText(R.string.c_clicked);
                Intent notificationIntent = new Intent(MainActivity. this, MainActivity. class ) ;
                notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
                notificationIntent.setAction(Intent. ACTION_MAIN ) ;
                notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP |Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                PendingIntent resultIntent =PendingIntent. getActivity (MainActivity. this, 0 , notificationIntent , 0 ) ;
                Builder mBuilder = new Builder(MainActivity. this, default_notification_channel_id )
                        .setSmallIcon(R.mipmap.ic_launcher )
                        .setContentTitle( "C pressed Push notif." )
                        .setContentIntent(resultIntent)
                        .setStyle( new InboxStyle())
                        .setContentText( "Hello! U clicked C" ) ;
                final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
                if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
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
                break;
            case R.id.search:
                String url = "http://www.google.com/search?q=KauaLe";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.button5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }else requestPermission();
                }
                break;// Ref: https://stackoverflow.com/questions/13977245
            case R.id.btnContact:
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA},CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Unable to launch camera permission denied.", Toast.LENGTH_SHORT).show();
                }
            }else {
                // Camera has data for the image you captured. TODO from data
            }
        }
    }

    public void showNotification(Context context, Intent intent) {
        if (intent != null) {
            //uniqueRequestID
            int requestID = (int) System.currentTimeMillis();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "oreo_notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                notification = new Notification.Builder(context)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("Mini Program")
                        .setContentTitle("Title of miniprogram")
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setContentIntent(contentIntent)
                        .setStyle(new Notification.InboxStyle())
                        .setContentInfo("Info")
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true)
                        .build();

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(mChannel);
                }
            } else {
                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("Mini Program")
                        .setContentTitle("Title of mini program")
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setContentIntent(contentIntent)
                        .setStyle(new InboxStyle())
                        .setAutoCancel(true)
                        .setContentInfo("Info");
            }
            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.notify(requestID, notification);
                } else {
                    notificationManager.notify(requestID, builder.build());
                }
            }
        }
    }


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