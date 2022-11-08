package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.widget.TextView;

import com.example.arduinobanio.ContractPairDevices;
import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelPairDevices;
import com.example.arduinobanio.modelo.ModelWelcome;
import com.example.arduinobanio.presentador.PresentPairDevices;
import com.example.arduinobanio.presentador.PresentWelcome;

public class Welcome extends AppCompatActivity implements ContractWelcome.View {

    private TextView textTitulo;
    private TextView textPresentacion;
    private Button btnEmparejar; //seria para ver los dispositivos emparejados
    private Button btnVerBanios;

    // String for MAC address del Hc05
    private static String address = null;

    private ContractWelcome.Presenter presenter;

    private ProgressDialog mProgressDlg;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter mBluetoothAdapter;

    public static final int MULTIPLE_PERMISSIONS = 10;

    String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textTitulo = (TextView) findViewById(R.id.textView3);
        textPresentacion = (TextView) findViewById(R.id.textView4);
        btnEmparejar = (Button) findViewById(R.id.button3);
        btnVerBanios = (Button) findViewById(R.id.button2);
        //btnBuscarBanios = (Button) findViewById(R.id.); //faltaria agregarlo en el layout

        registrarBroadcasts();

        presenter.detectPairDevices();

        //Se Crea la ventana de dialogo que indica que se esta buscando dispositivos bluethoot
        mProgressDlg = new ProgressDialog(this);

        mProgressDlg.setMessage("Buscando dispositivos...");
        mProgressDlg.setCancelable(false);


        presenter = new PresentWelcome(this, new ModelWelcome());

        Log.i("Ejecuto","Ejecuto onCreate");
    }





    private void registrarBroadcasts() {
        //se definen un broadcastReceiver que captura el broadcast del SO cuando captura los siguientes eventos:
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND); //Se encuentra un dispositivo bluethoot al realizar una busqueda
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //Cuando se comienza una busqueda de bluethoot
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //cuando la busqueda de bluethoot finaliza

        //se define (registra) el handler que captura los broadcast anterirmente mencionados.
        registerReceiver(mReceiver, filter);
    }

    protected  void enableComponent()
    {
        //se determina si existe bluethoot en el celular
        if (mBluetoothAdapter != null)
        {
            //si el celular soporta bluethoot, se definen los listener para los botones de la activity

            btnEmparejar.setOnClickListener(btnListener);
            btnVerBanios.setOnClickListener(btnListener);

            //se determina si esta activado el bluethoot
            if (mBluetoothAdapter.isEnabled())
            {
                //se informa si esta habilitado
                //showEnabled();
                showToast("El bluetooth se encuentra habilitado");
            }
            else
            {
                //se informa si esta deshabilitado
                //showDisabled();
                showToast("Bluetooth deshabilitado. Se requiere activación");
            }
        }

    }


    //Metodo que chequea si estan habilitados los permisos
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        //Se chequea si la version de Android es menor a la 6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }


        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart()
    {
        Log.i("Ejecuto","Ejecuto Onstart");
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Obtengo el parametro, aplicando un Bundle, que me indica la Mac Adress del HC05
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            address = extras.getString("MAC_HC05");
        }
    }

    @Override
    protected void onPause() {
        Log.i("Ejecuto","Ejecuto OnPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Ejecuto","Ejecuto OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("Ejecuto","Ejecuto OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Ejecuto","Ejecuto OnDestroy");
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button2: //verBaños
                    break;
                case R.id.button3: //emparejar BT

                    Intent intent = new Intent(Welcome.this, ListDevices.class);

                    intent.putParcelableArrayListExtra("device.list", mDeviceList);

                    startActivity(intent);

                    break;
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            presenter.foundDevices(intent);
        }
    };


    public void goToBT(View view) {
        Intent goToBT = new Intent(this, ListDevices.class);
        startActivity(goToBT);
    }

    public void goToList(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        goToList.putExtra("MAC_HC05", address);
        startActivity(goToList);
    }

    public void showMsg (String msge) {
        showToast(msge);
    }

    public void enableButtonsEmparejamiento(boolean dispEmparejado) {
        btnEmparejar.setEnabled(!dispEmparejado);
        btnVerBanios.setEnabled(dispEmparejado);
    }


    public void setDevicesFound(ArrayList<BluetoothDevice> pDeviceList) {
        mDeviceList = pDeviceList;
    }
}