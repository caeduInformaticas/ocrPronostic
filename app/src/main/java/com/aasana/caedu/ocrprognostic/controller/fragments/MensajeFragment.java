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
import com.aasana.caedu.ocrprognostic.controller.adapter.MensajePagerAdapter;
import com.aasana.caedu.ocrprognostic.model.Mensaje;
import com.aasana.caedu.ocrprognostic.model.MensajeLista;
import com.aasana.caedu.ocrprognostic.model.MessageFlight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MensajeFragment extends Fragment implements ViewPager.OnPageChangeListener {

    public static final String ARG_WINE = "MensajeFragment.ARG_WINE";
    private static final String SAVE_ADAPTER = "ADAPTER" ;
    private TextView mDestinationTitle=null, mDateOrigiTitle=null,mDatecreationTitle=null, mPriorityTitle =null, mTitlePages;
    private EditText mDestinationEdit = null, mDateOrigiEdit = null,mDateCreationEdit = null, mPriorityEdit =null;
    private Button mButtonSend = null;

    private ViewPager mPager = null;
    private ActionBar mActionBar = null;
    private MensajeLista mMensajeLista = null;
    private String mNameAirport = null;
    private String mNameDomain = null;
    private MensajePagerAdapter mensajePagerAdapter = null;
    public String mStringFinalDomain = "TorresControl";

    FirebaseFirestore db=FirebaseFirestore.getInstance();


    HashMap<Integer,ValorMensajeFragment> mValueMensajeFragmentHashMap = new HashMap<Integer, ValorMensajeFragment>();;
    private boolean band = false;
    private ArrayList<String> dataFromFile = new ArrayList<String>();
    private Date timestamp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState!=null){
            mNameAirport = getArguments().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
            mNameDomain = getArguments().getString(AeropuertoActivity.NOMBRE_DOMINIO);
            Log.e("savedInstanceState",getArguments().toString());

        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments()!= null && getArguments().containsKey(AeropuertoActivity.NOMBRE_AEROPUERTO)){

            mNameAirport = getArguments().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
            mNameDomain = getArguments().getString(AeropuertoActivity.NOMBRE_DOMINIO);
            //hay que guardar los names
            Log.e("getArgumentOncreate",mNameAirport+mNameDomain);
        }
        else Log.e("getArgument","nullllllllllllllllllll");

        View root = inflater.inflate(R.layout.fragment_mensaje, container, false);


        mDestinationTitle = (TextView) root.findViewById(R.id.destination);
        mDestinationEdit = (EditText) root.findViewById(R.id.value_destination);

        mDateOrigiTitle = (TextView) root.findViewById(R.id.origi_message);
        mDateOrigiEdit = (EditText) root.findViewById(R.id.value_origi_message);

        mDatecreationTitle = (TextView) root.findViewById(R.id.creation_message);
        mDateCreationEdit = (EditText) root.findViewById(R.id.value_creation_message);

        mPriorityTitle = (TextView) root.findViewById(R.id.priority);
        mPriorityEdit = (EditText) root.findViewById(R.id.value_priority);

        mTitlePages = (TextView) root.findViewById(R.id.title_page);

        mPager = (ViewPager) root.findViewById(R.id.pager);
        mensajePagerAdapter = new MensajePagerAdapter(this.getFragmentManager());
        mPager.setAdapter(mensajePagerAdapter);
        mButtonSend = (Button) root.findViewById(R.id.sendFirestore);
        mButtonSend.setVisibility(View.INVISIBLE);


        mMensajeLista = mMensajeLista.getInstance();

        mActionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mPager.addOnPageChangeListener(this);

        if (savedInstanceState!= null && savedInstanceState.containsKey(AeropuertoActivity.NOMBRE_DOMINIO)){
            mNameAirport = getArguments().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
            mNameDomain = getArguments().getString(AeropuertoActivity.NOMBRE_DOMINIO);
        }
        return root;
    }

    private int positionEditEmpty(){

        int res = 0;
        if (mPriorityEdit.getText().toString().isEmpty())
            res = 4;
        if (mDateCreationEdit.getText().toString().isEmpty())
            res=3;
        if (mDateOrigiEdit.getText().toString().isEmpty())
            res = 2;
        if (mDestinationEdit.getText().toString().isEmpty()){
            res=1;}
        return res;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (isEmptyEditText()) {
            if ( position ==1 ) {
                if (mValueMensajeFragmentHashMap.isEmpty()) {
                    mPager.setCurrentItem(0, false);
//                    Log.e("mensajefragment", "onPageSelected ---::  " + position);
                }
                else if (mValueMensajeFragmentHashMap.get(0).valueEditText().isEmpty())
                    mPager.setCurrentItem(0, false);
            }
        }
        if (position == mMensajeLista.getMensajeCount()-1 ){
            mButtonSend.setVisibility(View.VISIBLE);
            showButtonSend();

//            }
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        int pagerCurrentItem = mPager.getCurrentItem();

        if (state == ViewPager.SCROLL_STATE_DRAGGING ) {
            if (isEmptyEditText()){
//            Log.e("mensajefragment", "SCROLL_STATE_DRAGGING ---::  " + pagerCurrentItem);
                mPager.setCurrentItem(mPager.getCurrentItem()-1, false);

            }else{
                putValueMessageFragment(pagerCurrentItem);
            }
        }
    }

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("mensajeFragment","onSaveInstanceState "+mNameAirport+mNameDomain);
        outState.putString(AeropuertoActivity.NOMBRE_AEROPUERTO,mNameAirport);
        outState.putString(AeropuertoActivity.NOMBRE_DOMINIO,mNameDomain);
    }
