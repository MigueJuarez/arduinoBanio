package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BanioDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banio_detalle);
    }

    public void iniciarLimpieza(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }

    public void finalizarLimpieza(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }

    public void goToList(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }
}