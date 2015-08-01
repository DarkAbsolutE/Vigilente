package com.crowdint.vigilente.utilities;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class TypeFace {

    public static void setTypeFace(int r,Context context, float size){
        android.graphics.Typeface typeface = android.graphics.Typeface.createFromAsset(context.getAssets(),"visitor2.ttf");
        Activity activity = (Activity)context;
        ((TextView) activity.findViewById(r)).setTypeface(typeface);
        ((TextView) activity.findViewById(r)).setTextSize(size);
    }
}
