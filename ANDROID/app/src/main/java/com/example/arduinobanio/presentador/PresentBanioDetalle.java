package com.example.arduinobanio.presentador;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.example.arduinobanio.ContractBanioDetalle;
import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.vista.ListDevices;

public class PresentBanioDetalle implements ContractBanioDetalle.Presenter,  ContractBanioDetalle.Model.CallBackToView  {

    private ContractBanioDetalle.View mView;
    private ContractBanioDetalle.Model model;

    public PresentBanioDetalle(ContractBanioDetalle.View pView, ContractBanioDetalle.Model pModel) {
        mView = pView;
        model = pModel;
    }

    @Override
    public void actualizarEstado(String estado) {
        mView.actualizarEstado(estado);
    }

    @Override
    public void showMsg(String msg) {
        mView.showMsg(msg);
    }

    @Override
    public void finishView() {
        mView.finishView();
    }

    @Override
    public void establecerConexionDevice(BluetoothDevice device) {
        model.establecerConexionDevice(this, device);
    }

    @Override
    public Handler Handler_Msg_Hilo_Principal() {
        return model.Handler_Msg_Hilo_Principal(this);
    }


}
