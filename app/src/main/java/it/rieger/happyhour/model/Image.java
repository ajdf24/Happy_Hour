package it.rieger.happyhour.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * container class for a bitmap image
 * Created by sebastian on 11.12.16.
 */

public class Image implements Serializable{

    String image = null;

    public Image() {
    }

    public Bitmap getImage() {

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return bitmap;
    }

    public void setBitmapToImage(Bitmap image) {

        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        this.image = imageFile;
    }

    public void setImage(String image) {


        this.image = image;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("image", image);

        return result;
    }
}