*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mensaje, menu);
    }
    public Map<Integer,Object> getTextsFragment(){
        Map<Integer,Object> res = new HashMap<Integer, Object>();
        for (int i = 0; i< mValueMensajeFragmentHashMap.size(); i++){
            String value = mValueMensajeFragmentHashMap.get(i).valueEditText();
            res.put(i,value);
        }
        return  res;
    }
    private void putValueMessageFragment(int position){
        if (mValueMensajeFragmentHashMap.containsKey(position)){
            mValueMensajeFragmentHashMap.remove(position);
            mValueMensajeFragmentHashMap.put(position,(ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem()));
//                    Log.e("HASHMAPiff  ", mValueMensajeFragmentHashMap.get(pagerCurrentItem).valueEditText());

        }
        else {
            mValueMensajeFragmentHashMap.put(position, (ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem()));
//                    Log.e("HASHMAPelse  ", mValueMensajeFragmentHashMap.get(pagerCurrentItem).valueEditText() + "  "+ pagerCurrentItem);
        }
    }

    private void notAdvance(int position){
        if ( position ==1 ) {
            if (mValueMensajeFragmentHashMap.isEmpty()) {
                mPager.setCurrentItem(0, false);
//                    Log.e("mensajefragment", "onPageSelected ---::  " + position);
            }
            else if (mValueMensajeFragmentHashMap.get(0).valueEditText().isEmpty())
                mPager.setCurrentItem(0, false);
        }
    }
    public ValorMensajeFragment mapEditTextFragment(int position){
        return (ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, position);

    }
    public boolean isEmptyEditText(){
        boolean res = false;
        int pagerCurrentItem = mPager.getCurrentItem();
        ValorMensajeFragment myFragment = (ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem());
        String editTextValue = myFragment.valueEditText();
        if (editTextValue.isEmpty()){

            res = true;
        }
//        Log.e("valueedittext" ,"in mensaje fragmen ---::  " +    editTextValue + "::  " +pagerCurrentItem);

        return res;
    }

    private void showButtonSend(){
        /*ValorMensajeFragment myFragment = mapEditTextFragment(mPager.getCurrentItem());
        myFragment.setmDominioAndTorre(nameDomain,nameAeropuerto);
        myFragment.setVisibleBtnSend();
//            if (!myFragment.getIsEmtyEditText()){

//            Log.e("getIsEmtyEditText", " FALSE ---::  " + position);

        myFragment.onSaveDates(getTextsFragment(),mValueMensajeFragmentHashMap.size());
        */
        mButtonSend.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int position = positionEditEmpty();
                if (position>0){

                    toastForEditText(position);
                }else {
                    obtainValuesForSave();
                }
            }
        });
    }

    private void obtainValuesForSave() {
        if (isEmptyEditText()){
            toastValueMessage(mMensajeLista.getMensajeCount()-1);
        }
        else{
            final String WHITESPACE = " ";
            putValueMessageFragment(mPager.getCurrentItem());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i< mValueMensajeFragmentHashMap.size(); i++){
                String value = mValueMensajeFragmentHashMap.get(i).valueEditText();
                builder.append(value);
                builder.append(WHITESPACE);
            }
            builder.deleteCharAt(builder.length()-1);
            String message = builder.toString();
            String destination = mDestinationEdit.getText().toString();
            String feOrigi = mDateOrigiEdit.getText().toString();
            String feCreation = mDateCreationEdit.getText().toString();
            String priority = mPriorityEdit.getText().toString();

            MessageFlight messageFlight = new MessageFlight(destination,feOrigi,feCreation,priority,message);
            sendToFirestore(messageFlight);

        }
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public Date getDateFromString(String datetoSaved){

        try {
            Date date = format.parse(datetoSaved);
            return date ;
        } catch (ParseException e){
            return null ;
        }

    }

    public String getTimestamp() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return date;
    }
    private void sendToFirestore(MessageFlight messageFlight) {
    if (mNameDomain != null && (mNameAirport!=null) ) {
//        Log.e("sendToFirestore",mNameDomain.concat(mStringFinalDomain));

//            if (band){

                db.collection(mNameDomain).document(mNameAirport+getTimestamp())
                        .set(messageFlight)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String stringToast = "!! Enviado satisfactoriamente ¡¡";
                                makeToast(stringToast);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String stringToast = "!! Error al enviar..  Verifique su internet ¡¡"+e.getMessage();
                                makeToast(stringToast);
                            }
                        });
