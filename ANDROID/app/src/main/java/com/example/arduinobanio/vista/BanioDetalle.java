package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arduinobanio.ContractBanioDetalle;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelBanioDetalle;
import com.example.arduinobanio.presentador.PresentBanioDetalle;

import java.util.Objects;

public class BanioDetalle extends AppCompatActivity implements ContractBanioDetalle.View {

    private ContractBanioDetalle.Presenter presenter;

    private Button btnIniciarLimpieza;
    private Button btnFinalizarLimpieza;
    private TextView txtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banio_detalle);
        presenter = new PresentBanioDetalle(this);
        presenter.pedirPermisos(this);

        btnIniciarLimpieza = (Button) findViewById(R.id.button7);
        btnFinalizarLimpieza = (Button) findViewById(R.id.button6);

        txtEstado = (TextView) findViewById(R.id.textView5);

        presenter.detectOnePairDevice();
        presenter.sendObtenerEstado();
    }

    //Cada vez que se detecta el evento OnResume se establece la comunicacion con el HC05, creando un socketBluethoot
    @Override
    public void onResume() {
        super.onResume();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void iniciarLimpieza(View view) {
        presenter.sendIniciarLimpieza();
        presenter.sendObtenerEstado();
    }

    public void finalizarLimpieza(View view) {
        presenter.sendFinalizarLimpieza();
        presenter.sendObtenerEstado();
    }

    public void goToListaBanios(View view) {
        presenter.deleteSocket();
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }

    @Override
    public void actualizarEstado(String estado) {
        txtEstado.setText(estado);
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void disableBtns() {
        btnIniciarLimpieza.setEnabled(false);
        btnIniciarLimpieza.setTextColor(Color.GRAY);
        btnFinalizarLimpieza.setEnabled(false);
        btnFinalizarLimpieza.setTextColor(Color.GRAY);
    }

    @Override
    public void enableBtns() {
        btnIniciarLimpieza.setEnabled(true);
        btnIniciarLimpieza.setTextColor(Color.WHITE);
        btnFinalizarLimpieza.setEnabled(true);
        btnFinalizarLimpieza.setTextColor(Color.WHITE);
    }

    @Override
    public void onDestroy() {
        presenter.deleteSocket();
        super.onDestroy();
    }
}