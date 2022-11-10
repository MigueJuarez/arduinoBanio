package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.arduinobanio.ContractBanioDetalle;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelBanioDetalle;
import com.example.arduinobanio.presentador.PresentBanioDetalle;

import java.util.Objects;

public class BanioDetalle extends AppCompatActivity implements ContractBanioDetalle.View {

    // String for MAC address del Hc05
    private static String address = null;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    private ContractBanioDetalle.Presenter presenter;

    private Handler bluetoothIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banio_detalle);
        presenter = new PresentBanioDetalle(this, new ModelBanioDetalle());
        presenter.pedirPermisos(this);
    }

    //Cada vez que se detecta el evento OnResume se establece la comunicacion con el HC05, creando un socketBluethoot
    @Override
    public void onResume() {
        super.onResume();

        // Obtengo el parametro, aplicando un Bundle, que me indica la Mac Adress del HC05
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (Objects.nonNull(extras)) {
            address = extras.getString("MAC_HC05");
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            presenter.establecerConexionDevice(device);
        }

        //defino el Handler de comunicacion entre el hilo Principal  el secundario.
        //El hilo secundario va a mostrar informacion al layout atraves utilizando indeirectamente a este handler
        bluetoothIn = presenter.Handler_Msg_Hilo_Principal();
        presenter.sendObtenerEstado();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void iniciarLimpieza(View view) {
        //Toast.makeText(view.getContext(), "Solicitud de limpieza aceptada",Toast.LENGTH_SHORT).show();
        presenter.sendIniciarLimpieza();
        //TODO FALTA ACTUALIZAR ESTADO EN LA VIEW
    }

    public void finalizarLimpieza(View view) {
        //Toast.makeText(view.getContext(), "Limpieza finalizada",Toast.LENGTH_SHORT).show();
        presenter.sendFinalizarLimpieza();
        //TODO FALTA ACTUALIZAR ESTADO EN LA VIEW
    }

    public void goToListaBanios(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }

    @Override
    public void actualizarEstado(String estado) {}

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void finishView() {
        finish();
    }


}