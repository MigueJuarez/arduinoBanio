package com.example.arduinobanio.modelo;


//******************************************** Hilo secundario del Activity**************************************
//*************************************** recibe los datos enviados por el HC05**********************************

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.example.arduinobanio.ContractBanioDetalle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread
{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private Handler bluetoothIn;

    final int handlerState = 0; //used to identify handler message

    //Constructor de la clase del hilo secundario
    public ConnectedThread(BluetoothSocket socket, Handler handler )
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try
        {
            //Create I/O streams for connection
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.i("Error","error");
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        bluetoothIn = handler;
    }

    //metodo run del hilo, que va a entrar en una espera activa para recibir los msjs del HC05
    public void run()
    {
        byte[] buffer = new byte[256];
        int bytes;

        //el hilo secundario se queda esperando mensajes del HC05
        while (true)
        {
            try
                {
                    //se leen los datos del Bluethoot
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);

                //se muestran en el layout de la activity, utilizando el handler del hilo
                // principal antes mencionado
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }


    //write method
    public void write(ContractBanioDetalle.Model.CallBackToView cb, String input) {
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
        } catch (IOException e) {
            //if you cannot write, close the application
            cb.showMsg("La conexion fallo");
            cb.finishView();
        }
    }
}
