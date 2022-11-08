package com.example.arduinobanio;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

public interface ContractBanioDetalle {

    interface View {
        void actualizarEstado(String estado);

        void showMsg(String msg);

        void finishView();
    }

    interface Model {

        void establecerConexionDevice(ContractBanioDetalle.Model.CallBackToView cb, BluetoothDevice device);

        Handler Handler_Msg_Hilo_Principal (CallBackToView cb);

        interface CallBackToView {
            void actualizarEstado(String estado);

            void showMsg(String msg);

            void finishView();
        }

    }

    interface Presenter {
        void establecerConexionDevice(BluetoothDevice device);

        Handler Handler_Msg_Hilo_Principal();
    }

}
