package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void goToBT(View view) {
        Intent goToBT = new Intent(this, Bluetooth.class);
        startActivity(goToBT);
    }

    public void goToList(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }
}