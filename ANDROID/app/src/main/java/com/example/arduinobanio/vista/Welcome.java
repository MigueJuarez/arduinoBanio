package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;

import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelWelcome;
import com.example.arduinobanio.presentador.PresentWelcome;

public class Welcome extends AppCompatActivity implements ContractWelcome.View, SensorEventListener {

    private TextView textTitulo;
    private TextView textPresentacion;
    private Button btnVerBanios;

    private ContractWelcome.Presenter presenter;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textTitulo = (TextView) findViewById(R.id.textView3);
        textPresentacion = (TextView) findViewById(R.id.textView4);
        btnVerBanios = (Button) findViewById(R.id.verBanios);

        btnVerBanios.setOnClickListener(btnListener);

        presenter = new PresentWelcome(this);

        startSensor();

    }

    private void startSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }


        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),2 );
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        presenter.isEnableBLT();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.verBanios:
                    goToListaBanios(view);
                    break;
            }
        }
    };

    public void goToListaBanios(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        startActivity(goToList);
    }

    @Override
    public void goToMain() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }

    @Override
    public void setEnableBtns(boolean value) {
        btnVerBanios.setEnabled(value);
        if (value){
            btnVerBanios.setTextColor(Color.WHITE);
        } else {
            btnVerBanios.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void showMsg (String msge) {
        showToast(msge);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        presenter.detectShake(sensorEvent, accelerometer);
    }

}