package com.aasana.caedu.ocrprognostic.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.aasana.caedu.ocrprognostic.controller.fragments.ValorMensajeFragment;
import com.aasana.caedu.ocrprognostic.model.MensajeLista;

/**
 * Created by caedu on 12/9/2018.
 */

public class MensajePagerAdapter extends FragmentPagerAdapter {
    private MensajeLista mMensajeLista = null;

    public MensajePagerAdapter(FragmentManager fm){
        super(fm);
        mMensajeLista = mMensajeLista.getInstance();
    }

    @Override
    public Fragment getItem(int position) {

        ValorMensajeFragment fragment = new ValorMensajeFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ValorMensajeFragment.ARG_VALOR, mMensajeLista.getMensaje(position));
        fragment.setArguments(arguments);

        return fragment;
    }
    /*public String getValueGettex(){

        Log.e("MensajePagerAdapter" ,"TEXT FROM CAMERA GOT ---::  " +  fragment.mValueEditText);

        return fragment.mValueEditText;

    }
*/

    @Override
    public int getCount() {
        return mMensajeLista.getMensajeCount();
    }
    public CharSequence getPageTitle(int position) {

        super.getPageTitle(position);

        return mMensajeLista.getMensaje(position).getNombreCampo();
    }

}
