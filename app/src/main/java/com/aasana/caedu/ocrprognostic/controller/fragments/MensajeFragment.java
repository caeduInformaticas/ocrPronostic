package com.aasana.caedu.ocrprognostic.controller.fragments;

import android.os.Bundle;
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

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.adapter.MensajePagerAdapter;
import com.aasana.caedu.ocrprognostic.model.MensajeLista;

import java.util.HashMap;
import java.util.Map;

public class MensajeFragment extends Fragment implements ViewPager.OnPageChangeListener{

    public static final String ARG_WINE = "MensajeFragment.ARG_WINE";
    private ViewPager mPager = null;
    private ActionBar mActionBar = null;
    private MensajeLista mMensajeLista = null;
    String nameAeropuerto;
    public boolean poibleNext = false;
    public String nameDomain;
    HashMap<Integer,ValorMensajeFragment> valorMensajeFragmentHashMap = new HashMap<Integer, ValorMensajeFragment>();;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mensaje, container, false);
        mPager = (ViewPager) root.findViewById(R.id.pager);
        nameAeropuerto = getArguments().getString("TORRE");
        nameDomain = getArguments().getString("DOMAIN");
        Log.e("MensajeFragment" ,"nombre Aeropuerto ---::  " + nameAeropuerto);

        mPager.setAdapter(new MensajePagerAdapter(getFragmentManager()));

        mMensajeLista = mMensajeLista.getInstance();

        mActionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mPager.addOnPageChangeListener(this);

        updateActionBar(0);
//
//            Log.e("MENSAJEFRAGMENT" ,"TEXT FROM CAMERA GOT ---::  " +  getArguments().getString(ValorMensajeFragment.ARG_VALOR));
//

        return root;
    }


    private void updateActionBar(int i) {
        mActionBar.setTitle(mMensajeLista.getMensaje(i).getNombreCampo());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (isEmptyEditText()) {
            if ( position ==1 ) {
                if (valorMensajeFragmentHashMap.isEmpty()) {
                    mPager.setCurrentItem(0, false);
//                    Log.e("mensajefragment", "onPageSelected ---::  " + position);
                }
                else if (valorMensajeFragmentHashMap.get(0).valueEditText().isEmpty())
                    mPager.setCurrentItem(0, false);
            }
        }
        if (position == mMensajeLista.getMensajeCount()-1 ){

            ValorMensajeFragment myFragment = mapEditTextFragment(mPager.getCurrentItem());
            myFragment.setmDominioAndTorre(nameDomain,nameAeropuerto);
            myFragment.setVisibleBtnSend();
//            if (!myFragment.getIsEmtyEditText()){

//            Log.e("getIsEmtyEditText", " FALSE ---::  " + position);

            myFragment.onSaveDates(getTextsFragment(),valorMensajeFragmentHashMap.size());
//            }
        }
    }
    private void notAdvance(int position){
        if ( position ==1 ) {
            if (valorMensajeFragmentHashMap.isEmpty()) {
                mPager.setCurrentItem(0, false);
//                    Log.e("mensajefragment", "onPageSelected ---::  " + position);
            }
            else if (valorMensajeFragmentHashMap.get(0).valueEditText().isEmpty())
                mPager.setCurrentItem(0, false);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        int pagerCurrentItem = mPager.getCurrentItem();

        if (state == ViewPager.SCROLL_STATE_DRAGGING ) {
            if (isEmptyEditText()){
//            Log.e("mensajefragment", "SCROLL_STATE_DRAGGING ---::  " + pagerCurrentItem);
                mPager.setCurrentItem(mPager.getCurrentItem()-1, true);

            }else{
                if (valorMensajeFragmentHashMap.containsKey(pagerCurrentItem)){
                    valorMensajeFragmentHashMap.remove(pagerCurrentItem);
                    valorMensajeFragmentHashMap.put(pagerCurrentItem,(ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem()));
//                    Log.e("HASHMAPiff  ", valorMensajeFragmentHashMap.get(pagerCurrentItem).valueEditText());

                }
                else {
                    valorMensajeFragmentHashMap.put(pagerCurrentItem, (ValorMensajeFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem()));
//                    Log.e("HASHMAPelse  ", valorMensajeFragmentHashMap.get(pagerCurrentItem).valueEditText() + "  "+ pagerCurrentItem);
                }
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mensaje, menu);
    }
    public Map<Integer,Object> getTextsFragment(){
        Map<Integer,Object> res = new HashMap<Integer, Object>();
        for (int i =0 ;i<valorMensajeFragmentHashMap.size();i++){
            String value =valorMensajeFragmentHashMap.get(i).valueEditText();
            res.put(i,value);
        }
        return  res;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);
        int pagerCurrentItem = mPager.getCurrentItem();
        int itemId = item.getItemId();


        if (itemId == R.id.menu_next && pagerCurrentItem < mMensajeLista.getMensajeCount() - 1 && isEmptyEditText()) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);


            updateActionBar(pagerCurrentItem);
            return true;
        }

        else if (itemId == R.id.menu_prev && pagerCurrentItem > 0 && isEmptyEditText()) {
            mPager.setCurrentItem(pagerCurrentItem - 1);
            updateActionBar(pagerCurrentItem);

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
