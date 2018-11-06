package com.aasana.caedu.ocrprognostic.controller.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.adapter.NotifyAdapter;
import com.aasana.caedu.ocrprognostic.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends FragmentContainerActivity implements NotifyAdapter.OnMesaggeSelectedListener {

    String [] datesfirebase;

    @BindView(R.id.recycler_menssages)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_current_sort_by)
    TextView mCurrentSortByView;

    @BindView(R.id.view_empty)
    ViewGroup mEmptyView;;



    Query mQuery;
    private NotifyAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    CollectionReference collectionReference;

    ArrayList<Message> mensajeList = new ArrayList<Message>();
    private String domain;
    private String torre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);


        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpRecyclerView();
        initFirestore();
        //addTestDataToFirebase();
        initRecyclerView();



//        obtenerObjectFirestore();
        String nameAeropuerto = getIntent().getExtras().getString("TORRE");
        String nameDominio= getIntent().getExtras().getString("DOMAIN");
        Log.e("NotifiExtras",nameAeropuerto +"  "+ nameDominio);

//        datesfirebase  = getIntent().getExtras().getStringArray("NOTIFY");
//        String string  = getIntent().getExtras().getString("NOTIFY");
//        Log.e("notificationActivity",datesfirebase.toString()+"   ::    "+string);
    }

    private void setUpRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_menssages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("beni")
//                .orderBy("indice", Query.Direction.DESCENDING)
        ;
        collectionReference = mFirestore.collection("beni");
    }
    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("nulll", "No query, not initializing RecyclerView");
        }


        mAdapter = new NotifyAdapter(mQuery,this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    Log.e("Activity onDataChaned", "getItemCount es ceroo");
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    Log.e("Activity onDataChaned", "getItemCount is ::" +getItemCount());
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("onError", e.getMessage());
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }

        };

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setQuery(mQuery);
    }

    @Override
    public void onMessageSelect(DocumentSnapshot message) {
        Log.e("NotificationAct","onmessageslect");
    }

    /*private void addTestDataToFirebase(){
        Message mensaje = new Message("reyes","inmediata","cadena de mensaje");
        Message mensaje2 = new Message("trinidad","inmediata","cadena de mensaje");
        mFirestore.collection("beni").document("reyes").set(mensaje);
        mFirestore.collection("beni").document("trinidad").set(mensaje2).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NotificationActivity.this,"mensajes aderidos ",Toast.LENGTH_LONG).show();
                        Log.e("adddatatofirestore"," success");
                    }

                }
        );
    }*/


}
