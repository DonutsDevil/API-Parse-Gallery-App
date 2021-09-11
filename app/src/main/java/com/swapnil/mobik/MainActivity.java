package com.swapnil.mobik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GalleryFragment fragment = new GalleryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment,"Gallery Fragment")
                .commit();
    }
}