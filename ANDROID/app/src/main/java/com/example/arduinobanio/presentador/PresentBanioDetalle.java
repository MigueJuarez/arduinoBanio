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

    private final String COMANDO_INICIAR_LIMPIEZA = "I";
    private final String COMANDO_FINALIZAR_LIMPIEZA = "F";
    private final String COMANDO_OBTENER_ESTADO = "E";

    public PresentBanioDetalle(ContractBanioDetalle.View pView) {
        this.view = pView;
        this.model = new ModelBanioDetalle(this);
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
    public void sendIniciarLimpieza() {
        model.sendMsg(COMANDO_INICIAR_LIMPIEZA);
        this.view.showMsg("Iniciar limpieza enviado");
    }

    @Override
    public void sendFinalizarLimpieza() {
        model.sendMsg(COMANDO_FINALIZAR_LIMPIEZA);
        this.view.showMsg("Finalizar limpieza enviado");
    }

    @Override
    public void sendObtenerEstado() {
        model.sendMsg(COMANDO_OBTENER_ESTADO);
        this.view.showMsg("Obtener Estado enviado");
    }

    @Override
    public void finishView() {
        view.finishView();
    }

    @Override
    public Handler Handler_Msg_Hilo_Principal() {
        return model.Handler_Msg_Hilo_Principal();
    }

    @Override
    public void pedirPermisos(Activity activity) {
        if (Objects.nonNull(view)) {
            model.pedirPermisos(activity);
        }
    }

    @Override
    public void detectOnePairDevice() {
        BluetoothDevice device = model.detectOnePairDevice();
        if (device != null) {
            model.establecerConexionDevice(device);
        }
        else {
            this.view.showMsg("Debe emparejar solamente a 1 dispositivo");
        }

    }

}
