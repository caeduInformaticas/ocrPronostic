package com.aasana.caedu.ocrprognostic.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by caedu on 12/9/2018.
 */

public class MensajeLista {

    private static MensajeLista sInstance = null;
    private List<Mensaje> mMensajes = null;

    public static MensajeLista getInstance() {
        if (sInstance == null) {
            sInstance = new MensajeLista();
        }
        return sInstance;
    }

    public MensajeLista(){
        Mensaje clave = new Mensaje("CCC");
        Mensaje yygg = new Mensaje("YYGG ggz");/*
        Mensaje fecha = new Mensaje("dddffGtm");
        Mensaje vvvvd = new Mensaje("VVVVD");
        Mensaje ww = new Mensaje("WW WW WW");
        Mensaje nnnhh1 = new Mensaje("NNNhhh1");
        Mensaje nnnhh2 = new Mensaje("NNNhhh2");
        Mensaje nnnhh3 = new Mensaje("NNNhhh3");
        Mensaje it = new Mensaje("IT / TT");
        Mensaje apppp = new Mensaje("APPPP");
        Mensaje informacion = new Mensaje("Informacion y TREND");
        Mensaje tt = new Mensaje("TT");
        Mensaje tbh = new Mensaje("TBH");
        Mensaje hr = new Mensaje("HR");*/

        mMensajes = Arrays.asList(new Mensaje[]{clave,yygg,
//                fecha,vvvvd,ww,nnnhh1,nnnhh2,nnnhh3,it,apppp,informacion,tt,tbh,hr
        });
    }
    public Mensaje getMensaje(int index) {
        return mMensajes.get(index);
    }

    public int getMensajeCount() {
        return mMensajes.size();
    }

}
