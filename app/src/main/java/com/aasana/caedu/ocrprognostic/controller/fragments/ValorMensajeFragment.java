package com.aasana.caedu.ocrprognostic.controller.fragments;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.activity.NotificationActivity;
import com.aasana.caedu.ocrprognostic.controller.camera.AbbyyActivity;
import com.aasana.caedu.ocrprognostic.model.Mensaje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caedu on 12/9/2018.
 */

public class ValorMensajeFragment extends Fragment{
    public static final String ARG_VALOR = "ValorMensajeActivityARG_VALOR";
    private static final String TAG = ValorMensajeFragment.class.getSimpleName();
    public String mTextCaptured = null;
    public String mValueEditText = "null";
    private static final int SETTINGS_REQUEST  = 1;
    public String mDominioApp;
    public String mTorreApp;
    public String mStringFinalDomain = "TorresControl";
    FirebaseFirestore db=FirebaseFirestore.getInstance();
  //  private CollectionReference pronosticRef = db.collection("pronostic");

    //modelos
    Mensaje mMensaje = null;

    //vistas
    private boolean band = false;
    Button mBtnSend ;
    private TextView mNombreCampo = null;
    private EditText mValorCampo = null;
    AbbyyActivity mAbbyy = new AbbyyActivity();
    private boolean isEmptyEditText =false;


    public boolean getIsEmtyEditText(){
        return  isEmptyEditText;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!= null){
            mMensaje = (Mensaje) getArguments().getSerializable(ARG_VALOR);
            Log.e("savedInstanceState",mMensaje.getClass().toString());
        }
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.dataPasser = (OnDataPass) context;
////    }
//    public void passData(String data) {
//        dataPasser.onDataPass(data);
//    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View root = inflater.inflate(R.layout.fragment_valor_mensaje, container, false);

        // Recogemos el modelo
        mMensaje = (Mensaje) getArguments().getSerializable(ARG_VALOR);

        // Accedemos a las vistas desde el controlador
        mNombreCampo = (TextView) root.findViewById(R.id.nombre_campo);
        mValorCampo = (EditText) root.findViewById(R.id.value_campo);

        mNombreCampo.setText(mMensaje.getNombreCampo());

        ImageButton ib = (ImageButton)root.findViewById(R.id.textWithCamera);
        mBtnSend = (Button) root.findViewById(R.id.sendFirebase);
        mBtnSend.setVisibility(View.INVISIBLE);
        ib.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
            Intent nextScreen = new Intent(getContext(), mAbbyy.getClass());
            startActivityForResult(nextScreen,123);

            }
        });
        if (savedInstanceState != null) {
                Log.e("SAVEDINSTANCE" ,"TEXT FROM CAMERA GOT ---::  " +  savedInstanceState.getString("ONSAVE"));
            mValorCampo.setText(savedInstanceState.getString("ONSAVE"));
        }
        valueEditText();
        return root;
    }
    public void setVisibleBtnSend(){
        mBtnSend.setVisibility(View.VISIBLE);

    }
    public void onSaveDates(final Map<Integer,Object> dates, final int indice){
        mBtnSend.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!valueEditText().isEmpty()){
                    isEmptyEditText = true;
                    dates.put(indice,valueEditText());
                    sendToFirebase(dates);
                }
            }
        });
    }
    public void setmDominioAndTorre(String dominio, String torre){
        mDominioApp = dominio;
        mTorreApp=torre;
    }
    private void sendToFirebase(Map<Integer,Object> dates){

        Log.e("setDominiotorre", mTorreApp + "   dominioconcat: "+ mDominioApp.concat(mStringFinalDomain));
        Map<String, Object> data1 = new HashMap<>();
        for (int i=0;i<dates.size();i++){
            String key = ""+i;
            Object obj = dates.get(i);
            data1.put(key,obj);
//            Log.e("onSaveDates" ,"onclik   for ---::  " +  obj.toString()+ "  "+ i);
        }

        db.collection(mDominioApp.concat(mStringFinalDomain))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().containsKey(mTorreApp)){
                                    Log.e("CONTAIN KEY", " ======>  " + document.getData());
                                    band = true;
                                }
                                Log.d(TAG, "id :  "+document.getId() + "  =>   Data" + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        if (band){
            Log.e("BAND ", "///  TRUE");
            db.collection(mDominioApp).document(mTorreApp)
                    .set(data1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }
    public String valueEditText() {
        mValueEditText = mValorCampo.getText().toString();
        Log.e("VALUEEDITTEXT" ," ---::  " +  mValorCampo.getText().toString());

        return mValorCampo.getText().toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mensaje, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTextCaptured != null){
            outState.putString("ONSAVE",mTextCaptured);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (123) : {
                if (resultCode == getActivity().RESULT_OK) {
                    mTextCaptured = data.getStringExtra("VALOR");
                    Log.e("onActivityResult" ,"TEXT FROM CAMERA GOT ---::  " +  mTextCaptured);
                    mValorCampo.setText(mTextCaptured,TextView.BufferType.EDITABLE);

                }
                break;
            }
        }
    }



//    public interface OnDataPass{
//        public String onDataPass(String data);
//    }
}
