package com.aasana.caedu.ocrprognostic.model;

import java.io.Serializable;

/**
 * Created by caedu on 12/9/2018.
 */

public class Mensaje implements Serializable { //para el Bundle.putSerializable del MensajePagerAdapter.getItem

    private int date;
    private String mNombreCampo = null;
    private String prioridad = null;
    private String fuente=null;



    public Mensaje(String prioridad, String fuente){
        this.mNombreCampo = "null";
        this.date= 4565132;
        this.fuente = fuente;
        this.prioridad = prioridad;
    }
    public Mensaje(String nombreCampo){
        mNombreCampo = nombreCampo;
        prioridad = nombreCampo;

    }
    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public int getDate() {
        return date;
    }

    public void setIndice(int date) {
        this.date = date;
    }

    public void setNombreCampo(String nombreCampo) {
        mNombreCampo = nombreCampo;
    }

    public void setPrioridad(String valor) {
        prioridad = prioridad;
    }

    public String getNombreCampo() {
        return mNombreCampo;
    }

    public String getPrioridad() {
        return prioridad;
    }
    /* private String mClaveEmitente = null;
    private String mFechaHora = null;
    private String mViento = null;
    public Mensaje(String claveEmitente, String fechaHora, String viento)
    {
        mClaveEmitente = claveEmitente;
        mFechaHora = fechaHora;
        mViento = viento;
    }

    public String getClaveEmitente() {
        return mClaveEmitente;
    }

    public String getFechaHora() {
        return mFechaHora;
    }

    public String getViento() {
        return mViento;
    }

    public void setClaveEmitente(String claveEmitente) {
        mClaveEmitente = claveEmitente;
    }

    public void setFechaHora(String fechaHora) {
        mFechaHora = fechaHora;
    }

    public void setViento(String viento) {
        mViento = viento;
    }*/
}
