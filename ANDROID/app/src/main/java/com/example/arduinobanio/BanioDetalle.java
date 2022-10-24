package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BanioDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banio_detalle);
    }

    public void iniciarLimpieza(View view) {
        Toast.makeText(view.getContext(), "Solicitud de limpieza aceptada",Toast.LENGTH_SHORT).show();
    }

    public void finalizarLimpieza(View view) {
        Toast.makeText(view.getContext(), "Limpieza finalizada",Toast.LENGTH_SHORT).show();
    }
}