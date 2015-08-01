package com.crowdint.vigilente;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by DarkAbsolutE on 7/29/15.
 */
public class Camera extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera);
    }
}
