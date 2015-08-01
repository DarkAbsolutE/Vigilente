package com.crowdint.vigilente;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdint.vigilente.utilities.Action;
import com.crowdint.vigilente.utilities.Captures;
import com.crowdint.vigilente.utilities.GPS;
// import com.crowdint.reportatesting.clases.GalleryAdapter;
import com.crowdint.vigilente.views.Tutorial;
import com.crowdint.vigilente.views.Menu;
import com.crowdint.vigilente.utilities.Graphics;
import com.crowdint.vigilente.utilities.PreferencesManager;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class ViewCamera extends Activity implements
        SurfaceHolder.Callback, Camera.PictureCallback, LocationListener {

    Camera.PictureCallback jpgCallBack;
    Camera camera;

    static ProgressDialog progressDialog;

    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    ImageButton delete1;
    ImageButton delete2;
    ImageButton delete3;
    ImageButton galeria;
    ImageButton btnCapture;
    ImageButton reporte;
    Location localizacion;
    LocationManager locationManager;
    ImageLoader imageLoader;
    RelativeLayout imagePreviews;
    TextView tvLimite;

    SlidingPaneLayout mPanes;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera);

        foto1 = (ImageView) findViewById(R.id.preview1);
        foto2= (ImageView) findViewById(R.id.preview2);
        foto3 = (ImageView) findViewById(R.id.preview3);
        delete1 = (ImageButton) findViewById(R.id.delete1);
        delete2 = (ImageButton) findViewById(R.id.delete2);
        delete3 = (ImageButton) findViewById(R.id.delete3);
        galeria = (ImageButton) findViewById(R.id.galeria);
        btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        mPanes = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        reporte = (ImageButton) findViewById(R.id.next);
        galeria = (ImageButton) findViewById(R.id.galeria);
        mPanes.setParallaxDistance(30);
        galeria = (ImageButton) findViewById(R.id.galeria);
        imagePreviews = (RelativeLayout) findViewById(R.id.imagePreviews);
        tvLimite = (TextView) findViewById(R.id.tvLimite);

        if (!PreferencesManager.ReadPreferences("Provider", this).equals(""))
            ((ImageButton) findViewById(R.id.mnuLogout)).setVisibility(View.VISIBLE);

        ConfiguraCamara();
        initImageLoader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // multiples fotos
        if (requestCode == 200 && resultCode == Activity.RESULT_OK){
            if (data.getBooleanExtra("fromGaleria",false)){
                startActivity(new Intent(this, ViewCaptureInfo.class));
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializaEstado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocalization();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocalization();
    }

    public void menu(View view) {
        if (mPanes.isOpen()) {
            mPanes.closePane();
        } else {
            mPanes.openPane();
        }
    }

    public void onClickMenuItem(View view){

        switch (view.getId()){
            case R.id.mnuSitio:
                mPanes.closePane();
                String url = "http://www.vigilente.mx";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;

            case R.id.mnuAbout:
                mPanes.closePane();
                Intent i = new Intent(this, Tutorial.class);
                startActivity(i);
                finish();
                break;

            default:
                mPanes.closePane();
                Intent I = new Intent(this, Menu.class);
                I.putExtra("Type", view.getId());
                startActivity(I);
                break;
        }

    }

    //</editor-fold>
//Opciones del menu inferior
    public void onClick(View view){
        progressDialog = ProgressDialog.show(this, null, null, true);
        switch (view.getId()){
            case R.id.next:
                startActivity(new Intent(this, ViewCaptureInfo.class));
                break;

            case R.id.galeria:
//                imagePreviews.setVisibility(view.GONE);
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                break;
        }
    }

    public void captura(View view ){
        btnCapture.setEnabled(false);
        galeria.setVisibility(View.INVISIBLE);
        camera.takePicture(null, null, jpgCallBack);
    }

    private void inicializaEstado(){
        inicializaLocalizacion();
        //Boton logout
        if (!PreferencesManager.ReadPreferences("Provider", this).equals(""))
            ((ImageButton) findViewById(R.id.mnuLogout)).setVisibility(View.VISIBLE);

        foto1.setImageResource(R.drawable.rectangle_icon_1);
        delete1.setVisibility(View.GONE);
        foto2.setImageResource(R.drawable.rectangle_icon_2);
        delete2.setVisibility(View.GONE);
        foto3.setImageResource(R.drawable.rectangle_icon_3);
        delete3.setVisibility(View.GONE);
        btnCapture.setEnabled(true);

        if (Captures.cantidad != 0) {
            galeria.setVisibility(View.INVISIBLE);
            reporte.setVisibility(View.VISIBLE);
        }
        else {
            galeria.setVisibility(View.VISIBLE);
            reporte.setVisibility(View.INVISIBLE);
        }

        for (int i =1;i<=3; i++){
            if(!Captures.getFoto(i).equals("")){
                switch (i){
                    case 1:
                        foto1.setImageBitmap(Graphics.imageResize(Captures.getFoto(1), 480, 480));
                        delete1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        foto2.setImageBitmap(Graphics.imageResize(Captures.getFoto(2), 480, 480));
                        delete2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        foto3.setImageBitmap(Graphics.imageResize(Captures.getFoto(3), 480, 480));
                        delete3.setVisibility(View.VISIBLE);
                        break;


                }
            }
        }


    }

    public void deleteImage(View view ){
        switch (view.getId()){
            case R.id.delete1:
                Captures.deleteFoto(1);
                foto1.setImageResource(R.drawable.rectangle_icon_1);
                delete1.setVisibility(view.GONE);
                camera.startPreview();
                btnCapture.setEnabled(true);
                tvLimite.setText("");
                break;
            case R.id.delete2:
                Captures.deleteFoto(2);
                foto2.setImageResource(R.drawable.rectangle_icon_2);
                delete2.setVisibility(view.GONE);
                camera.startPreview();
                btnCapture.setEnabled(true);
                tvLimite.setText("");
                break;
            case R.id.delete3:
                Captures.deleteFoto(3);
                foto3.setImageResource(R.drawable.rectangle_icon_3);
                delete3.setVisibility(view.GONE);
                camera.startPreview();
                btnCapture.setEnabled(true);
                tvLimite.setText("");
                break;
        }

        if (Captures.cantidad == 0) {
            galeria.setVisibility(View.VISIBLE);
            reporte.setVisibility(View.INVISIBLE);
        }


    };

    private void ConfiguraCamara(){
        surfaceView = (SurfaceView)findViewById(R.id.camera_preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpgCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                procesaImagenCapturada(data);
            }
        };
    }

    private void procesaImagenCapturada(byte[] data){
        File pictureFile = getOutputMediaFile(1);
        if (pictureFile == null){
            Log.d("TAG", "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);

            switch (Captures.nuevaFoto(pictureFile.toString())){
                case 1:
                    foto1.setImageBitmap(Graphics.imageResize(Captures.getFoto(1), 480, 480));
                    delete1.setVisibility(View.VISIBLE);
                    camera.startPreview();
                    break;
                case 2:
                    foto2.setImageBitmap(Graphics.imageResize(Captures.getFoto(2), 480, 480));
                    delete2.setVisibility(View.VISIBLE);
                    camera.startPreview();
                    break;
                case 3:
                    foto3.setImageBitmap(Graphics.imageResize(Captures.getFoto(3), 480, 480));
                    delete3.setVisibility(View.VISIBLE);
                    camera.startPreview();
                    break;
                default:
                    camera.stopPreview();
                    break;

            }
            if (Captures.cantidad < 3){
                btnCapture.setEnabled(true);
                camera.startPreview();
                reporte.setVisibility(View.VISIBLE);
            }
            else {
                camera.stopPreview();
                tvLimite.setText("Haz alcanzado el límite de fotos.");
            }

            fos.close();

        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    //<editor-fold desc="Acceso a la Camara">
    private void refreshCamera(){
        if (surfaceHolder.getSurface() == null){
            return;
        }
        try {
            camera.stopPreview();
        }
        catch (Exception e){

        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e){

        }
    }


    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Vigilente");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Vigilente", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            camera = camera.open();
        }
        catch (RuntimeException e){
            System.err.println(e);
        }
        Camera.Parameters param;
        param = camera.getParameters();
        int tSize = param.getSupportedPictureSizes().size()/2;
        int w = param.getSupportedPictureSizes().get(tSize).width;
        int h = param.getSupportedPictureSizes().get(tSize).height;
        param.setPictureSize(w, h);

        param.setRotation(90);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);

        try {
            param.setFocusMode(param.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(param);
        }
        catch (Exception e ){

        }

        try {
            camera.setPreviewDisplay(surfaceHolder);camera.startPreview();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera =null;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
    //</editor-fold>

    //<editor-fold desc="Localizacion">
    private void inicializaLocalizacion(){
        stopLocalization();
        if (GPS.gpsEnabled(this)){
            startGpsLocalization();
        }
        else {
            alertGPSDisable();
        }
    }

    private void startGpsLocalization(){
        if ( locationManager == null ) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        localizacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Captures.setLocalizacion(localizacion);
        // Iniciamos el proceso de localizacion
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000 * 60,20,this);

    }

    private void stopLocalization(){
        if (locationManager != null)
            locationManager.removeUpdates(this);
    }

    public void alertGPSDisable() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Importante");
        dialogo.setMessage("Es necesario tener activada la geolocalizacion...");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                iniciarActivarGPS();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                canceloActivarGPS();
            }
        });
        dialogo.show();
    }

    public void iniciarActivarGPS() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void canceloActivarGPS() {
        Toast t=Toast.makeText(this,"La geolocalizacion no ha sido activada.", Toast.LENGTH_SHORT);
        t.show();
    }
    @Override
    public void onLocationChanged(Location location) {
        localizacion = location;
        Captures.setLocalizacion(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        stopLocalization();
        startGpsLocalization();
    }

    @Override
    public void onProviderDisabled(String provider) {
        stopLocalization();
        startGpsLocalization();
    }
    //</editor-fold>

    static public void dismiss() {
        progressDialog.dismiss();
    }
}