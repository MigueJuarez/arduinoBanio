package com.example.arduinobanio.modelo;

import static com.example.arduinobanio.vista.MainActivity.DEBUGGER_ENABLED;

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

    private static final float SHAKE_THRESHOLD = 5.25f;
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
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null)
        {

            if (mBluetoothAdapter.isEnabled())
            {
                if (DEBUGGER_ENABLED){
                    this.callBackToViewPresenter.showMsg("El bluetooth se encuentra habilitado");
                }
                this.callBackToViewPresenter.setEnableBtns(true);
            }
            else
            {
                if (DEBUGGER_ENABLED){
                    this.callBackToViewPresenter.showMsg("Bluetooth deshabilitado. Se requiere activación");
                }
                this.callBackToViewPresenter.setEnableBtns(false);
            }
        }
    }

}
