package com.aasana.caedu.ocrprognostic.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SelectAirportActivity extends AppCompatActivity {

    public static final String SELECT_ARIPORTS = "SELECT";
    LinearLayout mLinearSelect = null;
    CheckBox mCHeckbox = null;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String mNameAirport = null;
    private String mNameDomain = null;
    private String mStringFinalDomain = "TorresControl";
    private ArrayList<Object> mTorresDomainSelect = new ArrayList<Object>();
    private ArrayList<String> nameCheckBox = new ArrayList<String>();
    private ArrayList<CheckBox> arrayofCheckbox = new ArrayList<>();
    private Map<String,Object> mAirportMap = new HashMap<>();
    private ArrayList<String> dataFromFile = new ArrayList<String>();
    private String[] arrayReadFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_airport);
        mLinearSelect = (LinearLayout) findViewById(R.id.linear_select);
        readInternalFile();
        initializeNames();
    }


    private void showCheckBox() {

        for (int i= 0; i<nameCheckBox.size();i++){
            mCHeckbox = new CheckBox(this);
            mCHeckbox.setId(i);
            mCHeckbox.setText(nameCheckBox.get(i));
            mCHeckbox.setTag(nameCheckBox.get(i));
            /*mCHeckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        writeInPlainText();
                        Log.e("value is checked",compoundButton.getTag().toString());
                        createInternalFile(compoundButton.getTag().toString());
                    }else {
                        Log.e("value is unchecked",compoundButton.getTag().toString());
                    }
                }
            });*/
            arrayofCheckbox.add(mCHeckbox);
            mLinearSelect.addView(mCHeckbox);
        }

        Button mButton = new Button(this);
        mButton.setText("Terminar Seleccion");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuilder stringBuilder= new StringBuilder();
                for(int i =0;i<arrayofCheckbox.size();i++){
                    if (arrayofCheckbox.get(i).isChecked()){
                        stringBuilder.append(arrayofCheckbox.get(i).getTag());
                        stringBuilder.append(",");
//                        Log.e("arrayofCheck", " // "+arrayofCheckbox.get(i).getText());
                    }
                }
                if (stringBuilder.length()>=1){
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    createInternalFile(stringBuilder.toString());
                    finish();
                }
                else makeToast("Seleccione aeropuerto(s) de destino");

            }

        });
        mLinearSelect.addView(mButton);
    }

    void readInternalFile(){
        try {
            FileInputStream fileIn = openFileInput(AeropuertoActivity.FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[100];
            int charRead;
            while ((charRead=InputRead.read(inputBuffer))>0) {
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                Log.e("SelectReadFile",readstring);
                arrayReadFile = readstring.split(",");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void initializeNames() {
        if (arrayReadFile.length>0  && arrayReadFile.length<=2){
            mNameAirport = arrayReadFile[0];
            mNameDomain = arrayReadFile[1];
        fillListFromFirestore();
            for (int i=0;i<arrayReadFile.length;i++){
                Log.e("initializeNames",arrayReadFile[i]+" // nameAirport: " +mNameAirport+" :  nameDomain: "+mNameDomain+"// ");
            }
        }else{
            makeToast("vuelva a menu -> Cambiar Aeroupuerto");
            this.finish();
        }
    }

    private void createInternalFile(String date) {
        try {
            FileOutputStream fOut = openFileOutput(AeropuertoActivity.FILENAME, MODE_APPEND);
            fOut.write(date.getBytes());
//            fOut.write("\n".getBytes());
            fOut.close();
//            Log.e("selectSaveInternal", " date= " + date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void prepareNameCheckBox(Map<String,Object> date) {
        Set keys = date.keySet();

        for(Object key: keys){
            String fildName = key.toString();
            if (!fildName.equals(mNameAirport)) {
                nameCheckBox.add(fildName);
                Log.e("date.keySet()", fildName+ ": " + date.get(key));

            }
        }

        showCheckBox();

    }

    private void fillListFromFirestore() {

        db.collection(mNameDomain.concat(mStringFinalDomain))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().containsKey(mNameAirport)) {
                                    Log.e("CONTAIN KEY", document.getId()+" = "+nameCheckBox.size()+"==>  " + document.getData());
                                    if (document.getData()!=null){
                                        prepareNameCheckBox(document.getData());
                                    }
                                }
                                Log.e("mNameAirport", "id :  " + document.getId() + "  =>   Data" + document.getData());
                            }


                            } else {
                            String stringToast = "!! No se encuentra:"+mNameAirport+" ...  Verifique su internet ¡¡";
                            makeToast(stringToast);
                        }
                    }
                });
    }


    private void makeToast(String stringToast){
        Toast.makeText(getApplicationContext(),stringToast,Toast.LENGTH_LONG).show();

    }
}
