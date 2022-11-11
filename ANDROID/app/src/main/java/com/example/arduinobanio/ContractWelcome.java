package com.example.arduinobanio;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.ArrayList;

public interface ContractWelcome {

    interface View {
        void showMsg(String msg);
        void goToMain();
        void setEnableBtns(boolean value);
    }

    interface Model {
        void detectShake(SensorEvent sensorEvent, Sensor accelerometer);
        void isEnableBLT();

        interface CallBackToView {
            void showMsg(String msg);
            void goToMain();
            void setEnableBtns(boolean value);
        }
    }

    interface Presenter {
        void detectShake(SensorEvent sensorEvent, Sensor accelerometer);
        void isEnableBLT();
    }
}
