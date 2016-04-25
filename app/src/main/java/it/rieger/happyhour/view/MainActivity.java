package it.rieger.happyhour.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        Log.i("Test",BitmapLRUCache.BITMAP_LRU_CACHE.name());
    }
}
