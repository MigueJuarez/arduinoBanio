package com.example.arduinobanio.modelo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.vista.ListDevices;

import java.util.ArrayList;
import java.util.Set;

public class ModelWelcome implements ContractWelcome.Model {


    @Override
    public boolean detectPairDevices() {
        //Se crea un adaptador para poder manejar el bluethoot del celular
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices == null || pairedDevices.size() == 0)
        {
            return false;
        }
        else{
            return true;
        }

    }

    @Override
    public void foundDevices(CallBackToView cb, Intent intent) {
        //Atraves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
        String action = intent.getAction();
        ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
        {
            cb.setDevicesFound(mDeviceList);
        }
        //si se encontro un dispositivo bluethoot
        else if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            //Se lo agregan sus datos a una lista de dispositivos encontrados
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            mDeviceList.add(device);
        }
    }
}
