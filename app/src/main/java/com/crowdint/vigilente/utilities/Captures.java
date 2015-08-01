package com.crowdint.vigilente.utilities;

import android.location.Location;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class Captures {

    public static String[] fotos = {"","",""};
    public static int cantidad =0;
    public static Location localizacion = new Location("");
    public static String delito;
    public static String  seccion;
    public static String descripcion;
    public static String distrito;
    public static String direccion;
    public static String usuario;
    public static String userId;
    public static String provider;
    public static boolean facebook = false;
    public static boolean twitter  = false;
    public static boolean anonimo  = false;

    public static String getLatitude() {
        return String.valueOf(localizacion.getLatitude());
    }
    public static String getLongitude() {
        return String.valueOf(localizacion.getLongitude());
    }
    public static Location getLocalizacion() {
        return localizacion;
    }

    public static void setLocalizacion(Location localizacion) {
        if (localizacion == null) {
            Captures.localizacion = new Location("");
        }
        else
            Captures.localizacion = localizacion;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        Captures.userId = userId;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        Captures.usuario = usuario;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        Captures.direccion = direccion;
    }

    public static String getDelito() {
        return delito;
    }

    public static void setDelito(String delito) {
        Captures.delito = delito;
    }

    public static String getSeccion() {
        return seccion;
    }

    public static void setSeccion(String seccion) {
        Captures.seccion = seccion;
    }

    public static String getDescripcion() {
        return descripcion;
    }

    public static void setDescripcion(String descripcion) {
        Captures.descripcion = descripcion;
    }

    public static String getDistrito() {
        return distrito;
    }

    public static void setDistrito(String distrito) {
        Captures.distrito = distrito;
    }

    public static void limpiar(){
        fotos[0]="";
        fotos[1]="";
        fotos[2]="";

        cantidad = 0;
        delito= "";
        seccion="";
        descripcion="";
        distrito="";
        direccion="";
    }

    public static int nuevaFoto(String foto){
        int p = -1 ;
        if (cantidad >=3)
            return p =-1;
        else
        {
            for (int i=0;i<3; i++){
                if (fotos[i].equals("")){
                    fotos[i]=foto;
                    cantidad++;
                    p = i;
                    break;
                }
            }
            return p+1;
        }
    }

    public static void setLocation(Location loc){
        localizacion = loc;
    }

    public static void deleteFoto(int i){
        if (i<=3){
            fotos[i-1]="";
            cantidad--;
        }
    }

    public static  String getFoto(int i){
        if (i<=3){
            return fotos[i-1];
        }
        return "";
    }

    public Captures(){
        Captures.setLocalizacion(null);
    }
}
