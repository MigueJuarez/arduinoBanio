package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;

import com.example.arduinobanio.ContractWelcome;
import com.example.arduinobanio.R;
import com.example.arduinobanio.modelo.ModelWelcome;
import com.example.arduinobanio.presentador.PresentWelcome;

public class Welcome extends AppCompatActivity implements ContractWelcome.View, SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

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


    String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textTitulo = (TextView) findViewById(R.id.textView3);
        textPresentacion = (TextView) findViewById(R.id.textView4);
        btnVerBanios = (Button) findViewById(R.id.verBanios);
        //btnBuscarBanios = (Button) findViewById(R.id.); //faltaria agregarlo en el layout

        registrarBroadcasts();

        presenter = new PresentWelcome(this);

        // Inicio sensorManager y me traigo acelerometro
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),2 );
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
        // registro la activity como listener del sensor
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        Log.i("Ejecuto","Ejecuto OnPause");
        super.onPause();
        sensorManager.unregisterListener(this);
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
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.verBanios: //verBaños
                    goToListaBanios(view);
                    break;
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            presenter.foundDevices(intent);
        }
    };

    public void goToListaBanios(View view) {
        Intent goToList = new Intent(this, ListaBanios.class);
        goToList.putExtra("MAC_HC05", address);
        startActivity(goToList);
    }

    public void goToMain() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }

    public void showMsg (String msge) {
        showToast(msge);
    }


    public void setDevicesFound(ArrayList<BluetoothDevice> pDeviceList) {
        mDeviceList = pDeviceList;
    }

    /**
     * Sensor event listener
     */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // this.showMsg("onAccuracyChanged");
    }

    private static final float SHAKE_THRESHOLD = 5.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getName().equals(accelerometer.getName())) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                //this.showMsg("Acceleration is " + acceleration + "m/s^2");

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    this.showMsg("Usuario deslogueado");
                    this.goToMain();
                }
            }
        }
    }
}