package com.example.arduinobanio.vista;


import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;

import android.content.Intent;

import android.widget.ListView;
import android.widget.Toast;

import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelPairDevices;
import com.example.arduinobanio.presentador.PresentPairDevices;

public class PairDevices extends Activity implements ContractPairDevices.View {
    private ListView mListView;
    public ListDevices mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private int posicionListBluethoot;
    private ContractPairDevices.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pair_devices);

        //defino los componentes de layout
        mListView = (ListView) findViewById(R.id.lv_paired);

        //obtengo por medio de un Bundle del intent la lista de dispositivos encontrados
        mDeviceList = getIntent().getExtras().getParcelableArrayList("device.list");

        //defino un adaptador para el ListView donde se van mostrar en la activity los dispositovs encontrados
        mAdapter = new ListDevices(this);

        //asocio el listado de los dispositovos pasado en el bundle al adaptador del Listview
        mAdapter.setData(mDeviceList);

        //defino un listener en el boton emparejar del listview
        mAdapter.setListener(listenerBotonEmparejar);
        mListView.setAdapter(mAdapter);

        presenter = new PresentPairDevices(this, new ModelPairDevices(), mAdapter);
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //Metodo que actua como Listener de los eventos que ocurren en los componentes graficos de la activty
    private ListDevices.OnPairButtonClickListener listenerBotonEmparejar = new ListDevices.OnPairButtonClickListener() {
        @Override
        public void onPairButtonClick(int position) {
            //Obtengo los datos del dispostivo seleccionado del listview por el usuario
            BluetoothDevice device = mDeviceList.get(position);

            presenter.decidePairOrUnpair(device, position);

        }
    };

    //Handler que captura los brodacast que emite el SO al ocurrir los eventos del bluethoot
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            presenter.checkPairDevice(intent, posicionListBluethoot, mAdapter);
        }
    };

    @Override
    public void setPosicionListBluetooth(int position) {
        posicionListBluethoot = position;
    }

    @Override
    public void goToWelcome(String direccionMAC) {
        Intent i = new Intent(PairDevices.this, Welcome.class);
        i.putExtra("MAC_HC05", direccionMAC);

        startActivity(i);
    }

    @Override
    public void showMsg (String msge) {

        showToast(msge);
    }



}
