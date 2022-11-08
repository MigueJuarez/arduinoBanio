package com.example.arduinobanio.modelo;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.vista.ListDevices;
import com.example.arduinobanio.vista.PairDevices;
import com.example.arduinobanio.vista.Welcome;

import java.lang.reflect.Method;

public class ModelPairDevices implements ContractPairDevices.Model {

    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkPairDevice(CallBackToView cb, Intent intent, int posicionListBluethoot, ListDevices mAdapter) {
        //Atraves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
        String action = intent.getAction();

        //si el SO detecto un emparejamiento o desemparjamiento de bulethoot
        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
        {
            //Obtengo los parametro, aplicando un Bundle, que me indica el estado del Bluethoot
            final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            //se analiza si se puedo emparejar o no
            if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING)
            {
                //Si se detecto que se puedo emparejar el bluethoot
                cb.showMsg("Emparejado");

                BluetoothDevice dispositivo = (BluetoothDevice) mAdapter.getItem(posicionListBluethoot);

                //se inicia el Activity de comunicacion con el bluethoot, para transferir los datos.
                //Para eso se le envia como parametro la direccion(MAC) del bluethoot Arduino
                String direccionBluethoot = dispositivo.getAddress();


            }  //si se detrecto un desaemparejamiento
            else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                cb.showMsg("o emparejado");
            }

            mAdapter.notifyDataSetChanged();
        }
    }
}
