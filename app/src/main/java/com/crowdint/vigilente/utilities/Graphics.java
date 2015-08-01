package com.crowdint.vigilente.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Base64;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by DarkAbsolutE on 8/1/15.
 */
public class Graphics {

    private static ImageLoader imageLoader;
    // Procesa la imagen para cambiarle el tamaño
    public static String imageToBase64(String imagen, int _width, int _height){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap photobmp1 = imageLoad(imagen, _width, _height);

        photobmp1.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    // Procesa la imagen para cambiarle el tamaño
    public static Bitmap imageResize(String imagen, int _width, int _height){

        return imageLoad(imagen, _width, _height);
    }

    public static Bitmap imageLoad(String image, int _width, int _height){

        File f = new File(image);
        ByteArrayOutputStream baos=null;
        Bitmap b=null;
        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fileInputStream =  new FileInputStream(f);
            BitmapFactory.decodeStream(fileInputStream,null,o);
            fileInputStream.close();

            float sc = 0.0f;
            int scale = 1;

            if (o.outHeight > o.outWidth){
                sc = o.outHeight/_height;
                scale = Math.round(sc);
            }
            else {
                sc = o.outWidth/_width;
                scale = Math.round(sc);
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fileInputStream = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fileInputStream,null,o2);

            baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG,80,baos);
            //return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
