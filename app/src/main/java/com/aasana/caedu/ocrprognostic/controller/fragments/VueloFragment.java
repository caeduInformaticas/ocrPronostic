package com.aasana.caedu.ocrprognostic.controller.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.activity.AeropuertoActivity;
import com.aasana.caedu.ocrprognostic.controller.activity.NavigateActivity;
import com.aasana.caedu.ocrprognostic.controller.adapter.MensajePagerAdapter;
import com.aasana.caedu.ocrprognostic.model.MensajeLista;
import com.aasana.caedu.ocrprognostic.model.MessageFlight;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class VueloFragment extends Fragment{

    public static final String ARG_WINE = "MensajeFragment.ARG_WINE";
    private static final String SAVE_ADAPTER = "ADAPTER" ;
    private TextView mDestinationTitle=null, mDateOrigiTitle=null,mDatecreationTitle=null, mPriorityTitle =null, mMessageTitle = null;
    private EditText mDestinationEdit = null, mDateOrigiEdit = null,mDateCreationEdit = null, mPriorityEdit =null, mMessageEdit = null;
    private Button mButtonSend = null;

    private ActionBar mActionBar = null;
    private String mNameAirport = null;
    private String mNameDomain = null;
    public String mStringFinalDomain = "TorresControl";

    FirebaseFirestore db=FirebaseFirestore.getInstance();


    HashMap<Integer,ValorMensajeFragment> mValueMensajeFragmentHashMap = new HashMap<Integer, ValorMensajeFragment>();;
    private boolean band = false;
    private ArrayList<String> dataFromFile = new ArrayList<String>();
    private Date timestamp;
    private String[] arrayReadFile;
    private String[] arrayAirportDestinatino;
    private StringBuilder stringDestination = new StringBuilder();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        readInternalFile();

            initializeNames();
            initializeArrayDestination();
            View root = inflater.inflate(R.layout.fragment_vuelo, container, false);
            mDestinationTitle = (TextView) root.findViewById(R.id.destination_vuelo);
            mDestinationEdit = (EditText) root.findViewById(R.id.value_destination_vuelo);

            if (stringDestination.length()>0) {
                stringDestination.deleteCharAt(stringDestination.length()-1);
                mDestinationEdit.setText(stringDestination.toString());
            }

            mDateOrigiTitle = (TextView) root.findViewById(R.id.origi_vuelo);
            mDateOrigiEdit = (EditText) root.findViewById(R.id.value_origi_vuelo);

            mDatecreationTitle = (TextView) root.findViewById(R.id.creation_vuelo);
            mDateCreationEdit = (EditText) root.findViewById(R.id.value_creation_vuelo);

            mPriorityTitle = (TextView) root.findViewById(R.id.priority_vuelo);
            mPriorityEdit = (EditText) root.findViewById(R.id.value_priority_vuelo);

            mMessageTitle = (TextView) root.findViewById(R.id.message_vuelo);
            mMessageEdit = (EditText) root.findViewById(R.id.insert_message_vuelo);


            mButtonSend = (Button) root.findViewById(R.id.send_vuelo);

            mActionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            mActionBar.setDisplayHomeAsUpEnabled(true);

      /*  if (savedInstanceState!= null && savedInstanceState.containsKey(AeropuertoActivity.NOMBRE_DOMINIO)){
            mNameAirport = getArguments().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
            mNameDomain = getArguments().getString(AeropuertoActivity.NOMBRE_DOMINIO);
        }*/

            mButtonSend.setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    int position = positionEditEmpty();
                    if (position>0){

                        toastForEditText(position);
                    }else {
                        sendToFirestore(getDateFromEdit());
                    }
                }
            });

        return root;

    }

    void readInternalFile(){
        try {
            FileInputStream fileIn = getActivity().openFileInput(AeropuertoActivity.FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[100];
            int charRead;
            while ((charRead=InputRead.read(inputBuffer))>0) {
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                Log.e("notifiREadInternal",readstring);
                arrayReadFile = readstring.split(",");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void initializeNames() {
        mNameAirport = arrayReadFile[0];
        mNameDomain = arrayReadFile[1];
        for (int i=0;i<arrayReadFile.length;i++){
            Log.e("VueloarrayReadFile","<" +arrayReadFile[i]+">");
        }
    }
    private void initializeArrayDestination() {
        arrayAirportDestinatino = new String[arrayReadFile.length-2];

        int j =0;
        for (int i =2;i<arrayReadFile.length;i++){
            arrayAirportDestinatino[j] = arrayReadFile[i];
            stringDestination.append(arrayReadFile[i]);
            stringDestination.append(",");
            Log.e("arrayAirportDestiVuelo",i +" :: "+arrayAirportDestinatino[j] + "  length =" +arrayReadFile[i]);
            j++;
        }
    }
    private MessageFlight getDateFromEdit() {
        String destino = mDestinationEdit.getText().toString();
        String fechaOrigen = mDateOrigiEdit.getText().toString();
        String fechaCreacion = mDateCreationEdit.getText().toString();
        String prioridad = mPriorityEdit.getText().toString();
        String mensaje = mMessageEdit.getText().toString();
        MessageFlight messageFlight = new MessageFlight(destino,fechaOrigen,fechaCreacion,prioridad,mensaje);
        return messageFlight;
    }

    private int positionEditEmpty(){

        int res = 0;
        if (mMessageEdit.getText().toString().isEmpty())
            res = 5;
        if (mPriorityEdit.getText().toString().isEmpty())
            res = 4;
        if (mDateCreationEdit.getText().toString().isEmpty())
            res=3;
        if (mDateOrigiEdit.getText().toString().isEmpty())
            res = 2;
        if (mDestinationEdit.getText().toString().isEmpty())
            res=1;
        return res;
    }

   /* @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("mensajeFragment","onSaveInstanceState "+mNameAirport+mNameDomain);
        outState.putString(AeropuertoActivity.NOMBRE_AEROPUERTO,mNameAirport);
        outState.putString(AeropuertoActivity.NOMBRE_DOMINIO,mNameDomain);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_mensaje, menu);
    }
    public String getTimestamp() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return date;
    }
    private void sendToFirestore(MessageFlight messageFlight) {
        if (mNameDomain != null && (mNameAirport!=null) ) {
            db.collection(mNameDomain).document(mNameAirport+getTimestamp())
                .set(messageFlight)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String stringToast = "!! Enviado satisfactoriamente ¡¡";
                        makeToast(stringToast);
                        getActivity().getFragmentManager().popBackStack();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String stringToast = "!! Error al enviar..  Verifique su internet ¡¡"+e.getMessage();
                        makeToast(stringToast);
                    }
                });
        }
        else {
            String stringToast = "Vuelva a introducir torre control y su respectivo dominio valido";
            makeToast(stringToast);
        }
    }

    private void makeToast(String stringToast){
        Toast.makeText(getActivity(),stringToast,Toast.LENGTH_LONG).show();

    }
    private void toastForEditText(int position) {
        String fild = null;
        switch (position){
            case 5:
                fild = "Mensaje";
                break;
            case 4:
                fild = "Prioridad";
                break;
            case 3:
                fild = "Fecha de Creacion";
                break;
            case 2:
                fild = "Fecha de Origen";
                break;
            case 1:
                fild = "Destino";
                break;
        }

        Toast.makeText(getActivity(),"LLene el Campo "+ fild+ " no lo deje vacio",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
