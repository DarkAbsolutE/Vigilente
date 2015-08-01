package com.crowdint.vigilente.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.crowdint.vigilente.R;
import com.crowdint.vigilente.utilities.Captures;
// import com.crowdint.reportatesting.clases.TypeFace;
import com.crowdint.vigilente.utilities.PreferencesManager;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class Menu extends Activity {

    private static final int PARALLAX_SIZE = 30;
    private SlidingPaneLayout mPanes;

    RelativeLayout acerca;
    RelativeLayout delito;
    RelativeLayout politicas;

    ImageButton aceptarTerminos;
    CheckBox checkBox;

    Bundle type;


    ImageButton authButton_menu;
    ImageButton loginButton_menu;

    Intent intent;

    int seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        intent = new Intent();

        mPanes = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        mPanes.setParallaxDistance(PARALLAX_SIZE);

        /*delito = (RelativeLayout) findViewById(R.id.includeDelito);
        politicas = (RelativeLayout) findViewById(R.id.includePoliticas);
        aceptarTerminos = (ImageButton) findViewById(R.id.aceptarTerminos);
        checkBox = (CheckBox) findViewById(R.id.checkBox);*/


        type = getIntent().getExtras();
        seleccion = type.getInt("Type");
        onSeleccionRecibida(seleccion);

        if (!PreferencesManager.ReadPreferences("Provider", this).equals(""))
            ((ImageButton) findViewById(R.id.mnuLogout)).setVisibility(View.VISIBLE);

        //TypeFace.setTypeFace(R.id.delitosTitulo, this, 36);
    }

    // Adapter para las opcioens del mennu de Cristian
    private void  onSeleccionRecibida(int sel){
        switch (sel){
            case R.id.mnuAbout:
                acerca.setVisibility(View.VISIBLE);
                break;
            case R.id.mnuDelito:
                delito.setVisibility(View.VISIBLE);
                break;
            case R.id.mnuReportar:
                mPanes.closePane();
                finish();
                break;
            case R.id.mnuSitio:
                mPanes.closePane();
                String url = "http://www.vigilente.mx";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
                break;
            case R.id.mnuTerminos:
                mPanes.closePane();
                politicas.setVisibility(View.VISIBLE);
                aceptarTerminos.setVisibility(View.INVISIBLE);
                checkBox.setVisibility(View.GONE);
                break;

        }
    }


    public void onClickMenuItem(View view){

        switch (view.getId()){
            case R.id.mnuAbout:
                mPanes.closePane();
                startActivity(new Intent(this, Tutorial.class));
                finish();
                break;

            case R.id.mnuDelito:
                mPanes.closePane();
                delito.setVisibility(View.VISIBLE);
                politicas.setVisibility(View.INVISIBLE);
                aceptarTerminos.setVisibility(View.INVISIBLE);
                break;

            case R.id.mnuReportar:
                mPanes.closePane();
                finish();
                break;

            case R.id.mnuSitio:
                mPanes.closePane();
                String url = "http://www.vigilente.mx";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
                break;

            case R.id.mnuTerminos:
                mPanes.closePane();
                politicas.setVisibility(View.VISIBLE);
                aceptarTerminos.setVisibility(View.INVISIBLE);
                //((CheckBox)findViewById(R.id.checkBox)).setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void menu(View view) {
        if (mPanes.isOpen()) {
            mPanes.closePane();
        } else {
            mPanes.openPane();
        }
    }

    public void reportar(View view) {
        mPanes.closePane();
        finish();
    }

    public void sitio(View view) {
        mPanes.closePane();
    }
}