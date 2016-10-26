package com.gob.pgutierrezd.e_personas.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pgutierrezd on 17/10/2016.
 */
public class Constants {

    //Google Maps API
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final LatLng QUERETARO = new LatLng(20.5872194, -100.387161);

    //Facebook API
    public static final String[] PERMISSIONS_FACEBOOK = new String[]{
            "email",
            "public_profile"
    };

}
