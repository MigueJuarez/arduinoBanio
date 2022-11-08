package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.arduinobanio.R;

public class BanioDetalle extends AppCompatActivity {

    // String for MAC address del Hc05
    private static String address = null;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banio_detalle);
    }

    @Override
    //Cada vez que se detecta el evento OnResume se establece la comunicacion con el HC05, creando un
    //socketBluethoot
    public void onResume() {
        super.onResume();

        //Obtengo el parametro, aplicando un Bundle, que me indica la Mac Adress del HC05
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        address = extras.getString("MAC_HC05");

        BluetoothDevice device = btAdapter.getRemoteDevice(address);



    }

    public void iniciarLimpieza(View view) {
        Toast.makeText(view.getContext(), "Solicitud de limpieza aceptada",Toast.LENGTH_SHORT).show();
    }

    public void finalizarLimpieza(View view) {
        Toast.makeText(view.getContext(), "Limpieza finalizada",Toast.LENGTH_SHORT).show();
    }

    public void goToList(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }
}