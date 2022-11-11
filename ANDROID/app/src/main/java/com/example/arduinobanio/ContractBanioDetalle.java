package com.example.arduinobanio;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

public interface ContractBanioDetalle {

    interface View {
        void actualizarEstado(String estado);
        void showMsg(String msg);
        void finishView();
        void disableBtns();
    }

    interface Model {
        BluetoothDevice detectOnePairDevice();
        void establecerConexionDevice( BluetoothDevice device);
        void pedirPermisos( Activity activity);
        Handler Handler_Msg_Hilo_Principal ();
        void sendMsg(String msg);

        interface CallBackToView {
            void actualizarEstado(String estado);
            void showMsg(String msg);
            void finishView();
        }
    }

    interface Presenter {
        void showMsg(String msg);
        Handler Handler_Msg_Hilo_Principal();
        void pedirPermisos(Activity activity);
        void sendObtenerEstado();
        void sendIniciarLimpieza();
        void sendFinalizarLimpieza();
        void detectOnePairDevice();
    }


}
