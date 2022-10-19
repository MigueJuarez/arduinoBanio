package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Metodo boton siguiente
    public void goToWelcome(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }
}