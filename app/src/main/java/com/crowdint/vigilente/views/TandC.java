package com.crowdint.vigilente.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.crowdint.vigilente.ViewCamera;
import com.crowdint.vigilente.R;
import com.crowdint.vigilente.utilities.PreferencesManager;

/**
 * Created by DarkAbsolutE on 7/29/15.
 */
public class TandC extends Activity {

    private boolean checkCondition = false;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tandc);

        tandc();

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkCondition = isChecked;
            }
        });
    }

    public void nextCamera(View view) {
        check();
    }

    public void check() {
        if (checkCondition) {
            saveOption();
            changeView();
        } else {
            alertDialog();
        }
    }

    public void saveOption() {
        PreferencesManager.SavePreferences("t&c", "1", this);
    }

    public void changeView() {
        Intent intent = new Intent(this, ViewCamera.class);
        startActivity(intent);
        finish();
    }

    public void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Terminos & Condiciones");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Debe aceptar los terminos y condiciones para poder continuar");
        alertDialog.setPositiveButton("Ok", null);
        alertDialog.show();
    }

    public void tandc() {
        TextView textView = (TextView) findViewById(R.id.TandC);
        textView.setText("Hello world!");
    }
}
