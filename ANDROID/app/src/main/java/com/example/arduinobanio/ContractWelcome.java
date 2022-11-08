package com.example.arduinobanio;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;

public interface ContractWelcome {

    interface View {
        void showMsg(String msg);
        void setDevicesFound(ArrayList<BluetoothDevice> pDeviceList);
    }

    interface Model {
        boolean detectPairDevices();

        void foundDevices(CallBackToView cb, Intent intent);

        interface CallBackToView {
            void setDevicesFound(ArrayList<BluetoothDevice> pDeviceList);
        }
    }

    interface Presenter {
        void detectPairDevices();
        void foundDevices(Intent intent);
    }
}
