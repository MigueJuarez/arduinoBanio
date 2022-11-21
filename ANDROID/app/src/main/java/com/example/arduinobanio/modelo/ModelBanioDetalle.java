package com.example.arduinobanio.modelo;

import static com.example.arduinobanio.vista.MainActivity.DEBUGGER_ENABLED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.arduinobanio.ContractBanioDetalle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ModelBanioDetalle implements ContractBanioDetalle.Model {

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    private final String COMANDO_ESTADO_LIBRE = "L";
    private final String COMANDO_ESTADO_OCUPADO = "O";
    private final String COMANDO_ESTADO_SOLICITUD_LIMPIEZA = "S";
    private final String COMANDO_ESTADO_PENDIENTE_LIMPIEZA = "P";
    private final String COMANDO_ESTADO_EN_LIMPIEZA = "E";

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    final int handlerState = 0;

    private CallBackToView callBackToViewPresenter;

    public ModelBanioDetalle(CallBackToView pcb) {
        this.callBackToViewPresenter = pcb;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void establecerConexionDevice(BluetoothDevice device) {

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            callBackToViewPresenter.showMsg( "La creacción del Socket fallo");
        }

        try {
            if (!btSocket.isConnected()) {
                btSocket.connect();
            }
            callBackToViewPresenter.showMsg( "La conexion del Socket se realizo correctamente");

            startComunicacion();
        } catch (IOException e) {
            callBackToViewPresenter.showMsg( "Verificar sincronizacion con el disp. Bluetooth");
            try {
                btSocket.close();
            } catch (IOException e2) {

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void pedirPermisos(Activity activity) {

        String[] permissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permiso : permissions) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext() , permiso) != PackageManager.PERMISSION_GRANTED) {

                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Permiso " + permiso +  " no aceptado por el momento");
                }
                listPermissionsNeeded.add(permiso);
            } else {

                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Ya tenemos permiso de " + permiso);
                }
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso)) {

                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Permiso " + permiso + "debe otorgarse desde Ajustes");
                }
            }
        }

        if (listPermissionsNeeded.size() > 0){

            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 123);
        }

    }

    private void startComunicacion() {
        Handler hand = Handler_Msg_Hilo_Principal();
        mConnectedThread = new ConnectedThread(btSocket, hand);
        mConnectedThread.start();
    }

    public void sendMsg(String msg) {
        mConnectedThread.write(callBackToViewPresenter, msg);
    }

    @Override
    public void deleteSocket() {
        this.cerrarConexion();
    }

    public void cerrarConexion() {
        try {
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private Handler Handler_Msg_Hilo_Principal ()
    {
        return new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg)
            {
                if (msg.what == handlerState)
                {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    String dataInPrint = "";
                    if (COMANDO_ESTADO_LIBRE.equals(readMessage)) {
                        dataInPrint = "Libre";
                    } else if (COMANDO_ESTADO_OCUPADO.equals(readMessage)) {
                        dataInPrint = "Ocupado";
                    } else if (COMANDO_ESTADO_SOLICITUD_LIMPIEZA.equals(readMessage)){
                        dataInPrint = "Pendiente";
                    } else if (COMANDO_ESTADO_PENDIENTE_LIMPIEZA.equals(readMessage)){
                        dataInPrint = "Pendiente";
                    } else if (COMANDO_ESTADO_EN_LIMPIEZA.equals(readMessage)){
                        dataInPrint = "En Limpieza";
                    }

                    callBackToViewPresenter.actualizarEstado(dataInPrint);
                    recDataString.delete(0, recDataString.length());
                }
            }
        };
    }

    @Override
    public BluetoothDevice detectOnePairDevice() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices != null)
        {
            if (pairedDevices.size() == 1) {
                return pairedDevices.iterator().next();
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }

    }

}
