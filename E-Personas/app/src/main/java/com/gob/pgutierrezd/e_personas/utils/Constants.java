package com.gob.pgutierrezd.e_personas.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by pgutierrezd on 17/10/2016.
 */
public class Constants {

    //Google Maps API
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final LatLng QUERETARO = new LatLng(20.5872194, -100.387161);

    //Facebook API
    public static final List<String> PERMISSIONS_FACEBOOK =
            Collections.unmodifiableList(Arrays.asList("email", "public_profile"));

    //SharedPreferences
    public static final String SHARED_PREFERENCES_LOGIN = "login";
    public static final String SHARED_PREFERENCES_LOGIN_ID_FLAG = "id";
    public static final String SHARED_PREFERENCES_LOGIN_EMAIL_FLAG = "email";

    //Encuesta
    public static final int CAMARA_REQUEST = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;

    public final static String IMAGE_PROFILE="base64.txt";
    public final static int INTERVALO_SEGUNDOS = 30;


    public static final int DATE_DIALOG_ID = 999;

    //SharePreferences para verificar ecuesta activa
    public static final String SHARED_PREFERENCES_COORDS = "coords";
    public static final String SHARED_PREFERENCES_COORDS_FLAG = "false";

    //CONSTANTES PARA LA BASE DE DATOS
    public static final String DATABASE_NAME = "epersonas.db";
    public static final int DATABASE_VERSION = 2;
    //Tabla de encuestas
    public static final String TABLE_ENCUESTAS = "encuestas";
    public static final String IDENCUESTA = "_id";
    public static final String IDUSUARIO = "idusuario";
    public static final String IDINFO = "idinfo";
    public static final String CS_OPINION_POLICIA = "cs_opinion_policia";
    public static final String CS_PATRULLAJE = "cs_patrullaje";
    public static final String CS_HORARIO_PATRULLAJE = "cs_horario_patrullaje";
    public static final String CS_ACCIONES_MUNICIPIO = "cs_acciones_municipio";
    public static final String CS_TRAMITE_PENDIENTE_BOOL = "cs_tramite_pendiente_bool";
    public static final String CS_TRAMITE_PENDIENTE_COM = "cs_tramite_pendiente_com";
    public static final String CS_GOBIERNO_MARCOS_AGUILAR = "cs_gobierno_marcos_aguilar";
    public static final String CS_ESPERAR_MARCOS = "cs_esperar_marcos";
    public static final String CS_DEPORTES_BOOL = "cs_deportes_bool";
    public static final String CS_DEPORTES_COM = "cs_deportes_com";
    public static final String CS_NECESIDAD_COLONIA = "cs_necesidad_colonia";
    public static final String ID_OPINION_REALIZAR_OBRA_NOMBRE = "id_opinion_realizar_obra_nombre";
    public static final String ID_OPINION_REALIZAR_OBRA = "id_opinion_realizar_obra";
    public static final String ID_RIESGOS_FRACCIONAMIENTO = "id_riesgos_fraccionamiento";
    public static final String ID_HORARIO_MAYOR_RIESGO = "id_horario_mayor_riesgo";
    public static final String IB_HAY_LOTES_BALDIOS = "ib_hay_lotes_baldios";
    public static final String IB_CONSIDERA_RIESGOS = "ib_considera_riesgos";
    public static final String IB_DEBEN_ATENDERSE = "ib_deben_atenderse";
    public static final String IB_REALIZADO_LIMPIEZA = "ib_realizado_limpieza";
    public static final String IB_DESMALEZADO_LOTES = "ib_desmalezado_lotes";
    public static final String SA_APARIENCIA_ALAMEDA = "sa_aparienza_alameda";
    public static final String SA_SEGURO_AL_TRANSITAR = "sa_seguro_al_transitar";
    public static final String SA_ALAMEDA_RECREO = "sa_alameda_recreo";
    public static final String SA_PROBLEMA_ALAMEDA = "sa_problema_alameda";
    public static final String SA_MEJORAR_IMAGEN = "sa_mejorar_imagen";
    public static final String LUGAR_ENCUESTA = "lugar_encuesta";
    public static final String FECHA = "fecha";
    public static final String STATUS = "status";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String COMENTARIOS = "comentarios";
    public static final String REFERENCIA_MOVIL = "referencia_movil";

    //TABLA DE INFORMACION COMPLEMENTARIA
    public static final String TABLE_INFORMACION_COMPLEMENTARIA = "informacion_complementaria";
    public static final String TELEFONO = "telefono";
    public static final String EMAIL = "correo";
    public static final String FACEBOOK = "facebook";
    public static final String TWITTER = "twitter";
    public static final String SEXO = "sexo";
    public static final String EDAD = "edad";
    public static final String FOTO = "foto";

    //TABLA DE COORDENADAS
    public static final String TABLE_COORDINATES = "coordenadas";
    public static final String ID_COORDS = "idcoords";
}
