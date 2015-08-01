package com.crowdint.vigilente.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.crowdint.vigilente.ViewCamera;
import com.crowdint.vigilente.R;
import com.crowdint.vigilente.utilities.PreferencesManager;

/**
 * Created by DarkAbsolutE on 7/29/15.
 */
public class Tutorial extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tutorial);
    }

    public void nextView(View view) {
        check();
    }

    public void check() {
        if (checkNextView()) {
            saveOption();
            changeViewCamera();
        } else {
            changeViewTandC();
        }
    }

    public void saveOption() {
        PreferencesManager.SavePreferences("tutorial", "1", this);
    }

    public void changeViewTandC() {
        Intent intent = new Intent(this, TandC.class);
        startActivity(intent);
        finish();
    }

    public void changeViewCamera() {
        Intent intent = new Intent(this, ViewCamera.class);
        startActivity(intent);
        finish();
    }

    public boolean checkNextView() {
        //Check for change to view tutorial and not show the view t&c
        if (PreferencesManager.ReadPreferences("t&c", this).equals("1")) {
            return true;
        }
        //Check for change to view tutorial and show the view t&c
        else {
            return false;
        }
    }
}
