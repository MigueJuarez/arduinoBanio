package com.example.arduinobanio;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.arduinobanio.vista.ListDevices;

public interface ContractPairDevices {

    interface Presenter {
        void decidePairOrUnpair(BluetoothDevice device, int position);
        void checkPairDevice(Intent intent, int position, ListDevices mAdapter);

    }
    interface View {
        void goToWelcome(String direccionMAC);

        void setPosicionListBluetooth(int position);

        void showMsg(String msg);
    }

    interface Model {

        void pairDevice(BluetoothDevice device);

        void unpairDevice(BluetoothDevice device);

        void checkPairDevice(ContractPairDevices.Model.CallBackToView cb, Intent intent, int position, ListDevices mAdapter);

        interface CallBackToView {
            void goToWelcome(String direccionMAC);
            void setPosicionListBluetooth(int position);

            void showMsg(String msg);
        }

    }
}
