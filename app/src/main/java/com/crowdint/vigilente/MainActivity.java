package com.crowdint.vigilente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.crowdint.vigilente.utilities.PreferencesManager;
import com.crowdint.vigilente.views.TandC;
import com.crowdint.vigilente.views.Tutorial;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        changeView();
    }

    //Change the view
    public void changeView() {
        //Change to view camera, report or reports
        if (check() == 3) {
            Intent intent = new Intent(this, Camera.class);
            startActivity(intent);
            finish();
        }
        //Change to view t&c and not show the view tutorial
        else if (check() == 2) {
            Intent intent = new Intent(this, TandC.class);
            startActivity(intent);
            finish();
        }
        //Change to view tutorial and not show the view t&c
        else if (check() == 1) {
            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);
            finish();
        }
        //Change to view tutorial and show the view t&c
        else if (check() == 0) {
            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);
            finish();
        }
        //Show a error message to read preferences for the change the view
        else {
            Toast.makeText(this, "Error to read preferences", Toast.LENGTH_SHORT).show();
        }
    }
    //Check preferences for change the view next
    public int check() {
        //Check for change to view camera, report or reports
        if (PreferencesManager.ReadPreferences("tutorial", this).equals("1") && PreferencesManager.ReadPreferences("t&c", this).equals("1")) {
            return 3;
        }
        //Check for change to view t&c and not show the view tutorial
        else if (PreferencesManager.ReadPreferences("tutorial", this).equals("1")) {
            return 1;
        }
        //Check for change to view tutorial and not show the view t&c
        else if (PreferencesManager.ReadPreferences("t&c", this).equals("1")) {
            return 2;
        }
        //Check for change to view tutorial and show the view t&c
        else {
            return 0;
        }
    }
}
