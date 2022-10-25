package com.example.arduinobanio.thread;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arduinobanio.recyclerView.BanioItem;
import com.example.arduinobanio.recyclerView.adapter.BanioAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ThreadAsyncTaskBanioListFiltrada extends AsyncTask {

    List<BanioItem> banios = Collections.singletonList(
            new BanioItem("Planta Baja", "Sucio")
    );

    @Override
    protected Object doInBackground(Object[] objects) {

        RecyclerView recyclerView = (RecyclerView) objects[0];
        recyclerView.setLayoutManager(new LinearLayoutManager((Context) objects[1]));
        recyclerView.setAdapter(new BanioAdapter(banios));
        return recyclerView;
    }
}
