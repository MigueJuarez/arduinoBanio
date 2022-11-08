package com.example.arduinobanio.presentador;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.vista.ListDevices;

import java.lang.reflect.Method;

public class PresentPairDevices implements ContractPairDevices.Presenter, ContractPairDevices.Model.CallBackToView {

    private ListDevices mAdapter;
    private ContractPairDevices.View mView;
    private ContractPairDevices.Model model;

    public PresentPairDevices(ContractPairDevices.View pView, ContractPairDevices.Model pModel,ListDevices pAdapter) {
        mView = pView;
        model = pModel;
        mAdapter = pAdapter;

    }

    @Override
    public void decidePairOrUnpair(BluetoothDevice device, int position) {
        //Se checkea si el sipositivo ya esta emparejado
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            //Si esta emparejado,quiere decir que se selecciono desemparjar y entonces se le desempareja
            model.unpairDevice(device);
        } else {
            //Si no esta emparejado,quiere decir que se selecciono emparjar y entonces se le empareja
            mView.showMsg("Emparejando");

            mView.setPosicionListBluetooth(position);

            model.pairDevice(device);

        }
    }

    @Override
    public void checkPairDevice(Intent intent, int position, ListDevices mAdapter) {
        model.checkPairDevice(this, intent, position, mAdapter);
    }

    @Override
    public void goToWelcome(String direccionMAC) {
        mView.goToWelcome(direccionMAC);
    }

    @Override
    public void setPosicionListBluetooth(int position) {
        mView.setPosicionListBluetooth(position);
    }

    @Override
    public void showMsg(String msg) {

    }
}
