package com.example.arduinobanio.modelo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.arduinobanio.ContractBanioDetalle;
import com.example.arduinobanio.presentador.PresentBanioDetalle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ModelBanioDetalle implements ContractBanioDetalle.Model  {

    private ContractBanioDetalle.Presenter presenter;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service  - Funciona en la mayoria de los dispositivos
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    final int handlerState = 0; //used to identify handler message

    public ModelBanioDetalle(ContractBanioDetalle.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void establecerConexionDevice(CallBackToView cb, BluetoothDevice device) {

        //se realiza la conexion del Bluethoot crea y se conectandose a atraves de un socket
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            cb.showMsg( "La creacción del Socket fallo");
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }

        startComunicacion();
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
                // Permiso no aceptado por el momento
                presenter.showMsg("Permiso " + permiso +  " no aceptado por el momento");
                listPermissionsNeeded.add(permiso); // agrego el permiso para hacer el requestPermissions
            } else {
                // Ya tenemos los permisos
                presenter.showMsg("Ya tenemos permiso de " + permiso);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso)) {
                // Permisos rechazados, aceptarlos desde ajustes
                presenter.showMsg("Permiso " + permiso + "debe otorgarse desde Ajustes");
            }
        }

        if (listPermissionsNeeded.size() > 0){
            // Solicito los permisos que me faltan
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 123);
        }

        /*
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext() , Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Permiso no aceptado por el momento
            presenter.showMsg("Permiso no aceptado por el momento");
        } else {
            // Ya tenemos los permisos
            presenter.showMsg("Ya tenemos los permisos");
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH)) {
            // Permisos rechazados, aceptarlos desde ajustes
        } else {
            ActivityCompat.requestPermissions(activity, Arrays.asList(Manifest.permission.BLUETOOTH).toArray(new String[1]), 123);
        }*/
    }



    private void startComunicacion() {
        //Una establecida la conexion con el Hc05 se crea el hilo secundario, el cual va a recibir
        // los datos de Arduino atraves del bluethoot
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    public void sendMsge(String msg, CallBackToView cb) {
        mConnectedThread.write(msg, cb);
    }

    public void cerrarConexion() {
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Metodo que crea el socket bluethoot
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    //Handler que permite mostrar datos en el Layout al hilo secundario
    public Handler Handler_Msg_Hilo_Principal (CallBackToView cb)
    {
        return new Handler(){
            public void handleMessage(android.os.Message msg)
            {
                //si se recibio un msj del hilo secundario
                if (msg.what == handlerState)
                {
                    //voy concatenando el msj
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("\r\n");

                    //cuando recibo toda una linea la muestro en el layout
                    if (endOfLineIndex > 0)
                    {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);

                        cb.actualizarEstado(dataInPrint);

                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };
    }

}
