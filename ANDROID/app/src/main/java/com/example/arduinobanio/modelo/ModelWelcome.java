package com.example.arduinobanio.modelo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.arduinobanio.ContractWelcome;

import java.util.ArrayList;
import java.util.Set;

public class ModelWelcome implements ContractWelcome.Model {

    private CallBackToView callBackToViewPresenter;

    private static final float SHAKE_THRESHOLD = 5.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;


    public ModelWelcome(CallBackToView pcb) {
        this.callBackToViewPresenter = pcb;
    }

    @Override
    public void detectShake(SensorEvent sensorEvent, Sensor accelerometer) {
        if (sensorEvent.sensor.getName().equals(accelerometer.getName())) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                //this.showMsg("Acceleration is " + acceleration + "m/s^2");

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    this.callBackToViewPresenter.showMsg("Usuario deslogueado");
                    this.callBackToViewPresenter.goToMain();
                }
            }
        }
    }

    @Override
    public void isEnableBLT() {
        //Se crea un adaptador para poder manejar el bluethoot del celular
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //se determina si existe bluethoot en el celular
        if (mBluetoothAdapter != null)
        {
            //si el celular soporta bluethoot, se definen los listener para los botones de la activity

            //se determina si esta activado el bluethoot
            if (mBluetoothAdapter.isEnabled())
            {
                this.callBackToViewPresenter.showMsg("El bluetooth se encuentra habilitado");
                this.callBackToViewPresenter.setEnableBtns(true);
            }
            else
            {
                this.callBackToViewPresenter.showMsg("Bluetooth deshabilitado. Se requiere activación");
                this.callBackToViewPresenter.setEnableBtns(false);
            }
        }
    }

}
