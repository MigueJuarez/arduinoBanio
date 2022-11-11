package com.example.arduinobanio.presentador;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

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
    public void detectPairDevices() {
       boolean dispEmparejado =  model.detectPairDevices();
        if (dispEmparejado == true)
        {
            mView.showMsg("Se encontraron dispositivos emparejados");
        }
        else {
            mView.showMsg("No se encontraron dispositivos emparejados");
        }
    }

    @Override
    public void foundDevices( Intent intent) {
        model.foundDevices(intent);
    }

    @Override
    public void setDevicesFound(ArrayList<BluetoothDevice> pDeviceList) {
        mView.setDevicesFound(pDeviceList);
    }

}
