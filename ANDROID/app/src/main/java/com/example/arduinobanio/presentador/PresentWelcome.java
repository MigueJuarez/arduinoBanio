package com.example.arduinobanio.presentador;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.modelo.ModelWelcome;

import java.util.ArrayList;
import java.util.Set;

public class PresentWelcome implements ContractWelcome.Presenter, ContractWelcome.Model.CallBackToView {

    private ContractWelcome.View mView;
    private ContractWelcome.Model model;


    public PresentWelcome (ContractWelcome.View pView) {
        mView = pView;
        model = new ModelWelcome(this);
    }

    @Override
    public void detectShake(SensorEvent sensorEvent, Sensor accelerometer) {
        model.detectShake(sensorEvent, accelerometer);
    }

    @Override
    public void isEnableBLT() {
         model.isEnableBLT();
    }


    @Override
    public void showMsg(String msg) {
        mView.showMsg(msg);
    }

    @Override
    public void goToMain() {
        mView.goToMain();
    }

    @Override
    public void setEnableBtns(boolean value) {
        mView.setEnableBtns(value);
    }
}
