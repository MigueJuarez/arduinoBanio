package com.example.arduinobanio.modelo;

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

    final int handlerState = 0;

    public ConnectedThread(BluetoothSocket socket, Handler handler )
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        bluetoothIn = handler;
    }

    public void run()
    {
        byte[] buffer = new byte[256];
        int bytes;

        while (true)
        {
            try
            {
                bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);

                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(ContractBanioDetalle.Model.CallBackToView cb, String input) {
        byte[] msgBuffer = input.getBytes();
        try {
            mmOutStream.write(msgBuffer);
        } catch (IOException e) {
            cb.showMsg("La conexion fallo");
            cb.finishView();
        }
    }
}
