package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Bluetooth extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Spinner spinner = findViewById(R.id.spinner);

        String [] opciones = {"BT", "Wifi", "Radio"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);
    }

    public void goToWelcome(View view) {
        Intent goToWelcome = new Intent(this, Welcome.class);
        startActivity(goToWelcome);
    }
}