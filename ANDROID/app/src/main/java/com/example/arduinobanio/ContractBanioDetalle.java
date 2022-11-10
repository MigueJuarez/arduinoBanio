package com.example.arduinobanio;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

public interface ContractBanioDetalle {

    interface View {
        void actualizarEstado(String estado);
        void showMsg(String msg);
        void finishView();
    }

    interface Model {
        void establecerConexionDevice(CallBackToView cb, BluetoothDevice device);
        void pedirPermisos(CallBackToView cb, Activity activity);
        Handler Handler_Msg_Hilo_Principal (CallBackToView cb);
        void sendMsg(CallBackToView cb, String msg);
        interface CallBackToView {
            void actualizarEstado(String estado);
            void showMsg(String msg);
            void finishView();
        }
    }

    interface Presenter {
        void establecerConexionDevice(BluetoothDevice device);
        void showMsg(String msg);
        Handler Handler_Msg_Hilo_Principal();
        void pedirPermisos(Activity activity);
        void sendObtenerEstado();
        void sendIniciarLimpieza();
        void sendFinalizarLimpieza();
    }


}
