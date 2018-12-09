package com.aasana.caedu.ocrprognostic.controller.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.adapter.NotifyAdapter;
import com.aasana.caedu.ocrprognostic.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements NotifyAdapter.OnMesaggeSelectedListener {

    private static final String CHANNEL_ID = "chanel_01";
    @BindView(R.id.recycler_menssages)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;



    @BindView(R.id.view_empty)
    ViewGroup mEmptyView;;



    Query mQuery;
    private NotifyAdapter mAdapter;

    private FirebaseFirestore mFirestore;

    private String mNameDomain = null;
    private String mNameAirport = null;
    private final String TITLE_NOTIFICATION ="MENSAJE DE AASANA";
    private final String CONTEXT_NOTIFY = "MENSAJE DESDE FIREBASE";
    private ArrayList<String> dataFromFile = new ArrayList<String>();
    private Map<Integer,String[]> mapReadFile ;

    private String[] arrayReadFile;
    private String[] arrayAirportDestinatino;
    private NotificationManager mManager;
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readInternalFile();
        if (arrayReadFile.length>0)
        {
//            if (arrayReadFile.length>=3) {
                initializeNames();
//                initializeArrayDestination();
                setContentView(R.layout.activity_notification);
                ButterKnife.bind(this);

                setSupportActionBar(mToolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeButtonEnabled(true);
                }
//                setUpRecyclerView();
                initFirestore();
                initRecyclerView();
            /*}else{
                makeToast("Seleccione Aeropuertos de Destino");
                initIntentSelectAirport();
//                this.finish();
            }*/

        }

        else {
            makeToast("Vaya a Menu-> Escoger Destinos y seleccione sus aeropuertos de destino");
//            this.finish();
        }
    }

    private void initializeArrayDestination() {
        arrayAirportDestinatino = new String[arrayReadFile.length-2];
        int j =0;
        for (int i =2;i<arrayReadFile.length;i++){
            arrayAirportDestinatino[j] = arrayReadFile[i];
            Log.e("arrayAirportDestinatino",i +" :: "+arrayAirportDestinatino[j] + "  length =" +arrayReadFile[i]);
            j++;
        }

    }

    private void initIntentSelectAirport() {
        Intent nextScreen = new Intent(getApplicationContext(), SelectAirportActivity.class);
        startActivity(nextScreen);
    }

    private void initializeNames() {
        mNameAirport = arrayReadFile[0];
        mNameDomain = arrayReadFile[1];
        for (int i=0;i<arrayReadFile.length;i++){
            Log.e("NotifyarrayReadFile","<" +arrayReadFile[i]+">");
        }
    }
    private void makeToast(String stringToast){
        Toast.makeText(getApplicationContext(),stringToast,Toast.LENGTH_LONG).show();

    }

    private void setUpRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_menssages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void initFirestore() {
        Log.e("initFirestore","<"+mNameDomain+">");
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection(mNameDomain)
//                .orderBy("indice", Query.Direction.DESCENDING)
        ;
    }

    void readInternalFile(){
        try {
            FileInputStream fileIn = openFileInput(AeropuertoActivity.FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[100];
            int charRead;
            mapReadFile=new HashMap<Integer,String[]>();
            while ((charRead=InputRead.read(inputBuffer))>0) {
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                Log.e("notifiREadInternal",readstring);
                arrayReadFile = readstring.split(",");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   /* @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            Log.e("notifiOnstop",mAdapter.toString());
            mAdapter.stopListening();
        }
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        Log.e("notifionStart",mAdapter.toString());
        if (mAdapter!=null){
//            mAdapter.setQuery(mQuery);
            mAdapter.startListening();
        }

    }*/

   /* @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            Log.e("notifiOnPause",mAdapter.toString());

            mAdapter.stopListening();
        }
    }
*/
    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("nulll", "No query, not initializing RecyclerView");
        }
//        Log.e("initrecyclerView", "creater notification");
    //recorrerArrayDeSelects();
        mNotificationHelper = new NotificationHelper(this);
        mAdapter = new NotifyAdapter(mQuery,this,mNameAirport) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
//                    Log.e("Activity onDataChaned", "getItemCount es ceroo");

                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
//                    Log.e("Activity onDataChaned", "getItemCount is ::" +getItemCount());
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    sendChanel("AASANA APP","Tenes "+getItemCount()+ " Mensajes Recividos");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("onError", e.getMessage());
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            protected void onModified() {
                super.onModified();
                Log.e("documenmodified","notifiactionActivityupdate");
            }

            @Override
            protected void onDelete() {
                super.onDelete();
                Log.e("documenDelete","notifiactionActivityremove");

            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setQuery(mQuery);
    }

    private void recorrerArrayDeSelects() {
        for (int i =0;i<arrayAirportDestinatino.length;i++){
            Log.e("recorrerArrayDeSelects",i +" :: <"+arrayAirportDestinatino[i]+">" );
        }
    }
    private void sendChanel(String title,String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title,message);
        mNotificationHelper.getManager().notify(1,nb.build());
    }


    public NotificationManager getManager(){
        if (mManager==null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification(String title, String message){
        Intent intent = new Intent(this,NotificationActivity.class);
        PendingIntent resulPending = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setContentIntent(resulPending);
    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.canShowBadge();
            channel.setShowBadge(true);
            channel.setLightColor(Color.CYAN);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageSelect(DocumentSnapshot message) {
        Log.e("NotificationActSel","onmessageslect:"+message.getId());
    }

}
