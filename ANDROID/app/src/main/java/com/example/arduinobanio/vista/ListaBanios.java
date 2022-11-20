package com.example.arduinobanio.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.arduinobanio.R;
import com.example.arduinobanio.thread.ThreadAsyncTaskBanioList;

import java.util.Objects;


public class ListaBanios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_banios);
        initSpinnerStatus();
        initRecyclerView();
    }

    @Override
    //Cada vez que se detecta el evento OnResume se establece la comunicacion con el HC05, creando un
    //socketBluethoot
    public void onResume() {
        super.onResume();
    }

    private void initSpinnerStatus() {
        Spinner spinner = findViewById(R.id.spinner);

        String [] opciones = {"Limpio", "Sucio", "Muy Sucio"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvListaBanios);
        ThreadAsyncTaskBanioList thread = new ThreadAsyncTaskBanioList();
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recyclerView, this);
    }

    public void goToWelcome(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }

    public void goToDetail(View view) {
        Intent goToDetail = new Intent(this, BanioDetalle.class);
        startActivity(goToDetail);
    }

    public void filterList(View view) {
        Intent filterList = new Intent(this, ListaBaniosFiltrada.class);
        startActivity(filterList);
    }
}