//            }
//            else
//            {
//                String stringToast = "la torre de control '"+mNameAirport+"' no pertenece al dominio:"+mNameDomain;
//                makeToast(stringToast);
//            }
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

    private void toastValueMessage(int position){
        String fild = mMensajeLista.getMensaje(position).getNombreCampo();
        /*
        switch (position){
            case 3:
                fild = mMensajeLista.getMensaje(position).getNombreCampo();
                break;
            case 2:
                fild = "YYYY";
                break;
            case 1:
                fild = "VVVV";
                break;
        }
*/
        Toast.makeText(getActivity(),"Complete "+ fild+ " para Continuar",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);
        int pagerCurrentItem = mPager.getCurrentItem();
        int itemId = item.getItemId();


        if (itemId == R.id.menu_next && pagerCurrentItem < mMensajeLista.getMensajeCount() - 1 && !isEmptyEditText()) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);


            //updateActionBar(pagerCurrentItem);
            return true;
        }

        else if (itemId == R.id.menu_prev && pagerCurrentItem > 0 && !isEmptyEditText()) {
            mPager.setCurrentItem(pagerCurrentItem - 1);
           // updateActionBar(pagerCurrentItem);

           // mpa.getValueGettex();
            //Log.e("MensajePagerAdapter" ,"TEXT FROM CAMERA GOT ---::  " +  mpa.getValueGettex());

            return true;
        }
        else {
            return superValue;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        MenuItem menuNext = menu.findItem(R.id.menu_next);
        MenuItem menuPrev = menu.findItem(R.id.menu_prev);

        menuNext.setEnabled(mPager.getCurrentItem() < mMensajeLista.getMensajeCount() - 1);
        menuPrev.setEnabled(mPager.getCurrentItem() > 0 );
    }


}


/*Log.e("setDominiotorre", mNameAirport + "   dominioconcat: "+ mNameDomain.concat(mStringFinalDomain));
            Map<Integer, Object>  dates = getTextsFragment();
            Map<String, Object> data1 = new HashMap<>();
            for (int i=0;i<dates.size();i++){
                String key = ""+i;
                Object obj = dates.get(i);
                data1.put(key,obj);
//            Log.e("onSaveDates" ,"onclik   for ---::  " +  obj.toString()+ "  "+ i);
            }
            data1.put("")

            db.collection(mNameDomain.concat(mStringFinalDomain))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().containsKey(mNameAirport)){
                                        Log.e("CONTAIN KEY", " ======>  " + document.getData());
                                        band = true;
                                    }
                                    Log.e("succesfull ", "id :  "+document.getId() + "  =>   Data" + document.getData());
                                }
                            } else {
                                Log.d("insuccesfull", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            if (band){
                Log.e("BAND ", "///  TRUE");
                db.collection(mNameDomain).document(mNameAirport)
                        .set(data1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("setDate", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("nott SET", "Error writing document", e);
                            }
                        });
            }
*/