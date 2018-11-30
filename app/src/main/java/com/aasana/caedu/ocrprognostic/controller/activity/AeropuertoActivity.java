package com.aasana.caedu.ocrprognostic.controller.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AeropuertoActivity extends AppCompatActivity  {

    public static final String NOMBRE_AEROPUERTO = "AeropuertoActivity.NOMBRE_AEROPUERTO";
    public static final String NOMBRE_DOMINIO = "AeropuertoActivity.NOMBRE_DOMINIO";
    TextView insertNameAeropuerto =null;
    EditText EditNameAeropuerto = null;
    TextView insertDominioAeropuerto =null;
    EditText EditNameDominio= null;
    Button btnSendName = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String FILENAME = "dominio_aeropuerto.txt";
    String string = "hello world!";
    private Context context;
    String mNameAirport = null;
    String mNameDomain = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_source);
        insertNameAeropuerto = (TextView) findViewById(R.id.inserte_nombre_aeropuerto);
        EditNameAeropuerto = (EditText) findViewById(R.id.nombre_aeropuerto);
        insertDominioAeropuerto = (TextView) findViewById(R.id.inserte_dominio);
        EditNameDominio = (EditText) findViewById(R.id.dominio_perteneciente);
        context=this;
        btnSendName = (Button) findViewById(R.id.btn_send);
        btnSendName.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                deleteInternalFile();
                mNameAirport = EditNameAeropuerto.getText().toString();
                mNameDomain = EditNameDominio.getText().toString();
                thereAreNames();
            }
        });

    }

    private void thereAreNames() {
        if (mNameDomain!= null ){
            if (mNameAirport!=null) {
                db.collection(mNameDomain)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean isEmpty = task.getResult().isEmpty();
                                    Log.e("thereAreNames", mNameDomain+"  isEmpty " + isEmpty);
                                    if (!isEmpty) {
                                        comprobateAirport();

                                    } else
                                        makeToast("Error el dominitio: " + mNameDomain + " no existe");
                                } else {
                                    Log.e("thereAreNames", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
            else makeToast("Por favor recuerde el aeropuerto" );
        }
        else makeToast("Por favor recuerde el Dominio" );
    }

    private void comprobateAirport() {
        db.collection(mNameDomain.concat("TorresControl"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean band = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().containsKey(mNameAirport)) {
                                    band = true;
                                    Log.e("CONTAIN KEY", " ======>  " + document.getData());

                                    createInternalFile();
//                                    createExtermalFile();
                                    Intent nextScreen = new Intent(getBaseContext(), NavigateActivity.class);
                                    /*nextScreen.putExtra(NOMBRE_AEROPUERTO, mNameAirport);
                                    nextScreen.putExtra(NOMBRE_DOMINIO, mNameDomain);*/

                                    startActivity(nextScreen);
                                    finish();
                                }
                                Log.e("mNameAirport", "id :  " + document.getId() + "  =>   Data" + document.getData());
                            }
                            if (!band){
                                makeToast("Error el Aeropuerto: "+ mNameAirport + "No existe");
                            }
                        } else {
                            String stringToast = "!! No se Dominio:"+mNameDomain+" ...  Verifique su internet ¡¡";
                            makeToast(stringToast);                            }
                    }
                });
    }

    private void makeToast(String stringToast){
        Toast.makeText(getApplicationContext(),stringToast,Toast.LENGTH_LONG).show();

    }

    private void createInternalFile() {
        try {
            final StringBuilder stringBuilder= new StringBuilder();
            stringBuilder.append(mNameAirport);
            stringBuilder.append(",");
            stringBuilder.append(mNameDomain);
            stringBuilder.append(",");

            FileOutputStream fOut = openFileOutput(FILENAME,MODE_APPEND);
            fOut.write(stringBuilder.toString().getBytes());
            fOut.close();
            Log.e("AeropuertSaveInternal", " = "+stringBuilder.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    void deleteInternalFile(){
    try {
        File[] files = getBaseContext().getFilesDir().listFiles();
        if(files != null)
            for(File file : files) {
                file.delete();
                Log.e("deleteInternalFile", " files = " + getFilesDir().toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
