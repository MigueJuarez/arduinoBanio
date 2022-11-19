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

    // SPP UUID service  - Funciona en la mayoria de los dispositivos

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    final int handlerState = 0; //used to identify handler message

    private CallBackToView callBackToViewPresenter;

    public ModelBanioDetalle(CallBackToView pcb) {
        this.callBackToViewPresenter = pcb;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void establecerConexionDevice(BluetoothDevice device) {

        //se realiza la conexion del Bluethoot crea y se conectandose a atraves de un socket
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            callBackToViewPresenter.showMsg( "La creacción del Socket 1 fallo");
        }
        // Establish the Bluetooth socket connection.
        try {
            if (!btSocket.isConnected()) {
                btSocket.connect();
            }
            callBackToViewPresenter.showMsg( "La conexion del Socket 2 se realizo correctamente");
        } catch (IOException e) {
            callBackToViewPresenter.showMsg( "La conexion del Socket 2 fallo");
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
                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Permiso " + permiso +  " no aceptado por el momento");
                }
                listPermissionsNeeded.add(permiso); // agrego el permiso para hacer el requestPermissions
            } else {
                // Ya tenemos los permisos
                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Ya tenemos permiso de " + permiso);
                }
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso)) {
                // Permisos rechazados, aceptarlos desde ajustes
                if (DEBUGGER_ENABLED){
                    callBackToViewPresenter.showMsg("Permiso " + permiso + "debe otorgarse desde Ajustes");
                }
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
        Handler hand = Handler_Msg_Hilo_Principal();
        mConnectedThread = new ConnectedThread(btSocket, hand);
        mConnectedThread.start();
    }

    public void sendMsg(String msg) {
        mConnectedThread.write(callBackToViewPresenter, msg);
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
    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    //Handler que permite mostrar datos en el Layout al hilo secundario
    private Handler Handler_Msg_Hilo_Principal ()
    {
        return new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg)
            {
                //si se recibio un msj del hilo secundario
                if (msg.what == handlerState)
                {
                    //voy concatenando el msj
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    String dataInPrint = "";
                    if ("L".equals(readMessage)) {
                        dataInPrint = "Libre";
                    } else if ("O".equals(readMessage)) {
                        dataInPrint = "Ocupado";
                    } else if ("S".equals(readMessage)){
                        dataInPrint = "Pendiente";
                    } else if ("P".equals(readMessage)){
                        dataInPrint = "Pendiente";
                    } else if ("E".equals(readMessage)){
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
        //Se crea un adaptador para poder manejar el bluethoot del celular
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
