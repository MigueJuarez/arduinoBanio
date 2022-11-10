package com.example.arduinobanio.presentador;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.example.arduinobanio.ContractBanioDetalle;
import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.modelo.ModelBanioDetalle;

import java.util.Objects;

public class PresentBanioDetalle implements ContractBanioDetalle.Presenter,  ContractBanioDetalle.Model.CallBackToView  {

    private final ContractBanioDetalle.View view;
    private final ContractBanioDetalle.Model model;

    public PresentBanioDetalle(ContractBanioDetalle.View pView, ContractBanioDetalle.Model pModel) {
        this.view = pView;
        this.model = pModel;
    }

    @Override
    public void actualizarEstado(String estado) {
        view.actualizarEstado(estado);
    }

    @Override
    public void showMsg(String msg) {
        view.showMsg(msg);
    }

    @Override
    public void finishView() {
        view.finishView();
    }



    @Override
    public void establecerConexionDevice(BluetoothDevice device) {
        model.establecerConexionDevice(this, device);
    }

    @Override
    public Handler Handler_Msg_Hilo_Principal() {
        return model.Handler_Msg_Hilo_Principal(this);
    }

    @Override
    public void pedirPermisos(Activity activity) {
        if (Objects.nonNull(view)) {
            model.pedirPermisos(this, activity);
        }
    }

}
