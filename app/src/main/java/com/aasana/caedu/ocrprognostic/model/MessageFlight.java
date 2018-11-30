package com.aasana.caedu.ocrprognostic.model;

public class MessageFlight {
    private String destino = null;
    private String fechaOrigen = null;
    private String fechaCreacion = null;
    private String prioridad = null;
    private String mensaje = null;

    public MessageFlight(String Destino, String FechaOrigen, String FechaCreacion, String Prioridad, String Mensaje) {
        this.destino = Destino;
        this.fechaOrigen = FechaOrigen;
        this.fechaCreacion = FechaCreacion;
        this.prioridad = Prioridad;
        this.mensaje = Mensaje;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setFechaOrigen(String fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDestino() {
        return destino;
    }

    public String getFechaOrigen() {
        return fechaOrigen;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getMensaje() {
        return mensaje;
    }
}
