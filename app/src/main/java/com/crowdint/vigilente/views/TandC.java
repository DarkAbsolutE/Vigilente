package com.crowdint.vigilente.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.crowdint.vigilente.R;

/**
 * Created by DarkAbsolutE on 7/29/15.
 */
public class TandC extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tandc);
    }
}
