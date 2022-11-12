package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arduinobanio.R;

public class MainActivity extends AppCompatActivity {

    public static final boolean DEBUGGER_ENABLED = false;

    private TextView textView2;
    private TextView textView;
    private Button ingresar;
    private EditText usuario;
    private EditText contrasenia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.textView = (TextView) findViewById(R.id.textView);
        this.textView2 = (TextView) findViewById(R.id.textView2);
        this.ingresar = (Button) findViewById(R.id.goHome);
        this.usuario = (EditText) findViewById(R.id.editTextTextPersonName);
        this.contrasenia = (EditText) findViewById(R.id.editTextTextPassword);


        ingresar.setOnClickListener(ingresarListener);

        Log.i("Ejecuto","Ejecuto onCreate");
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
        Log.i("Ejecuto","Ejecuto OnResume");
        super.onResume();
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

   private View.OnClickListener ingresarListener = new View.OnClickListener() {
       public void onClick(View view) {
           goToWelcome(view);
       }
   };

    // Metodo boton siguiente donde se realiza la transicion a
    private void goToWelcome(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }
}