package com.crowdint.vigilente.utilities;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class GPS {

    public static boolean gpsEnabled(Context context){
        LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return true;
        }
        else
            return false;
    }
}
